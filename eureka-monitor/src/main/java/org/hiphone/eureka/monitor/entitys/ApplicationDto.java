package org.hiphone.eureka.monitor.entitys;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author HiPhone
 */
@Getter
@Setter
@ToString
@ApiModel(value = "ApplicationDto", description = "应用统计信息的封装类")
public class ApplicationDto implements Serializable {

    private static final long serialVersionUID = 3977791388444387551L;

    @ApiModelProperty(value = "集群ID", name = "clusterId")
    private String clusterId;

    @ApiModelProperty(value = "应用的名称", name = "applicationName")
    private String applicationName;

    @ApiModelProperty(value = "应用状态为up的实例数目", name = "upInstanceNum")
    private Integer upInstanceNum;

    @ApiModelProperty(value = "应用的总实例数", name = "totalInstanceNum")
    private Integer totalInstanceNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationDto that = (ApplicationDto) o;
        return Objects.equal(clusterId, that.clusterId) &&
                Objects.equal(applicationName, that.applicationName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clusterId, applicationName);
    }
}
