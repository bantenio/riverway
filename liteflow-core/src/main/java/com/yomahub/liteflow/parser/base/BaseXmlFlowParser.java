package com.yomahub.liteflow.parser.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.helper.ParserHelper;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


/**
 * 基类，用于存放 XmlFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseXmlFlowParser implements FlowParser {
	private final Set<String> CHAIN_NAME_SET = new HashSet<>();

	public void parse(String content, LiteflowConfig liteflowConfig) throws Exception {
		parse(ListUtil.toList(content), liteflowConfig);
	}

	@Override
	public void parse(List<String> contentList, LiteflowConfig liteflowConfig) throws Exception {
		if (CollectionUtil.isEmpty(contentList)) {
			return;
		}
		List<Document> documentList = ListUtil.toList();
		for (String content : contentList) {
			Document document = DocumentHelper.parseText(content);
			documentList.add(document);
		}

		ParserHelper.parseDocument(documentList, CHAIN_NAME_SET, this::parseOneChain, liteflowConfig);
	}

	/**
	 * 解析一个 chain 的过程
	 *
	 * @param chain chain
	 */
	public abstract void parseOneChain(FlowConfiguration flowConfiguration, Element chain);

}
