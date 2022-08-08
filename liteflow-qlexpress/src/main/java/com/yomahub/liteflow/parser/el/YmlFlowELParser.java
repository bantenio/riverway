package com.yomahub.liteflow.parser.el;

import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.flow.FlowConfiguration;

/**
 * yml形式的EL表达式解析抽象引擎
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public abstract class YmlFlowELParser extends JsonFlowELParser {

	private static final String URL_PREFIX = "el_yaml://";

	/**
	 * 解析一个chain的过程
	 */
	@Override
	public void parseOneChain(FlowConfiguration flowConfiguration, JSONObject chainObject) {
		parseOneChainEl(chainObject, flowConfiguration);
	}

	@Override
	public boolean acceptsURL(String url) throws LiteFlowParseException {
		return false;
	}

}
