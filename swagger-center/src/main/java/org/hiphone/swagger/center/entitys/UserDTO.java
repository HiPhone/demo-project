package org.hiphone.swagger.center.entitys;

import com.alibaba.fastjson.annotation.JSONType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ToString
@ApiModel(value = "UserDTO", description = "用户信息封装数据传输类")
@JSONType(orders = {"id" , "loginName", "password", "role", "createBy", "createBy", "createTime", "updateBy", "updateTime"})
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -2221337494917385389L;

    @ApiParam(value = "数据生成的唯一id", name = "id")
    private Long id;

    @ApiModelProperty(value = "登陆名", name = "loginName")
    private String loginName;

    @ApiModelProperty(value = "登陆密码", name = "password")
    private String password;

    @ApiParam(value = "用户的角色", name = "role")
    private Integer role;

    @ApiParam(value = "角色创建人", name = "createBy")
    private String createBy;

    @ApiParam(value = "角色创建时间", name = "createTime")
    private Date createTime;

    @ApiParam(value = "角色更新人", name = "updateBy")
    private String updateBy;

    @ApiParam(value = "角色更新时间", name = "updateTime")
    private Date updateTime;
}
