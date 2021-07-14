package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分頁查詢
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收參數
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接收rname線路名稱
        //＊＊這邊要注意老師完全沒有講的部份 我是上網看到文章才改過來 在使用debug方式來查出來rname出了問題
        String rnameStr = request.getParameter("rname");
        String rname = null;
        //當rnameStr為"" (更新完cid=5的頁面)和"null"(按分頁條數)時
        //所以這邊也要做2.處理參數
        if (rnameStr != "" && !"null".equals(rnameStr)) {
            rname = new String(rnameStr.getBytes("iso-8859-1"), "utf-8");
        }
        //2.處理參數
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }


        int pageSize = 0;//每頁顯示條數
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3.調用service查詢pagebean對象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
        //4.將pageBean對象序列化為json,返回
        writeValue(pb, response);


    }

    /**
     * 根據id查詢一個旅遊線路的詳細訊息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收id
        String rid = request.getParameter("rid");
        //2.調用service查詢route對象
        Route route = routeService.findOne(rid);
        //3.轉為json寫回客戶端
        writeValue(route, response);
    }

    /**
     * 判斷當前登錄用戶是否收藏過該線路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isfavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.獲取線路id
        String rid = request.getParameter("rid");
        //2.獲取當前登錄的用戶user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用戶尚未登陸
            uid = 0;
        } else {
            //用戶已經登陸
            uid = user.getUid();
        }
        //3.調用FavoriteService查詢是否收藏

        boolean flag = favoriteService.isFavorite(rid, uid);
        //4.寫回客戶端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.獲取線路rid
        String rid = request.getParameter("rid");
        //2.獲取當前登錄的用戶user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用戶尚未登陸
            return ;
        } else {
            //用戶已經登陸
            uid = user.getUid();
        }

        //3.調用service添加
        favoriteService.add(rid,uid);
    }
}
