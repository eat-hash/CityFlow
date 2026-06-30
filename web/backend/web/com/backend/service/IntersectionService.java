package com.backend.service;

import com.backend.dao.IntersectionDAO;
import com.backend.entity.Intersection;
import java.util.List;

public class IntersectionService {

    private final IntersectionDAO intersectionDAO;

    // 构造注入DAO
    public IntersectionService(IntersectionDAO intersectionDAO) {
        this.intersectionDAO = intersectionDAO;
    }

    // 新增路口
    public boolean add(Intersection intersection) {
        return intersectionDAO.addIntersection(intersection);
    }

    // 删除路口
    public boolean delete(String id) {
        return intersectionDAO.deleteById(id);
    }

    // 修改路口
    public boolean update(Intersection intersection) {
        return intersectionDAO.update(intersection);
    }

    // 查询单个路口
    public Intersection getById(String id) {
        return intersectionDAO.getById(id);
    }

    // 查询所有路口
    public List<Intersection> listAll() {
        return intersectionDAO.listAll();
    }
}