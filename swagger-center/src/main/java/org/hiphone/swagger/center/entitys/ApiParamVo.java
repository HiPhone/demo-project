package org.hiphone.swagger.center.entitys;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ApiModel(value = "ApiParamVo", description = "Api参数的封装类")
public class ApiParamVo implements Serializable {

    private static final long serialVersionUID = -1696799743978051853L;

    @ApiModelProperty(value = "参数的名称", name = "name")
    private String name;

    @ApiModelProperty(value = "参数的描述", name = "description")
    private String description;

    @ApiModelProperty(value = "参数的java类型", name = "type")
    private String type;

    @ApiModelProperty(value = "参数是否必须", name = "required")
    private String required;

    @ApiModelProperty(value = "若参数为封装类，封装类的信息", name = "schema")
    private JSONArray schema;

}
