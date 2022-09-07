package com.yomahub.liteflow.builder;

public class ParseResource {
    private String content;
    private String resource;

    private String resourcePath;

    public String getContent() {
        return content;
    }

    public ParseResource setContent(String content) {
        this.content = content;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public ParseResource setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public ParseResource setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }
}