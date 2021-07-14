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
//name = "RegistUserServlet", value =
@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
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
        UserService service = new UserServiceImpl();
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
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //將json數據返回客戶端
        //設置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
