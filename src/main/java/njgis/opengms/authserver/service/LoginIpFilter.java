package njgis.opengms.authserver.service;

import njgis.opengms.authserver.dao.UserDaoImpl;
import njgis.opengms.authserver.pojo.User;
import njgis.opengms.authserver.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class LoginIpFilter implements Filter {
    @Autowired
    UserDaoImpl userDao;
    @Autowired
    CommonUtil commonUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        if ("POST".equals(req.getMethod()) && "/in".equals(req.getServletPath())){
            String email = req.getParameter("account");
            User user = userDao.findUserByFiled("email", email);
            HashMap<String, Object> infoMap = new HashMap<String, Object>();
            if (user != null){
                String ipAddress = commonUtil.getIpAddress(req);
                ArrayList<String> loginIp = user.getLoginIp();
                if (loginIp == null){
                    loginIp = new ArrayList<String>();
                }
                boolean contains = loginIp.contains(ipAddress);
                if (!contains){
                    loginIp.add(ipAddress);
                    infoMap.put("loginIp", loginIp);
                    userDao.updateInfo("email", email, infoMap);
                }
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
