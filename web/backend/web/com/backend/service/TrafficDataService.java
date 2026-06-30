package com.backend.service;

import com.backend.dao.TrafficDataDAO;
import com.backend.entity.TrafficData;
import com.backend.util.CSVUtil;
import java.io.IOException;
import java.util.List;

public class TrafficDataService {

    private final TrafficDataDAO trafficDataDAO;

    // 构造注入DAO
    public TrafficDataService(TrafficDataDAO trafficDataDAO) {
        this.trafficDataDAO = trafficDataDAO;
    }

    // 单个上传数据
    public boolean add(TrafficData data) {
        return trafficDataDAO.addTrafficData(data);
    }

    // 批量导入CSV数据
    public void importFromCSV(String filePath) throws IOException {
        List<String[]> rows = CSVUtil.readCSV(filePath, true); // true=跳过表头
        for (String[] row : rows) {
            // 按你的CSV格式解析，这里假设列顺序为：id, intersectionId, time, flow
            TrafficData data = new TrafficData();
            data.setId(Integer.parseInt(row[0]));
            data.setIntersectionId(row[1]);
            data.setCollectTime(row[2]);
            data.setLaneFlow(row[3]);
            trafficDataDAO.addTrafficData(data);
        }
    }

    // 查询所有数据
    public List<TrafficData> listAll() {
        return trafficDataDAO.listAll();
    }

    // 按路口ID查询数据
    public List<TrafficData> getByIntersectionId(String intersectionId) {
        return trafficDataDAO.getByIntersectionId(intersectionId);
    }
}