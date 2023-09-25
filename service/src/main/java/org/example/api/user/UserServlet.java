package org.example.api.user;

import com.google.gson.Gson;
import org.example.common.HttpUtil;
import org.example.register.dto.RegisterResponse;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter()
@WebServlet(urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {
    private final UserDatabaseService userDbService;

    public UserServlet() throws SQLException {
        this.userDbService = new UserDatabaseService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            HttpUtil.ResponseToJson(res, this.userDbService.getUsers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}