package org.example.register;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.api.user.UserDatabaseService;
import org.example.api.user.entity.User;
import org.example.common.HttpUtil;
import org.example.register.dto.RegisterResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private final UserDatabaseService userDbService;

    public RegisterServlet() throws SQLException {
        this.userDbService = new UserDatabaseService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            User body = HttpUtil.GetRequestBody(req, User.class);
            body.setPassword(DigestUtils.sha256Hex(body.getPassword()));
            this.userDbService.addUser(body);
            HttpUtil.ResponseToJson(res, new RegisterResponse(true));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
