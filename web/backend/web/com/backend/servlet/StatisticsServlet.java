package web.backend.servlet;

import web.backend.service.StatisticsService;
import com.backend.dao.StatisticsDAO;
import web.backend.util.JsonUtil;
import web.backend.util.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/stat")
public class StatisticsServlet extends HttpServlet {
    private final StatisticsService statService = new StatisticsService(new StatisticsDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        Map<String,Object> statData = statService.getGlobalStat();
        Map<String,Object> result = ResultUtil.success(statData);
        resp.getWriter().write(JsonUtil.toJson(result));
    }
}