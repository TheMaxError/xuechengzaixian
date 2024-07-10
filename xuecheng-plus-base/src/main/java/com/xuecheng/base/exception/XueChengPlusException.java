package com.xuecheng.base.exception;
//自定义异常数据类型
public class XueChengPlusException extends RuntimeException {
    private String errMessage;

    public XueChengPlusException(CommonError unkownError) {
    }

    public XueChengPlusException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }
    public String getErrMessage() {
        return errMessage;
    }

    private static void cast(String errMessage) {
        throw new XueChengPlusException(errMessage);
    }
    private static void cast(CommonError errMessage) {
        throw new XueChengPlusException(errMessage.getErrMessage());
    }


}
