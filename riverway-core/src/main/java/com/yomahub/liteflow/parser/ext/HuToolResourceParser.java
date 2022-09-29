package com.yomahub.liteflow.parser.ext;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.exception.ConfigErrorException;
import com.yomahub.liteflow.parser.ResourceParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HuToolResourceParser implements ResourceParser {
    @Override
    public List<ParseResource> findResources(String path) {
        String locationPattern = path;
        try {
            List<URL> resources = ResourceUtil.getResources(locationPattern);
            if (CollectionUtil.isEmpty(resources)) {
                return ListUtil.empty();
            }
            List<ParseResource> contents = new ArrayList<>();
            for (URL resource : resources) {
                contents.add(new ParseResource()
                        .setResourcePath(resource.toURI())
                        .setResource(resource.getFile())
                        .setContent(IoUtil.read(resource.openStream(), CharsetUtil.CHARSET_UTF_8)));
            }
            return contents;
        } catch (Exception e) {
            throw new ConfigErrorException("config error,please check the path", e);
        }
    }
}
