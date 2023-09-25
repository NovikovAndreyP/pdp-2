package org.example.common;

import com.google.gson.Gson;
import org.example.api.user.entity.User;
import org.example.register.dto.RegisterResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final Gson gson = new Gson();

    public static <T>T GetRequestBody(HttpServletRequest req, Class<T> type) throws IOException {
        String bodyString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return gson.fromJson(bodyString, type);
    }

    public static void ResponseToJson(HttpServletResponse res, Object resBody) throws IOException {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(resBody));
        out.flush();
    }
}
