package njgis.opengms.authserver.service;

import njgis.opengms.authserver.dao.ClientDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class MongoClientDetailsService implements ClientDetailsService {
    @Autowired
    ClientDaoImpl clientDao;
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails clientDetails = clientDao.findByClientId(clientId);
        if (clientDetails == null){
            throw new RuntimeException("Don't have client information");
        }
        return clientDetails;
    }
}
