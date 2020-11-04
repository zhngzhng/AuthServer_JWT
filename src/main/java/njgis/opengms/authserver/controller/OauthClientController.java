package njgis.opengms.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import njgis.opengms.authserver.dao.ClientDaoImpl;
import njgis.opengms.authserver.pojo.OauthClientDetails;
import njgis.opengms.authserver.util.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/OauthClient")
public class OauthClientController {
    @Autowired
    ClientDaoImpl clientDao;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addClient(@RequestBody OauthClientDetails client){
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        String encodePwd = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(encodePwd);
        try{
            return clientDao.saveClient(client);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateClient(@RequestBody JSONObject jsonParam){
        return clientDao.updateClientInfo(jsonParam);
    }

}
