package org.example.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.api.user.entity.User;
import org.example.api.user.UserDatabaseService;
import org.example.auth.dto.AuthRequest;
import org.example.auth.dto.AuthResponse;
import org.example.common.HttpUtil;
import org.example.common.JwtUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet(urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {
    private final UserDatabaseService userDbService;
    private final JwtUtils jwtUtils = new JwtUtils();

    public AuthServlet() throws SQLException {
        this.userDbService = new UserDatabaseService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            AuthRequest body = HttpUtil.GetRequestBody(req, AuthRequest.class);
            User user = this.userDbService.getUserByEmail(body.getEmail());
            AuthResponse responseBody;
            if (!Objects.equals(
                    DigestUtils.sha256Hex(body.getPassword()),
                    user.getPassword()
            )) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseBody = new AuthResponse("", false);
            } else {
                String token = this.jwtUtils.generateJwtToken(user);
                responseBody = new AuthResponse(token, true);
            }
            HttpUtil.ResponseToJson(res, responseBody);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}