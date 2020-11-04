package njgis.opengms.authserver.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class Md5PasswordEncoder implements PasswordEncoder {
    private final Log logger = LogFactory.getLog(Md5PasswordEncoder.class);
    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null){
            throw new IllegalArgumentException("rawPassword cannot be null!");
        }
        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodePassword) {
        if (rawPassword == null){
            throw new IllegalArgumentException("rawPassword cannot be null!");
        }
        if (encodePassword == null || encodePassword.length() == 0){
            logger.warn("Empty encoded password");
        }
        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()).equals(encodePassword);
    }
}
