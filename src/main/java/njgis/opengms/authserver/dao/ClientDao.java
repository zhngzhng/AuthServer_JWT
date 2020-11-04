package njgis.opengms.authserver.dao;

import com.alibaba.fastjson.JSONObject;
import njgis.opengms.authserver.pojo.OauthClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public interface ClientDao {
    BaseClientDetails findByClientId(String clientId);
    String saveClient(OauthClientDetails oauthClient);
    String updateClientInfo(JSONObject jsonParam);
}
