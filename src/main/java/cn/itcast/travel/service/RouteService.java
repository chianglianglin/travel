package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {

    /**
     * 根據類別進行分頁查詢
     * @param cid
     * @param currentPage
     * @param PageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid,int currentPage,int PageSize,String rname);

    /**
     * 根據rid查詢
     * @param rid
     * @return
     */
     public Route findOne(String rid);
}
