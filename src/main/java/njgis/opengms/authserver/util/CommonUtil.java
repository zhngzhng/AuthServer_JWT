package njgis.opengms.authserver.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Service
public class CommonUtil {
    @Autowired
    JavaMailSender mailSender;
    /**
     *获取Ip地址
     * @param request
     * @return
     */
    public String getIpAddress(HttpServletRequest request){
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
                ipAddress =request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")){
                    //根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try{
                        inet = InetAddress.getLocalHost();
                    }catch (UnknownHostException e){
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            //对于通过多个代理的情况，第一个Ip为真实Ip，多个Ip按照“,”分割
            if (ipAddress !=null && ipAddress.length()>15){
                if (ipAddress.indexOf(",")>0){
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
                }
            }

        }catch (Exception e){
            ipAddress = "";
        }
        return ipAddress;
    }

    @Value("${spring.mail.username}")
    String mailAddress;

    public Boolean sendEmail(String to, String subject, String content){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            InternetAddress internetAddress = new InternetAddress(mailAddress, "OpenGMS Team", "UTF-8");
            helper.setFrom(internetAddress);
            helper.setTo(to);
            helper.setCc(mailAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return true;
        }catch (Exception e){
            System.out.println("邮件发送异常: " + e);
            return false;
        }
    }

    public Update setUpdate(JSONObject jsonParam){
        Update update = new Update();
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        //修改密码的话，记得需要MD5加密
        for (Map.Entry entry: jsonParam.entrySet()){
            if (entry.getKey().equals("clientSecret")){
                update.set((String)entry.getKey(), passwordEncoder.encode((String)entry.getValue()));
                continue;
            }
            update.set((String) entry.getKey(),entry.getValue());
        }
        return update;
    }

}
