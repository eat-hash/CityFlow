package web.backend.service;

import web.backend.dao.TimingPlanDAO;
import web.backend.entity.TimingPlan;
import java.util.List;

public class TimingPlanService {
    // 注入DAO，和之前Service构造方法写法一致
    private final TimingPlanDAO timingPlanDAO;
    public TimingPlanService(TimingPlanDAO timingPlanDAO){
        this.timingPlanDAO = timingPlanDAO;
    }

    //新增配时
    public int add(TimingPlan plan){
        return timingPlanDAO.insert(plan);
    }
    //修改配时
    public int update(TimingPlan plan){
        return timingPlanDAO.update(plan);
    }
    //删除配时
    public int delete(Integer id){
        return timingPlanDAO.deleteById(id);
    }
    //根据id查询单个
    public TimingPlan getById(Integer id){
        return timingPlanDAO.findById(id);
    }
    //分页列表
    public List<TimingPlan> pageList(int page,int size){
        return timingPlanDAO.findPage(page,size);
    }
    //查询总条数
    public long getTotalCount(){
        return timingPlanDAO.count();
    }
}