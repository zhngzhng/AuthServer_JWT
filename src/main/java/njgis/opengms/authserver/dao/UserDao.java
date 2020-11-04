package njgis.opengms.authserver.dao;

import njgis.opengms.authserver.pojo.User;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;

public interface UserDao {
    User findUserByFiled(String filedName, String filedValue);
    int saveUser(User user);
    String sendResetPwdEmail(String email);
    void updateInfo(String filedName, String filedValue, HashMap<String, Object> infoMap);
    int updatePassword(String email, String oldPwd, String newPwd);
}
