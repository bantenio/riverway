package com.yomahub.liteflow.parser;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.exception.ConfigErrorException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.ArrayList;
import java.util.List;

public class SpringResourceParser implements ResourceParser {
    private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    public List<ParseResource> findResources(String path) {
        String locationPattern = path;
        try {
            Resource[] resources = resolver.getResources(locationPattern);
            if (ArrayUtil.isEmpty(resources)) {
                return ListUtil.empty();
            }
            List<ParseResource> contents = new ArrayList<>();
            for (Resource resource : resources) {
                contents.add(new ParseResource()
                        .setResourcePath(resource.getURL().toURI())
                        .setResource(resource.getFilename())
                        .setContent(IoUtil.read(resource.getInputStream(), CharsetUtil.CHARSET_UTF_8)));
            }
            return contents;
        } catch (Exception e) {
            throw new ConfigErrorException("config error,please check the path", e);
        }
    }
}
