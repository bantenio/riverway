package com.yomahub.liteflow.builder;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.exception.NoSuchFlowParserException;
import com.yomahub.liteflow.exception.NotSupportPathException;
import com.yomahub.liteflow.flow.FlowConfiguration;
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

    private static final Set<UrlFlowParser> parsers = new CopyOnWriteArraySet<>();

    public static void register(UrlFlowParser parser) {
        parsers.add(parser);
    }


    /**
     * 根据配置的地址找到对应的解析器
     *
     * @param path
     * @return
     */
    public static UrlFlowParser lookup(String path) throws LiteFlowParseException {
        if (CollectionUtil.isEmpty(parsers)) {
            throw new NoSuchFlowParserException();
        }
        for (UrlFlowParser parser : parsers) {
            if (parser.acceptsURL(path)) {
                return parser;
            }
        }
        // not found
        String errorMsg = StrUtil.format("can't find the parser for path:{}", path);
        throw new NotSupportPathException(errorMsg);
    }

    public static void parse(String path, FlowConfiguration flowConfiguration) {
        lookup(path).parseMain(path, flowConfiguration.getLiteflowConfig(), flowConfiguration);
    }
}
