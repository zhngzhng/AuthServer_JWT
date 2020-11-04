package njgis.opengms.authserver.dao;

import com.mongodb.client.result.UpdateResult;
import com.sun.org.apache.bcel.internal.generic.MONITORENTER;
import njgis.opengms.authserver.pojo.User;
import njgis.opengms.authserver.util.CommonUtil;
import njgis.opengms.authserver.util.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CommonUtil customUtil;

    private final String USER_COLLECTION = "users";

    public UserDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User findUserByFiled(String filedName,String filedValue) {
        User user = mongoTemplate.findOne(new Query(Criteria.where(filedName).is(filedValue)), User.class, USER_COLLECTION);
        if (user != null){
            return user;
        }
        return null;
    }

    @Override
    public int saveUser(User user) {
        Query query = Query.query(Criteria.where("email").is(user.getEmail()));
        if (!mongoTemplate.find(query, User.class).isEmpty()) {
            return -1;
        } else {
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);
            user.setPassword(new Md5PasswordEncoder().encode(user.getPassword()));
//            创建时间
            Long time = Long.parseLong(System.currentTimeMillis() + "");
            Date createDate = new Date(time);
            Date regisDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String regisTime = dateFormat.format(regisDate);
            user.setCreatTime(regisTime);
            mongoTemplate.save(user);
            return 1;
        }
    }

    @Override
    public String sendResetPwdEmail(String email) {
        try {
            Random random = new Random();
            String password = "";
            for (int i = 0; i < 8; i++) {
                int num = random.nextInt(62);
                if (num >= 0 && num < 10) {
                    password += num;
                } else if (num >= 10 && num < 36) {
                    num -= 10;
                    num += 65;
                    char c = (char) num;
                    password += c;
                } else {
                    num -= 36;
                    num += 97;
                    char c = (char) num;
                    password += c;
                }
            }
            User user = mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class);
            if (user !=null){
                //重置密码，使用随机生成的密码替代原来的密码
//                user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
                String md5Pwd = new Md5PasswordEncoder().encode(password);
                Update update = new Update().set("password", md5Pwd);
                mongoTemplate.updateFirst(new Query(Criteria.where("email").is(email)), update,User.class);
//              完成密码更新，发送邮件
                String subject = "OpenGMS Portal Password Reset";
                String content = "Hello " + user.getName() + ":<br/>" +
                        "Your password has been reset to <b>" + password + "</b>. You can use it to change the password.<br/>" +
                        "Welcome to <a href='https://geomodeling.njnu.edu.cn' target='_blank'>OpenGMS</a> !";

                //邮件开始发送
                Boolean flag = customUtil.sendEmail(email, subject, content);
                if (flag){
                    return "suc";
                }else {
                    return "send fail";
                }

            }else {
                return "no user";
            }
        }catch (Exception e){
            return "error";
        }
    }

    @Override
    public void updateInfo(String filedName, String filedValue, HashMap<String, Object> infoMap) {
        Query query = new Query(Criteria.where(filedName).is(filedValue));
        Update update = new Update();
        for (Map.Entry<String, Object> entry: infoMap.entrySet()){
            update.set(entry.getKey(), entry.getValue());
        }
        mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public int updatePassword(String email, String oldPwd, String newPwd) {
        User user = findUserByFiled("email", email);
        String md5Pwd = new Md5PasswordEncoder().encode(oldPwd);
        if (user.getPassword().equals(md5Pwd)){
            try {
                mongoTemplate.updateFirst(new Query(Criteria.where("email").is(email)), new Update().set("password", new Md5PasswordEncoder().encode(newPwd)),User.class);
                return 1;
            }catch (Exception e){
                return -1;
            }
        }
        return 0;
    }
}
