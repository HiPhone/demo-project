package org.hiphone.swagger.center.entitys;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ApiModel(value = "SwaggerApiDTO", description = "Api的封装类")
public class SwaggerApiDocsDTO implements Serializable {

    private static final long serialVersionUID = -8012571777767128070L;
    @ApiModelProperty(value = "微服务的服务名称", name = "serviceName")
    private String serviceName;

    @ApiModelProperty(value = "swagger请求的api-docs地址", name = "swaggerApiUrl")
    private String swaggerServiceUrl;

    @ApiModelProperty(value = "微服务的api-docs", name = "swaggerApiDocs")
    private JSONObject swaggerApiDocs;

    @ApiModelProperty(value = "对接不规范的数量", name = "notStandardNum")
    private Integer notStandardNum;

    @ApiModelProperty(value = "记录创建人", name = "createBy")
    private String createBy;

    @ApiModelProperty(value = "记录更新人", name = "updateBy")
    private String updateBy;

    @ApiModelProperty(value = "记录创建时间", name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "记录更新时间", name = "updateTime")
    private Date updateTime;


    @Override
    public String toString() {
        return "SwaggerApiDocsDTO{" +
                "serviceName='" + serviceName + '\'' +
                ", swaggerServiceUrl='" + swaggerServiceUrl + '\'' +
                ", swaggerApiDocs=" + swaggerApiDocs +
                ", notStandardNum=" + notStandardNum +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
