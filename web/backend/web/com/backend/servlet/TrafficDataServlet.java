package com.backend.servlet;

import com.backend.entity.TrafficData;
import com.backend.service.TrafficDataService;
import com.backend.util.ResultUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/traffic")
public class TrafficDataServlet extends HttpServlet {

    private final TrafficDataService trafficDataService = new TrafficDataService(new TrafficDataDAO());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String action = req.getParameter("action");

        if ("import".equals(action)) {
            // 批量导入CSV
            String filePath = req.getParameter("filePath");
            if (filePath == null) {
                ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(400, "缺少filePath参数"));
                return;
            }
            try {
                trafficDataService.importFromCSV(filePath);
                ResultUtil.writeJson(resp.getWriter(), ResultUtil.success("导入成功"));
            } catch (IOException e) {
                ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(500, "导入失败：" + e.getMessage()));
            }
        } else {
            // 单个上传
            TrafficData data = ResultUtil.readJson(req.getInputStream(), TrafficData.class);
            boolean success = trafficDataService.add(data);
            if (success) {
                ResultUtil.writeJson(resp.getWriter(), ResultUtil.success("上传成功"));
            } else {
                ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(500, "上传失败"));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String intersectionId = req.getParameter("intersectionId");
        if (intersectionId != null) {
            // 按路口查询
            List<TrafficData> list = trafficDataService.getByIntersectionId(intersectionId);
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success(list));
        } else {
            // 查询所有
            List<TrafficData> list = trafficDataService.listAll();
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success(list));
        }
    }
}