package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        UserServiceImpl service = new UserServiceImpl();
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
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

    }
}
