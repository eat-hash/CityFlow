package com.traffic.servlet;
import com.traffic.util.JsonUtil;
import com.traffic.model.*;
import com.traffic.service.TrafficOptService;
import com.traffic.util.ResultUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/traffic/*")
public class TrafficServlet extends BaseServlet {

    private TrafficOptService service = new TrafficOptService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/optimize/single".equals(path)) {
            SingleOptimizeRequest reqObj = JsonUtil.fromJson(req.getReader(), SingleOptimizeRequest.class);
            SingleOptimizeResponse respObj = service.optimizeSingle(
                    reqObj.getIntersectionId(),
                    reqObj.getTrafficFlow(),
                    reqObj.getMinGreen(),
                    reqObj.getMaxCycle()
            );
            writeJson(resp, ResultUtil.success(respObj));
        } else if ("/optimize/batch".equals(path)) {
            BatchOptimizeRequest reqObj = JsonUtil.fromJson(req.getReader(), BatchOptimizeRequest.class);
            List<SingleOptimizeResponse> list = service.optimizeBatch(reqObj.getIntersectionList());
            writeJson(resp, ResultUtil.success(list));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/data/test".equals(path)) {
            List<Map<String, Object>> testData = service.getTestData();
            writeJson(resp, ResultUtil.success(testData));
        }
    }
}