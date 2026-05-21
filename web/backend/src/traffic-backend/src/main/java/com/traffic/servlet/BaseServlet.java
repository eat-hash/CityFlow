package com.traffic.servlet;

import com.traffic.util.JsonUtil;
import com.traffic.util.ResultUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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