package web.backend.servlet;

import com.backend.util.ResultUtil;
import com.backend.entity.Intersection;
import com.backend.service.IntersectionService;
import com.backend.util.ResultUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/intersection")
public class IntersectionServlet extends HttpServlet {

    // 初始化Service（实际项目用依赖注入，这里直接new简化）
    private final IntersectionService intersectionService = new IntersectionService(new IntersectionDAO());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        // 解析请求体
        Intersection intersection = ResultUtil.readJson(req.getInputStream(), Intersection.class);
        boolean success = intersectionService.add(intersection);
        // 返回统一格式
        if (success) {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success("新增成功"));
        } else {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(500, "新增失败"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String id = req.getParameter("id");
        if (id == null) {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(400, "缺少id参数"));
            return;
        }
        boolean success = intersectionService.delete(id);
        if (success) {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success("删除成功"));
        } else {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(404, "数据不存在"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Intersection intersection = ResultUtil.readJson(req.getInputStream(), Intersection.class);
        boolean success = intersectionService.update(intersection);
        if (success) {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success("修改成功"));
        } else {
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.error(404, "数据不存在"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String id = req.getParameter("id");
        if (id != null) {
            // 查询单个
            Intersection intersection = intersectionService.getById(id);
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success(intersection));
        } else {
            // 查询所有
            List<Intersection> list = intersectionService.listAll();
            ResultUtil.writeJson(resp.getWriter(), ResultUtil.success(list));
        }
    }
}