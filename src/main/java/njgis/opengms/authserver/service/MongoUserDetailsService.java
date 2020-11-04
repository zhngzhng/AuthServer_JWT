package njgis.opengms.authserver.service;

import njgis.opengms.authserver.dao.UserDaoImpl;
import njgis.opengms.authserver.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MongoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDaoImpl userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findUserByFiled("email",email);
        if (user != null){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
