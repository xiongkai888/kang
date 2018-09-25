package com.xson.common.bean;

/**
 * @author xson
 *         带状态和描述的bean
 *         {
 *         "status":1,
 *         "status":"成功"
 *         }
 */
public class BaseBean {


    /**
     * status : 0
     * info : 用户已禁用
     * errorCode : 1000
     */

    private int status;
    private String info;
    private int errorCode;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
