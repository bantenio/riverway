package com.yomahub.liteflow.flow;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ChainManager {
    private static final Logger log = LoggerFactory.getLogger(ChainManager.class);

    private final Map<String, Chain> chainMap = new CopyOnWriteHashMap<>();

    public Chain getChain(String id) {
        return chainMap.get(id);
    }

    //这一方法主要用于第一阶段chain的预装载
    public void addChain(String chainName) {
        if (!chainMap.containsKey(chainName)) {
            chainMap.put(chainName, new Chain(chainName));
        }
    }

    //这个方法主要用于第二阶段的替换chain
    public void addChain(Chain chain) {
        chainMap.put(chain.getChainName(), chain);
    }

    public boolean containChain(String chainId) {
        return chainMap.containsKey(chainId);
    }

    public boolean removeChain(String chainId) {
        if (containChain(chainId)) {
            chainMap.remove(chainId);
            return true;
        } else {
            String errMsg = StrUtil.format("cannot find the chain[{}]", chainId);
            log.error(errMsg);
            return false;
        }
    }

    public Map<String, Chain> getChainMap() {
        return chainMap;
    }
}
