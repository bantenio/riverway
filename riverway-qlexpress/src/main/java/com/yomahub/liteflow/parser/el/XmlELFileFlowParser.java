package com.yomahub.liteflow.parser.el;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.base.BaseXmlFlowParser;
import com.yomahub.liteflow.parser.base.ObjectResource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static com.yomahub.liteflow.common.ChainConstant.NAME;

/**
 * Xml形式的EL表达式解析抽象引擎
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class XmlELFileFlowParser extends BaseXmlFlowParser {
    private static final Logger log = LoggerFactory.getLogger(XmlELFileFlowParser.class);

    /**
     * 解析一个chain的过程
     */
    @Override
    public void parseOneChain(FlowConfiguration flowConfiguration, Element e, ObjectResource<Document> objectResource) {
        parseOneChainEl(e, flowConfiguration, objectResource);
    }


    public void parseOneChainEl(Element e, FlowConfiguration flowConfiguration, ObjectResource<Document> objectResource) {
        //构建chainBuilder
        String chainName = e.attributeValue(NAME);
        String text = e.getText();
        String el = RegexUtil.removeComments(text);
        LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder
                .createChain(flowConfiguration)
                .setChainName(chainName);
        chainELBuilder.setEL(el, objectResource).build();
    }

    private static class RegexUtil {
        // java 注释的正则表达式
        private static final String REGEX_COMMENT = "/\\*((?!\\*/).|[\\r\\n])*?\\*/|[ \\t]*//.*";

        /**
         * 移除 el 表达式中的注释，支持 java 的注释，包括单行注释、多行注释，
         * 会压缩字符串，移除空格和换行符
         *
         * @param elStr el 表达式
         * @return 移除注释后的 el 表达式
         */
        private static String removeComments(String elStr) {
            if (StrUtil.isBlank(elStr)) {
                return elStr;
            }

            String text = Pattern.compile(REGEX_COMMENT)
                    .matcher(elStr)
                    // 移除注释
                    .replaceAll(CharSequenceUtil.EMPTY)
                    // 移除字符串中的空格
                    .replaceAll(CharSequenceUtil.SPACE, CharSequenceUtil.EMPTY);

            // 移除所有换行符
            return StrUtil.removeAllLineBreaks(text);
        }
    }

}
