package com.yomahub.liteflow.parser;

import com.yomahub.liteflow.builder.ParseResource;

import java.util.List;

public interface ResourceParser {
    List<ParseResource> findResources(String path);
}
