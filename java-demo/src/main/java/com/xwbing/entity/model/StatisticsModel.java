package com.xwbing.entity.model;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.xwbing.entity.BaseEntity;

/**
 * 说明: 统计属性信息完善程度
 * 创建日期: 2017年3月23日 下午1:30:40
 * 作者: xiangwb
 */

public class StatisticsModel extends BaseEntity {
    public static final String table = "statistics_model";
    private static final long serialVersionUID = -2425511147708333541L;
    /**
     * 应用主键
     */
    private String applicationId;
    /**
     * 服务器节点主键
     */
    private String nodeId;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 域名
     */
    private String domain;
    /**
     * 中间件
     */
    private String middleSoftWareId;
    /**
     * 版本
     */
    private String version;
    /**
     * 信息完善程度数值 临时字段
     */
    @JSONField(serialize = false)
    private Integer perfectDegree;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMiddleSoftWareId() {
        return middleSoftWareId;
    }

    public void setMiddleSoftWareId(String middleSoftWareId) {
        this.middleSoftWareId = middleSoftWareId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPerfectDegree(Integer perfectDegree) {
        this.perfectDegree = perfectDegree;
    }

    public Integer getPerfectDegree() {
        int applicationIdPD = 20;
        int nodeIdPD = 10;
        int applicationPortPD = 20;
        int applicationDomainPD = 15;
        int middleSoftWarePD = 15;
        int versionPD = 10;
        int sum = 0;
        sum = sum + (StringUtils.isEmpty(this.getApplicationId()) ? 0: applicationIdPD);
        sum = sum + (StringUtils.isEmpty(this.getNodeId()) ? 0 : nodeIdPD);
        sum = sum + (this.getPort() == null ? 0 : applicationPortPD);
        sum = sum + (StringUtils.isEmpty(this.getDomain()) ? 0: applicationDomainPD);
        sum = sum + (StringUtils.isEmpty(this.getMiddleSoftWareId()) ? 0: middleSoftWarePD);
        sum = sum + (StringUtils.isEmpty(this.getVersion()) ? 0 : versionPD);
        return sum;
    }

    public static void main(String[] args) {
        StatisticsModel model = new StatisticsModel();
        model.setApplicationId("11");
        model.setDomain("22");
        System.out.println(model.getPerfectDegree());
    }
}
