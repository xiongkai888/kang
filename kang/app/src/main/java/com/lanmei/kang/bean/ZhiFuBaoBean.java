package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/7/6.
 * 支付宝充值请求返回的数据
 */

public class ZhiFuBaoBean {

    /**
     * order_no : r_1707060535066
     * amount : 100
     * prepay_id :
     * query :
     * nonceStr :
     */

    private String order_no;
    private String amount;
    private String prepay_id;
    private String query;
    private String nonceStr;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
}
