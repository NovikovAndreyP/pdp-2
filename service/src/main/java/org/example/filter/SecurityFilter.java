package org.example.filter;

import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.api.user.UserDatabaseService;
import org.example.api.user.entity.User;
import org.example.auth.dto.AuthResponse;
import org.example.common.JwtUtils;
import org.example.common.dto.UserCredentials;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/user")
public class SecurityFilter extends HttpFilter {
    private final UserDatabaseService userDbService;
    private final JwtUtils jwtUtils = new JwtUtils();

    public SecurityFilter() throws SQLException {
        this.userDbService = new UserDatabaseService();
    }


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            String authorization = req.getHeader("Authorization");
            if (authorization == null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String jwt = authorization.substring(7, authorization.length());
            if (jwtUtils.validateJwtToken(jwt)) {
                UserCredentials credentials = jwtUtils.getUserCredentialsFromJwtToken(jwt);
                User user = this.userDbService.getUserByEmail(credentials.getEmail());
                if (!Objects.equals(
                        credentials.getPassword(),
                        user.getPassword()
                )) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            chain.doFilter(req, res);
        } catch (SQLException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}
