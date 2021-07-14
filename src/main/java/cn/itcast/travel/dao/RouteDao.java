package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根據cid查詢總紀路數
     */
    public int findTotalCount(int cid ,String rname);

    /**
     * 根據cid, start,pageSize查詢當前頁的數據集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize,String rname);

    public Route findOne(int rid);
}
