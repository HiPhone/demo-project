package org.hiphone.eureka.monitor.entitys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HiPhone
 */
@Setter
@Getter
@ApiModel(value = "ApplicationHistoryDto", description = "application历史记录的封装类")
public class ApplicationHistoryDto implements Serializable {

    private static final long serialVersionUID = -1739107968206405851L;

    @ApiModelProperty(value = "记录的id", name = "id")
    private Long id;

    @ApiModelProperty(value = "记录的集群id", name = "clusterId")
    private String clusterId;

    @ApiModelProperty(value = "记录的应用名称", name = "applicationName")
    private String applicationName;

    @ApiModelProperty(value = "记录的ip地址", name = "ipAddress")
    private String ipAddress;

    @ApiModelProperty(value = "记录的状态", name = "state")
    private Integer state;

    @ApiModelProperty(value = "产生记录的时间", name = "logTime")
    private Date logTime;

    @ApiModelProperty(value = "返回给前端的信息", name = "message")
    private String message;

}
