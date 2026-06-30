package web.backend.servlet;

import web.backend.entity.TimingPlan;
import web.backend.service.TimingPlanService;
import web.backend.dao.TimingPlanDAO;
import web.backend.util.JsonUtil;
import web.backend.util.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/plan")
public class TimingPlanServlet extends HttpServlet {
    //实例化Service，和IntersectionServlet、TrafficDataServlet写法统一
    private final TimingPlanService planService = new TimingPlanService(new TimingPlanDAO());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        BufferedReader br = req.getReader();
        TimingPlan plan = JsonUtil.fromJson(br,TimingPlan.class);
        int rows = planService.add(plan);
        Map<String,Object> res;
        if(rows>0){
            res = ResultUtil.success("新增配时方案成功");
        }else{
            res = ResultUtil.error(500,"新增失败");
        }
        resp.getWriter().write(JsonUtil.toJson(res));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        BufferedReader br = req.getReader();
        TimingPlan plan = JsonUtil.fromJson(br,TimingPlan.class);
        int rows = planService.update(plan);
        Map<String,Object> res = rows>0?ResultUtil.success("修改成功"):ResultUtil.error(500,"修改失败，数据不存在");
        resp.getWriter().write(JsonUtil.toJson(res));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String idStr = req.getParameter("id");
        Integer id = Integer.parseInt(idStr);
        int rows = planService.delete(id);
        Map<String,Object> res = rows>0?ResultUtil.success("删除成功"):ResultUtil.error(404,"无该方案");
        resp.getWriter().write(JsonUtil.toJson(res));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String idStr = req.getParameter("id");
        if(idStr!=null){
            //单条查询
            Integer id = Integer.parseInt(idStr);
            TimingPlan plan = planService.getById(id);
            Map<String,Object> res = plan!=null?ResultUtil.success(plan):ResultUtil.error(404,"数据不存在");
            resp.getWriter().write(JsonUtil.toJson(res));
        }else{
            //分页查询
            int page = Integer.parseInt(req.getParameter("page")==null?"1":req.getParameter("page"));
            int size = Integer.parseInt(req.getParameter("size")==null?"10":req.getParameter("size"));
            List<TimingPlan> list = planService.pageList(page,size);
            long total = planService.getTotalCount();
            Map<String,Object> dataMap = Map.of("list",list,"total",total);
            Map<String,Object> res = ResultUtil.success(dataMap);
            resp.getWriter().write(JsonUtil.toJson(res));
        }
    }
}