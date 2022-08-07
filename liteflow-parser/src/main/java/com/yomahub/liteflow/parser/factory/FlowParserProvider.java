package com.yomahub.liteflow.parser.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.FlowParser;
import com.yomahub.liteflow.exception.ErrorSupportPathException;
import com.yomahub.liteflow.parser.base.BaseFlowParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 解析器提供者
 * <p>
 *
 * @author junjun
 */
public class FlowParserProvider {

    private static final Logger log = LoggerFactory.getLogger(FlowParserProvider.class);

    private static final Set<BaseFlowParser> parsers = new CopyOnWriteArraySet<>();

    private static final String FORMAT_EL_XML_CONFIG_REGEX = "el_xml:.+";

    private static final String FORMAT_EL_JSON_CONFIG_REGEX = "el_json:.+";

    private static final String FORMAT_EL_YML_CONFIG_REGEX = "el_yml:.+";

    private static final String FORMAT_XML_CONFIG_REGEX = "xml:.+";

    private static final String FORMAT_JSON_CONFIG_REGEX = "json:.+";

    private static final String FORMAT_YML_CONFIG_REGEX = "yml:.+";

    public static void register(BaseFlowParser parser) {
        parsers.add(parser);
    }


    /**
     * 根据配置的地址找到对应的解析器
     *
     * @param path
     * @return
     */
    public static FlowParser lookup(String path) throws Exception {
        if (CollectionUtil.isEmpty(parsers)) {
            return null;
        }
        for (BaseFlowParser parser : parsers) {
            if (parser.acceptsURL(path)) {
                return parser;
            }
        }
        // not found
        String errorMsg = StrUtil.format("can't find the parser for path:{}", path);
        throw new ErrorSupportPathException(errorMsg);
    }
}
