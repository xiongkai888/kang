package com.lanmei.kang.bean;

/**
 * Created by xkai on 2019/1/17.
 */

public class SiteinfoBean {

    /**
     *  : false
     * android_update : {"version":"49","description":"一些BUG修复\r\n2019-12-16 18.00","url":"http://qkmimages.oss-cn-shenzhen.aliyuncs.com/appdown/kang_vc49_vn2.1.69_201901161854.apk","share_qr":false}
     * base : {"withdrawal_fee":"1","withdrawal_fee_max":"50","withdrawal_min":"100","holidays":"05-01,10-01","cancel_order_time":"200","cancel_order_time1":"240","platform_service":"205","commission1":"10","commission2":"5","phone":"13800138000","share_url":"https://itunes.apple.com/us/app/%E8%B5%9B%E8%89%87%E8%BE%BE%E4%BA%BA/id1255360897","share_qr":false}
     * counter : {"time":"1409815348","count_num":"1000000000","sec_num":"7"}
     * ct_price : {"time":"1410401950","price":"10","num":"2"}
     * device_update : {"version":"1.0.0","description":"第一次升级","url":"http://wlyg.oss-cn-shenzhen.aliyuncs.com/device/user2.1024.new.2.bin,http://wlyg.oss-cn-shenzhen.aliyuncs.com/device/user1.1024.new.2.bin","force":"1"}
     * free_gaia : {"day_num":"10","user_day_num":"1"}
     * iphone_update : {"version":"1.0.0","description":" 发现新版本，是否更新","url":"https://appsto.re/cn/bM00kb.i","share_qr":false}
     * other : {"free_shipping":"500"}
     * paypal_poundage : {"poundage":"5","percent":"0.05","poundage2":"5","percent2":"0.02","fee_status":"1"}
     * trade_poundage : {"max_val":"100","poundage":"5","percent":"0.02","status":"1","fee_status":"1"}
     */

    private AndroidUpdateBean android_update;
    private BaseBean base;
    private CounterBean counter;
    private CtPriceBean ct_price;
    private DeviceUpdateBean device_update;
    private FreeGaiaBean free_gaia;
    private IphoneUpdateBean iphone_update;
    private OtherBean other;
    private PaypalPoundageBean paypal_poundage;
    private TradePoundageBean trade_poundage;

    public AndroidUpdateBean getAndroid_update() {
        return android_update;
    }

    public void setAndroid_update(AndroidUpdateBean android_update) {
        this.android_update = android_update;
    }

    public BaseBean getBase() {
        return base;
    }

    public void setBase(BaseBean base) {
        this.base = base;
    }

    public CounterBean getCounter() {
        return counter;
    }

    public void setCounter(CounterBean counter) {
        this.counter = counter;
    }

    public CtPriceBean getCt_price() {
        return ct_price;
    }

    public void setCt_price(CtPriceBean ct_price) {
        this.ct_price = ct_price;
    }

    public DeviceUpdateBean getDevice_update() {
        return device_update;
    }

    public void setDevice_update(DeviceUpdateBean device_update) {
        this.device_update = device_update;
    }

    public FreeGaiaBean getFree_gaia() {
        return free_gaia;
    }

    public void setFree_gaia(FreeGaiaBean free_gaia) {
        this.free_gaia = free_gaia;
    }

    public IphoneUpdateBean getIphone_update() {
        return iphone_update;
    }

    public void setIphone_update(IphoneUpdateBean iphone_update) {
        this.iphone_update = iphone_update;
    }

    public OtherBean getOther() {
        return other;
    }

    public void setOther(OtherBean other) {
        this.other = other;
    }

    public PaypalPoundageBean getPaypal_poundage() {
        return paypal_poundage;
    }

    public void setPaypal_poundage(PaypalPoundageBean paypal_poundage) {
        this.paypal_poundage = paypal_poundage;
    }

    public TradePoundageBean getTrade_poundage() {
        return trade_poundage;
    }

    public void setTrade_poundage(TradePoundageBean trade_poundage) {
        this.trade_poundage = trade_poundage;
    }

    public static class AndroidUpdateBean {
        /**
         * version : 49
         * description : 一些BUG修复
         2019-12-16 18.00
         * url : http://qkmimages.oss-cn-shenzhen.aliyuncs.com/appdown/kang_vc49_vn2.1.69_201901161854.apk
         * share_qr : false
         */

        private String version;
        private String description;
        private String url;
        private boolean share_qr;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isShare_qr() {
            return share_qr;
        }

        public void setShare_qr(boolean share_qr) {
            this.share_qr = share_qr;
        }
    }

    public static class BaseBean {
        /**
         * withdrawal_fee : 1
         * withdrawal_fee_max : 50
         * withdrawal_min : 100
         * holidays : 05-01,10-01
         * cancel_order_time : 200
         * cancel_order_time1 : 240
         * platform_service : 205
         * commission1 : 10
         * commission2 : 5
         * phone : 13800138000
         * share_url : https://itunes.apple.com/us/app/%E8%B5%9B%E8%89%87%E8%BE%BE%E4%BA%BA/id1255360897
         * share_qr : false
         */

        private String withdrawal_fee;
        private String withdrawal_fee_max;
        private String withdrawal_min;
        private String holidays;
        private String cancel_order_time;
        private String cancel_order_time1;
        private String platform_service;
        private String commission1;
        private String commission2;
        private String phone;
        private String share_url;
        private boolean share_qr;

