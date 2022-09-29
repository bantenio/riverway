package com.yomahub.liteflow.parser.dsl

abstract class MainDslScript extends Script {

    Set<String> chainPaths = new LinkedHashSet<>(10)

    void chain(String chainPath) {
        chainPaths.add(chainPath)
    }
}
