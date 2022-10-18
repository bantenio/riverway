package com.yomahub.liteflow.parser.dsl.define

import com.yomahub.liteflow.flow.element.Chain

class PathChain extends Chain<PathChain> {
    private String chainPath
    private boolean inited = false

    String getChainPath() {
        return chainPath
    }

    PathChain setChainPath(String chainPath) {
        this.chainPath = chainPath
        return this
    }
    PathChain chainName(String chainName) {
        this.setChainName(chainName)
        return this
    }

    boolean isInited() {
        return inited;
    }

    PathChain setInited(boolean inited) {
        this.inited = inited
    }
}
