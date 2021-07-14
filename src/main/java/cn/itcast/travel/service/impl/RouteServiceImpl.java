package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDalImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    private RouteImgDao routeImgDao = new RouteImgDalImpl();

    private SellerDao sellerDao = new SellerDaoImpl();

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封裝pageBean
        PageBean<Route> pb = new PageBean<Route>();
        //設置當前頁碼
        pb.setCurrentPage(currentPage);
        //設置每頁顯示條數
        pb.setPageSize(pageSize);
        //設置總紀錄數
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //設置當前頁顯示的數據集合
        int start = (currentPage - 1) * pageSize;//開始的紀錄數
        List<Route> list =routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);

        //設置總頁數=總頁數/每頁顯示條數
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize:(totalCount / pageSize)+1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根據rid去route表中查詢route對象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.根據route的id查詢圖片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());

        //2.2將集合設置到route對象
        route.setRouteImgList(routeImgList);
        //3.根據route的sid(商家id)查詢商家對象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //4.查詢收藏次數
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
