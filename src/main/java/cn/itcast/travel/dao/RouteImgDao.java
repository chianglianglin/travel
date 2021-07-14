package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根據route的id查詢圖片
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
