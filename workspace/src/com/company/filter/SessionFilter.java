package com.company.filter;

/**
 * Created by didi on 2018/12/27.
 */
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter{
    private String excludedPage;
    private String[] excludedPages;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludedPage=filterConfig.getInitParameter("ignoneServlet");
        if(excludedPage!=null&&excludedPage.length()>0){
            excludedPages=excludedPage.split(",");
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)throws IOException,ServletException{
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) res;
        boolean flag=false;
        //System.out.println(request.getRequestURI());
        for(String page:excludedPages){
            if(request.getRequestURI().contains(page)){
                flag=true;
                break;
            }
        }
        if(flag){
            //System.out.println("flag=true,");
            filterChain.doFilter(request,response);
        } else{
            HttpSession session=request.getSession(false);
            if(session==null||session.getAttribute("username")==null){
                response.setContentType("application/json");//这一句不要忘了
                response.setCharacterEncoding("UTF-8");
                //response.sendRedirect("index.jsp");
                System.out.println("为什么过期了");
                JsonObject json=new JsonObject();
                json.addProperty("url","http://localhost:8080/book/index.jsp");
                json.addProperty("status",10000);
                PrintWriter out=response.getWriter();
                out.println(json);
                /**
                Gson gson=new Gson();
                JsonObject json=new JsonObject();
                json.addProperty("url","http://localhost:8080/book/index.jsp");
                json.addProperty("status",10000);
                PrintWriter out=response.getWriter();
                out.println(json);
                System.out.println("go to index.jsp");
                */
                //String errors="session expired,please login again";
                //request.setAttribute("Message",errors);
                //跳转至登录页面
                //request.getRequestDispatcher("index.jsp").forward(request,response);
            }else{
                //System.out.println(session.getAttribute("username"));
                //System.out.println("normal request");
                filterChain.doFilter(request,response);
            }
        }

    }

}
