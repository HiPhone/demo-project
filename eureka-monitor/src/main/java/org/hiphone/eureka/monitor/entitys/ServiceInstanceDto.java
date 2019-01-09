package org.hiphone.eureka.monitor.entitys;

import com.google.common.base.Objects;
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
public class ServiceInstanceDto implements Serializable {

    private static final long serialVersionUID = 3353406941094995780L;

    private String instanceId;
    private String clusterId;
    private String applicationName;
    private String hostName;
    private String ipAddress;
    private String servicePort;
    private Integer currentState;
    private Date registerTime;
    private Date lastRegisterTime;
    private String upDuration;
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
