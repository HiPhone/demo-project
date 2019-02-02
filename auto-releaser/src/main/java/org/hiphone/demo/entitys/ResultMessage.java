package org.hiphone.demo.entitys;

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
@ApiModel(value = "ResultMessage", description = "将返回值封装，统一放回的数据报文")
public class ResultMessage implements Serializable {

    private static final long serialVersionUID = 1452124883024046815L;

    @ApiModelProperty(value = "返回码", name = "code")
    private Integer code;

    @ApiModelProperty(value = "返回码的解释", name = "message")
    private String message;

    @ApiModelProperty(value = "返回的数据", name = "data")
    private Object data;

    public ResultMessage(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}

