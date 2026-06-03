package web.backend.service;

import com.backend.dao.StatisticsDAO;
import java.util.Map;

public class StatisticsService {
    private final StatisticsDAO statisticsDAO;
    public StatisticsService(StatisticsDAO statisticsDAO){
        this.statisticsDAO = statisticsDAO;
    }
    //获取全局统计数据：总方案、启用方案、关联路口数
    public Map<String,Object> getGlobalStat(){
        return statisticsDAO.getGlobalStats();
    }
}