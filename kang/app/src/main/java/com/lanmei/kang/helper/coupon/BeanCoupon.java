package com.lanmei.kang.helper.coupon;

public class BeanCoupon {

    /**
     * id : 2
     * addtime : 1545721149
     * uptime : 1545721149
     * lname : 水果10元优惠券
     * title: "全部水果商品满50.00元减10.00元"
     * consume : 50.00
     * money : 10.00
     * uid : 200
     * is_del : 0
     * starttime : 1541138400
     * endtime : 1541138400
     * state : 1
     * employ : 0
     * logid : 11
     * type : 1
     * name: null
     */

    private String id;
    private long addtime;
    private long uptime;
    private String lname;
    private String title;
    private Double consume;
    private Double money;
    private String uid;
    private int is_del;
    private long starttime;
    private long endtime;
    private int state;
    private int employ;
    private int logid;
    private String type;
    private String name;
    private boolean isChoose;//

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getConsume() {
        return consume;
    }

    public void setConsume(Double consume) {
        this.consume = consume;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getEmploy() {
        return employ;
    }

    public void setEmploy(int employ) {
        this.employ = employ;
    }

    public int getLogid() {
        return logid;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BeanCoupon{" +
                "id='" + id + '\'' +
                ", addtime=" + addtime +
                ", uptime=" + uptime +
                ", lname='" + lname + '\'' +
                ", title='" + title + '\'' +
                ", consume=" + consume +
                ", money=" + money +
                ", uid='" + uid + '\'' +
                ", is_del=" + is_del +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", state=" + state +
                ", employ=" + employ +
                ", logid=" + logid +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
