package org.hiphone.swagger.center.entitys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ApiModel(value = "SimplifyApiVo", description = "简化之后的Api信息类")
public class SimplifyApiVo implements Serializable {

    private static final long serialVersionUID = -932145470909509888L;

    @ApiModelProperty(value = "Api的命名", name = "path")
    private String path;

    @ApiModelProperty(value = "Api的方法", name = "method")
    private String method;

    @ApiModelProperty(value = "Api的说明", name = "description")
    private String description;

    @ApiModelProperty(value = "Api所属的Controller", name = "controller")
    private String controller;

    @ApiModelProperty(value = "Api所属Controller的说明", name = "controllerTag")
    private String controllerTag;


    private List<ApiParamVo> apiParamVos;

    public SimplifyApiVo(String path, String method, String description, String controller, String controllerTag, List<ApiParamVo> apiParamVos) {
        this.path = path;
        this.method = method;
        this.description = description;
        this.controller = controller;
        this.controllerTag = controllerTag;
        this.apiParamVos = apiParamVos;
    }
}
