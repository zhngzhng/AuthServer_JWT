package njgis.opengms.authserver.dao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import njgis.opengms.authserver.pojo.OauthClientDetails;
import njgis.opengms.authserver.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDaoImpl implements ClientDao{
    private final String COLLECTION_NAME = "oauth2ClientsDetails";
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CommonUtil commonUtil;
    @Override
    public BaseClientDetails findByClientId(String clientId) {
        BaseClientDetails client = mongoTemplate.findOne(new Query(Criteria.where("clientId").is(clientId)), BaseClientDetails.class, COLLECTION_NAME);
        if (client != null){
            return client;
        }
        return null;
    }

    @Override
    public String saveClient(OauthClientDetails oauthClient) {
        try{
            mongoTemplate.save(oauthClient);
            return oauthClient.getClientId();
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateClientInfo(JSONObject jsonParam) {
        String clientId = jsonParam.getString("clientId");
        BaseClientDetails client = findByClientId(clientId);
        if (client == null){
            return "There isn't this client, please check your client Id.";
        }
        try {
            Update update = commonUtil.setUpdate(jsonParam);
            mongoTemplate.updateFirst(new Query(Criteria.where("clientId").is(clientId)), update, OauthClientDetails.class);
            return clientId;
        }catch (Exception e){
            return "Fail";
        }
    }
}
