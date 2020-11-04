package njgis.opengms.authserver.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "resources")
public class Resource {
    private String uid;
    private String name;
    private String address;
    private String type;
    private String privacy;
    private String thumbnail;
    private String editToolInfo;
    private String fileSize;
    private String parent;
    private String md5;
    private String suffix;
    private String description;
    private String template;
    private String uploadTime;
    private ArrayList<Resource> children;
}
