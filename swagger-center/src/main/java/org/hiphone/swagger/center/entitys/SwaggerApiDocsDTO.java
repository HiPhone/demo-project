package org.hiphone.swagger.center.entitys;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author HiPhone
 */
@Getter
@Setter
public class SwaggerApiDocsDTO {

    /**
     * serviceName作为数据库的id字段
     */
    private String serviceName;
    private String swaggerServiceUrl;
    private JSONObject swaggerApiDocs;
    private Integer notStandardNum;
    private String createBy;
    private String updateBy;
    private Timestamp createTime;
    private Timestamp updateTime;

}
