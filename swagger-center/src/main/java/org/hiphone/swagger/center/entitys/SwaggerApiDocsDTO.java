package org.hiphone.swagger.center.entitys;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ToString
@ApiModel(value = "SwaggerApiDocsDTO", description = "swagger api信息封装传输类")
public class SwaggerApiDocsDTO {

    @ApiParam(value = "服务在eureka中注册的服务名称", name = "serviceName")
    private String serviceName;

    @ApiParam(value = "服务的api-docs url", name = "swaggerServiceUrl")
    private String swaggerServiceUrl;

    @ApiParam(value = "服务的swagger api文档", name = "swaggerApiDocs")
    private JSONObject swaggerApiDocs;

    @ApiParam(value = "服务对接swagger不规范的数量", name = "notStandardNum")
    private Integer notStandardNum;

    @ApiParam(value = "信息的创建者", name = "createBy")
    private String createBy;

    @ApiParam(value = "信息的更新者", name = "updateBy")
    private String updateBy;

    @ApiParam(value = "信息的创建时间", name = "createTime")
    private Timestamp createTime;

    @ApiParam(value = "信息的更新时间", name = "updateTime")
    private Timestamp updateTime;

}