        public String getWithdrawal_fee() {
            return withdrawal_fee;
        }

        public void setWithdrawal_fee(String withdrawal_fee) {
            this.withdrawal_fee = withdrawal_fee;
        }

        public String getWithdrawal_fee_max() {
            return withdrawal_fee_max;
        }

        public void setWithdrawal_fee_max(String withdrawal_fee_max) {
            this.withdrawal_fee_max = withdrawal_fee_max;
        }

        public String getWithdrawal_min() {
            return withdrawal_min;
        }

        public void setWithdrawal_min(String withdrawal_min) {
            this.withdrawal_min = withdrawal_min;
        }

        public String getHolidays() {
            return holidays;
        }

        public void setHolidays(String holidays) {
            this.holidays = holidays;
        }

        public String getCancel_order_time() {
            return cancel_order_time;
        }

        public void setCancel_order_time(String cancel_order_time) {
            this.cancel_order_time = cancel_order_time;
        }

        public String getCancel_order_time1() {
            return cancel_order_time1;
        }

        public void setCancel_order_time1(String cancel_order_time1) {
            this.cancel_order_time1 = cancel_order_time1;
        }

        public String getPlatform_service() {
            return platform_service;
        }

        public void setPlatform_service(String platform_service) {
            this.platform_service = platform_service;
        }

        public String getCommission1() {
            return commission1;
        }

        public void setCommission1(String commission1) {
            this.commission1 = commission1;
        }

        public String getCommission2() {
            return commission2;
        }

        public void setCommission2(String commission2) {
            this.commission2 = commission2;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public boolean isShare_qr() {
            return share_qr;
        }

        public void setShare_qr(boolean share_qr) {
            this.share_qr = share_qr;
        }
    }

    public static class CounterBean {
        /**
         * time : 1409815348
         * count_num : 1000000000
         * sec_num : 7
         */

        private String time;
        private String count_num;
        private String sec_num;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCount_num() {
            return count_num;
        }

        public void setCount_num(String count_num) {
            this.count_num = count_num;
        }

        public String getSec_num() {
            return sec_num;
        }

        public void setSec_num(String sec_num) {
            this.sec_num = sec_num;
        }
    }

    public static class CtPriceBean {
        /**
         * time : 1410401950
         * price : 10
         * num : 2
         */

        private String time;
        private String price;
        private String num;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class DeviceUpdateBean {
        /**
         * version : 1.0.0
         * description : 第一次升级
         * url : http://wlyg.oss-cn-shenzhen.aliyuncs.com/device/user2.1024.new.2.bin,http://wlyg.oss-cn-shenzhen.aliyuncs.com/device/user1.1024.new.2.bin
         * force : 1
         */

        private String version;
        private String description;
        private String url;
        private String force;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getForce() {
            return force;
        }

        public void setForce(String force) {
            this.force = force;
        }
    }

    public static class FreeGaiaBean {
        /**
         * day_num : 10
         * user_day_num : 1
         */

        private String day_num;
        private String user_day_num;

        public String getDay_num() {
            return day_num;
        }

        public void setDay_num(String day_num) {
            this.day_num = day_num;
        }

        public String getUser_day_num() {
            return user_day_num;
        }

        public void setUser_day_num(String user_day_num) {
            this.user_day_num = user_day_num;
        }
    }

    public static class IphoneUpdateBean {
        /**
         * version : 1.0.0
         * description :  发现新版本，是否更新
         * url : https://appsto.re/cn/bM00kb.i
         * share_qr : false
         */

        private String version;
        private String description;
        private String url;
        private boolean share_qr;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isShare_qr() {
            return share_qr;
        }

        public void setShare_qr(boolean share_qr) {
            this.share_qr = share_qr;
        }
    }

    public static class OtherBean {
        /**
         * free_shipping : 500
         */

        private String free_shipping;

        public String getFree_shipping() {
            return free_shipping;
        }

        public void setFree_shipping(String free_shipping) {
            this.free_shipping = free_shipping;
        }
    }

    public static class PaypalPoundageBean {
        /**
         * poundage : 5
         * percent : 0.05
         * poundage2 : 5
         * percent2 : 0.02
         * fee_status : 1
         */

        private String poundage;
        private String percent;
        private String poundage2;
        private String percent2;
        private String fee_status;

        public String getPoundage() {
            return poundage;
        }

        public void setPoundage(String poundage) {
            this.poundage = poundage;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPoundage2() {
            return poundage2;
        }

        public void setPoundage2(String poundage2) {
            this.poundage2 = poundage2;
        }

        public String getPercent2() {
            return percent2;
        }

        public void setPercent2(String percent2) {
            this.percent2 = percent2;
        }

        public String getFee_status() {
            return fee_status;
        }

        public void setFee_status(String fee_status) {
            this.fee_status = fee_status;
        }
    }

    public static class TradePoundageBean {
        /**
         * max_val : 100
         * poundage : 5
         * percent : 0.02
         * status : 1
         * fee_status : 1
         */

        private String max_val;
        private String poundage;
        private String percent;
        private String status;
        private String fee_status;

        public String getMax_val() {
            return max_val;
        }

        public void setMax_val(String max_val) {
            this.max_val = max_val;
        }

        public String getPoundage() {
            return poundage;
        }

        public void setPoundage(String poundage) {
            this.poundage = poundage;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFee_status() {
            return fee_status;
        }

        public void setFee_status(String fee_status) {
            this.fee_status = fee_status;
        }
    }
}
