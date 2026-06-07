package com.backend.servlet;

import com.backend.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class BaseServlet extends HttpServlet {
    protected void writeJson(HttpServletResponse resp, Map<String, Object> result) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(JsonUtil.toJson(result));
        out.flush();
        out.close();
    }
}