package com.xwbing.entity;


import lombok.Data;

/**
 * 快递信息
 * Created by xiangwb on 2015/10/10.
 */
@Data
public class KuaiModel extends BaseEntity {
    private static final long serialVersionUID = -5532070374216595023L;
    //快递公司名称
    private String kuaidiName;
    //运单号
    private String kuaidiNo;
    //订单号
    private String orderNo;
}
