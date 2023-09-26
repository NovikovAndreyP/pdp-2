package org.example.api.user;

import org.example.api.user.entity.User;
import org.example.common.HttpUtil;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            Stream<User> userStream = this.userDbService.getUsers().stream();
            switch (req.getParameter("sort")) {
                case ("name"):
                    userStream = userStream.sorted(Comparator.comparing(User::getName));
                    break;
                case ("email"):
                    userStream = userStream.sorted(Comparator.comparing(User::getEmail));
                    break;
                default:
                    break;
            }
            HttpUtil.ResponseToJson(res, userStream.collect(Collectors.toCollection(ArrayList::new)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}