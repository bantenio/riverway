package com.yomahub.liteflow.parser.el;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.base.BaseXmlFlowParser;
import com.yomahub.liteflow.builder.UrlFlowParser;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.yomahub.liteflow.common.ChainConstant.NAME;

/**
 * Xml形式的EL表达式解析抽象引擎
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class XmlFlowELParser extends BaseXmlFlowParser implements UrlFlowParser {

	private static final String URL_PREFIX = "el:xml://";
	/**
	 * 解析一个chain的过程
	 */
	@Override
	public void parseOneChain(FlowConfiguration flowConfiguration, Element e) {
		parseOneChainEl(e, flowConfiguration);
	}


	public void parseOneChainEl(Element e, FlowConfiguration flowConfiguration) {
		//构建chainBuilder
		String chainName = e.attributeValue(NAME);
		String text = e.getText();
		String el = RegexUtil.removeComments(text);
		LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder.createChain(flowConfiguration).setChainName(chainName);
		chainELBuilder.setEL(el).build();
	}


	@Override
	public void parseMain(List<String> pathList,
						  LiteFlowConfig liteflowConfig,
						  FlowConfiguration flowConfiguration) throws LiteFlowParseException {
		List<String> contents = new LinkedList<>();
		for (String path : pathList) {
			List<String> pathToContents = pathToContent(path);
			if (pathToContents == null) {
				throw new LiteFlowParseException("the path" + path + " not read any content");
			}
			contents.addAll(pathToContents);
		}
		parse(contents, liteflowConfig, flowConfiguration);
	}

	protected List<String> pathToContent(String path) {
		if (!acceptsURL(path)) {
			throw new LiteFlowParseException("the path" + path + " was not support " + URL_PREFIX + " schema");
		}
		try {
			return Arrays.asList(FileUtils.readFileToString(new File(path.substring(URL_PREFIX.length())), StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new LiteFlowParseException(e);
		}
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

	@Override
	public boolean acceptsURL(String url) throws LiteFlowParseException {
		return false;
	}

}
