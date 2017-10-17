package com.xwbing.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明: 公共枚举
 * 创建日期: 2016年12月9日 上午9:50:30
 * 作者: xiangwb
 */
public class CommonEnum {
    /**
     * 服务器告警类型
     */
    public enum ServerAlarmType {
        CPU("CPU", "cpu"), MEM("内存", "mem"), DISK("硬盘", "disk"), BandWidth(
                "带宽", "bandWidth");
        // 成员变量
        private String code;
        private String name;

        ServerAlarmType(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 宽带上行下行类别
     */
    public enum ServerNicType {
        UP("带宽上行值", "up"), DOWN("带宽下行值", "down");
        private String code;
        private String name;

        ServerNicType(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum YesOrNo {
        YES("是", "Y"), NO("否", "N");
        // 成员变量
        private String code;
        private String name;

        YesOrNo(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum MenuOrButton {
        MENU("菜单", 1), BUTTON("按钮", 2);
        // 成员变量
        private int code;
        private String name;

        MenuOrButton(String name, int code) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 数据源备份配置 类型
     */
    public enum DataBackupSettingsType {
        ADD("增量备份", "add"), QUAN("全量备份", "quan");
        // 成员变量
        private String code;
        private String name;

        DataBackupSettingsType(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 数据源备份配置 间隔单位
     */
    public enum DataBackupSettingsIntervalUnit {
        HOUR("时", "h"), MINUTE("分", "q"), SECOND("秒", "s");
        // 成员变量
        private String code;
        private String name;

        DataBackupSettingsIntervalUnit(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 登录登出标记
     */
    public enum LoginInOut {
        IN("登录", 1), OUT("登出", 2);
        // 成员变量
        private int value;
        private String name;

        LoginInOut(String name, int value) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum ValidateResultEnum {
        SUCCESS("校验通过", 1), FAILED("校验失败", 0);
        private String msg;
        private int errorCode;

        ValidateResultEnum(String msg, int errorCode) {
            this.msg = msg;
            this.errorCode = errorCode;
        }

        public String getMsg() {
            return this.msg;
        }

        public int getErrorCode() {
            return this.errorCode;
        }
    }
    public static void main(String[] args) {
        String code = "Y";
//        for (YesOrNo yesOrNo : YesOrNo.values()) {
//            if (yesOrNo.getCode().equals(code)) {
//                System.out.println(yesOrNo.getName());
//                break;
//            }
//        }
        YesOrNo yesOrNo = Arrays.stream(YesOrNo.values()).filter(obj -> obj.getCode().equals(code)).findFirst().get();
        System.out.println(yesOrNo.getName());


        List<JSONObject> resultVos = new ArrayList<>();
        JSONObject jsonObject;
        for (CommonEnum.YesOrNo status : CommonEnum.YesOrNo.values()) {
            jsonObject = new JSONObject();
            jsonObject.put("code", status.getCode());
            jsonObject.put("name", status.getName());
            resultVos.add(jsonObject);
        }
//        return resultVos;
    }
}
