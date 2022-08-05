package com.yomahub.liteflow.flow;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.exception.ComponentCannotRegisterException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NodeManager {

    private static final Logger log = LoggerFactory.getLogger(NodeManager.class);
    private final Map<String, Node> nodeMap = new CopyOnWriteHashMap<>();

    public Node getNode(String nodeId) {
        return nodeMap.get(nodeId);
    }

    //虽然实现了cloneable，但是还是浅copy，因为condNodeMap这个对象还是共用的。
    //那condNodeMap共用有关系么，原则上没有关系。但是从设计理念上，以后应该要分开
    //tag和condNodeMap这2个属性不属于全局概念，属于每个chain范围的属性
    public Node copyNode(String nodeId) {
        try {
            Node node = nodeMap.get(nodeId);
            return node.copy();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean containNode(String nodeId) {
        return nodeMap.containsKey(nodeId);
    }

    public void addNode(String nodeId, String name, NodeComponent nodeComponent, FlowConfiguration flowConfiguration) {
        try {
            //初始化Node
            Node node = new Node(nodeComponent, flowConfiguration);

            nodeMap.put(nodeId, node);
        } catch (Exception e) {
            String error = StrUtil.format("component[{}] register error", StrUtil.isEmpty(name) ? nodeId : StrUtil.format("{}({})", nodeId, name));
            log.error(error);
            throw new ComponentCannotRegisterException(error);
        }
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }
}
