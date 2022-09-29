package com.yomahub.liteflow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * logo打印器
 *
 * @author Bryan.Zhang
 */
public class LOGOPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(LOGOPrinter.class);
    /**
     * LiteFlow 当前版本号
     */
    private static final String VERSION_NO = "v2.8.3";

    public static void print() {
        LOG.info(getLogString());
    }

    public static String getLogString() {
        StringBuilder str = new StringBuilder("\n");
        str.append("================================================================================================\n");
        str.append("		 _     ___ _____ _____      _____ _     _____        __\n");
        str.append("		| |   |_ _|_   _| ____|    |  ___| |   / _ \\ \\      / /\n");
        str.append("		| |    | |  | | |  _| _____| |_  | |  | | | \\ \\ /\\ / / \n");
        str.append("		| |___ | |  | | | |__|_____|  _| | |__| |_| |\\ V  V /  \n");
        str.append("		|_____|___| |_| |_____|    |_|   |_____\\___/  \\_/\\_/   \n\n");
        str.append("		Version: " + VERSION_NO + "\n");
        str.append("		轻量且强大的规则引擎框架。\n");
        str.append("		Small but powerful rules engine.\n");
        str.append("================================================================================================\n");
        return str.toString();
    }
}
