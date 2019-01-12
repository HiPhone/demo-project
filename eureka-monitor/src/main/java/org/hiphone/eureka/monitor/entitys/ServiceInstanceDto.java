package org.hiphone.eureka.monitor.entitys;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ToString
@ApiModel(value = "ServiceInstanceDto", description = "eureka instance信息的封装类")
public class ServiceInstanceDto implements Serializable {

    private static final long serialVersionUID = 3353406941094995780L;

    @ApiModelProperty(value = "instance唯一的id", name = "instanceId")
    private String instanceId;

    @ApiModelProperty(value = "instance所属的集群id", name = "clusterId")
    private String clusterId;

    @ApiModelProperty(value = "instance所属的应用名", name = "applicationName")
    private String applicationName;

    @ApiModelProperty(value = "instance所在机器的hostname", name = "hostname")
    private String hostname;

    @ApiModelProperty(value = "instance所在机器的ip地址", name = "ipAddress")
    private String ipAddress;

    @ApiModelProperty(value = "instance占用的端口号", name = "servicePort")
    private Integer servicePort;

    @ApiModelProperty(value = "instance当前状态", name = "currentState")
    private Integer currentState;

    @ApiModelProperty(value = "instance注册的时间", name = "registerTime")
    private Date registerTime;

    @ApiModelProperty(value = "instance取消注册时间", name = "downTime")
    private Date downTime;

    @ApiModelProperty(value = "instance存活的总时长", name = "upDuration")
    private String upDuration;

    @ApiModelProperty(value = "instance上次宕机至今的时长", name = "downDuration")
    private String downDuration;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceInstanceDto that = (ServiceInstanceDto) o;
        return Objects.equal(instanceId, that.instanceId) &&
                Objects.equal(currentState, that.currentState);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instanceId, currentState);
    }
}
