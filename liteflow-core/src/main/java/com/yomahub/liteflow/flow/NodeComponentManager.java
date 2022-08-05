package com.yomahub.liteflow.flow;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.exception.ComponentCannotRegisterException;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NodeComponentManager {

    private static final Logger log = LoggerFactory.getLogger(NodeComponentManager.class);
    private final Map<String, NodeComponent> nodeComponentMap= new CopyOnWriteHashMap<>();

    public NodeComponent getNodeComponent(String nodeId) {
        return nodeComponentMap.get(nodeId);
    }

    public void addNodeComponent(String nodeId, NodeComponent nodeComponent) {
        try {
            nodeComponentMap.put(nodeId, nodeComponent);
        } catch (Exception e) {
            String error = StrUtil.format("component[{}] register error", StrUtil.isEmpty(nodeId) ? nodeId : StrUtil.format("{}", nodeId));
            log.error(error);
            throw new ComponentCannotRegisterException(error);
        }
    }
}
