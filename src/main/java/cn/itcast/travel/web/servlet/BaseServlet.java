package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException , IOException {
//        System.out.println("baseservlet的service被執行了");
         //完成方法分發
        //1.獲取請求路徑
        String uri = req.getRequestURI();
        System.out.println("請求uri:"+uri);
        //2.獲取方法名稱
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("方法名稱："+methodName);
        //3.獲取方法對象Method
        try {
            //忽略訪問權限修飾符,獲取方法
//            Method method =this.getClass().getDeclaredMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            Method method =this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);

            //4.執行方法
            //暴力反射
//            method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


     }

    /**
     * 直接將傳入的對象序列化為json ,並且寫回客戶端
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 將傳入的對象序列化為json,並返回給調用者
     * @param obj
     * @return
     */
    public String writeValueAs(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
