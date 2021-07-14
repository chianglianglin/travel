package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.獲取激活碼
        String code = request.getParameter("code");
        if(code != null){
            //2.調用service完成激活
            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判斷標記
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功,請<a href='login.html'>登入</a>";
            }else{
                //激活失敗
                msg = "激活失敗,請聯繫管理員";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
