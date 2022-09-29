package com.yomahub.liteflow.builder;

import java.net.URI;

public class ParseResource {
    private String content;
    private String resource;

    private URI resourcePath;

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

    public URI getResourcePath() {
        return resourcePath;
    }

    public ParseResource setResourcePath(URI resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }
}