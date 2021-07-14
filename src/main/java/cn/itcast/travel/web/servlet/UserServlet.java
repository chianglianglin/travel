package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService service = new UserServiceImpl();

    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //驗證校驗
        String check = request.getParameter("check");
        //從session中獲取驗證碼
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //比較
        if(checkcode_server == null ||!checkcode_server.equalsIgnoreCase(check)){
            //驗證碼錯誤
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("驗證碼錯誤");
            //將info對象序列化為json
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
            String json = writeValueAs(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //1.獲取數據
        Map<String, String[]> map = request.getParameterMap();
        //2.封裝對象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.調用service完成註冊
//        UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4.響應結果
        if(flag) {
            //註冊成功
            info.setFlag(true);
        }else {
            //註冊失敗
            info.setFlag(false);
            info.setErrorMsg("註冊失敗");
        }
        //將info對象序列化為json
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
        String json = writeValueAs(info);

        //將json數據返回客戶端
        //設置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }



    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.獲取用戶名和密碼數據
        Map<String, String[]> map = request.getParameterMap();
        //2.封裝User對象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.調用Service查詢
//        UserServiceImpl service = new UserServiceImpl();
        User u = service.login(user);
        ResultInfo info = new ResultInfo();

        //4.判斷用戶名是否為null
        if(u == null){
            //用戶名密碼錯誤
            info.setFlag(false);
            info.setErrorMsg("用戶名密碼或錯誤");
        }

        //5.判斷用戶是否激活
        if(u != null && !"Y".equals(u.getStatus())){
            //用戶尚未激活
            info.setFlag(false);
            info.setErrorMsg("你尚未激活,請激活");
        }
        //6.判斷登錄成功
        if(u != null && "Y".equals(u.getStatus())){
            //＊＊＊注意這裡老師上課沒有講到標記 我上網才找到結果是要登入成功後要把用戶訊息寫入到session裏才能傳給前端
            request.getSession().setAttribute("user",u);
            //登陸成功
            info.setFlag(true);

        }
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),info);
        writeValue(info,response);

    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //從session中獲取登陸用戶
        Object user = request.getSession().getAttribute("user");
//        //將user寫回客戶端
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),user);

        writeValue(user,response);
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.銷毀session
        request.getSession().invalidate();

        //2.跳轉到登入頁面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.獲取激活碼
        String code = request.getParameter("code");
        if(code != null) {
            //2.調用service完成激活
//            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判斷標記
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功,請<a href='login.html'>登入</a>";
            } else {
                //激活失敗
                msg = "激活失敗,請聯繫管理員";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("userServlet的find方法");
    }
}
