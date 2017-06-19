package com.xwbing.entity;


/**
 * 快递信息
 * Created by ff on 2015/10/10.
 */
public class KuaiModel extends BaseEntity {
    private static final long serialVersionUID = -5532070374216595023L;
    //快递公司名称
    private String kuaidiName;
    //运单号
    private String kuaidiNo;
    //订单号
    private String orderNo;
    public String getKuaidiName() {
        return kuaidiName;
    }

    public void setKuaidiName(String kuaidiName) {
        this.kuaidiName = kuaidiName;
    }

    public String getKuaidiNo() {
        return kuaidiNo;
    }

    public void setKuaidiNo(String kuaidiNo) {
        this.kuaidiNo = kuaidiNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

}
