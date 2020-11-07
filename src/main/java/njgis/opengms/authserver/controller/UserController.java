package njgis.opengms.authserver.controller;


import njgis.opengms.authserver.dao.UserDaoImpl;
import njgis.opengms.authserver.pojo.User;
import njgis.opengms.authserver.util.JsonResult;
import njgis.opengms.authserver.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserDaoImpl userDao;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object saveUser(@RequestBody User user){
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try{
            int flag = userDao.saveUser(user);
            return flag;
        }catch (Exception e){
            return "Fail";
        }
    }
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonResult sendEmail(@RequestParam String email){
        String result = userDao.sendResetPwdEmail(email);
        return ResultUtils.success(result);
    }
    @RequestMapping(value = "/newPassword", method = RequestMethod.POST)
    public Object updateNewPassword(@RequestParam String email, @RequestParam String oldPwd, @RequestParam String newPwd){
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        return userDao.updatePassword(email, oldPwd, newPwd);
    }

}
