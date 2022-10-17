package com.yomahub.liteflow.enums;

/**
 * 节点类型枚举
 * @author Bryan.Zhang
 * @since 2.6.0
 */
public enum NodeTypeEnum {
    COMMON("common","普通"),

    SWITCH("switch", "条件"),

    FOR("for", "次数循环"),
    WHILE("while", "条件循环"),
    BREAK("break", "跳出循环"),

    SCRIPT("script","脚本"),
    SWITCH_SCRIPT("switch_script","条件脚本")
    ;
    private String code;
    private String name;

    NodeTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static NodeTypeEnum getEnumByCode(String code) {
        for (NodeTypeEnum e : NodeTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
