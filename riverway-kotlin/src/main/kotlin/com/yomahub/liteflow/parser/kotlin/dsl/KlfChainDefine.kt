package com.yomahub.liteflow.parser.kotlin.dsl

import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.prop.NodePropBean
import com.yomahub.liteflow.components.RefValueHandler
import com.yomahub.liteflow.components.ThrowComponent
import com.yomahub.liteflow.components.ValueHandler
import com.yomahub.liteflow.components.ext.LogNodeAround
import com.yomahub.liteflow.components.ext.StringFormatNodeAround
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.core.NodeSwitchComponent
import com.yomahub.liteflow.enums.NodeTypeEnum
import com.yomahub.liteflow.flow.ChainManager
import com.yomahub.liteflow.flow.NodeManager
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.*
import org.slf4j.event.Level

@Throws(Throwable::class)
fun THEN(vararg executables: Executable?): ThenCondition {
    if (executables.isEmpty()) {
        throw LiteFlowParseException("THEN executable list is empty.");
    }
    var opIdx = -1
    val condition = ThenCondition()
    for (executable in executables) {
        opIdx++
        if (executable == null) {
            val msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx)
            throw Exception(msg)
        }
        condition.addExecutable(executable)
    }
    return condition
}

@Throws(Throwable::class)
fun WHEN(vararg executables: Executable?): WhenCondition? {
    if (executables.size == 0) {
        throw LiteFlowParseException("WHEN executable list is empty.")
    }
    var opIdx = -1
    val condition = WhenCondition()
    for (executable in executables) {
        opIdx++
        if (executable == null) {
            val msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx)
            throw LiteFlowParseException(msg)
        }
        condition.addExecutable(executable)
    }
    return condition
}

@Throws(Throwable::class)
fun SWITCH(node: Node?): SwitchCondition {
    if (node == null) {
        throw LiteFlowParseException("SWITCH parameter is null")
    }
    val switchCondition = SwitchCondition()
    switchCondition.setSwitchNode(node)
    return switchCondition
}

@Throws(Throwable::class)
fun SWITCH(node: NodeCondition?): SwitchCondition {
    if (node == null) {
        throw LiteFlowParseException("SWITCH parameter is null")
    }
    val switchCondition = SwitchCondition()
    switchCondition.setSwitchNode(node)
    return switchCondition
}

@Throws(Throwable::class)
fun PRE(vararg executables: Executable?): PreCondition? {
    if (executables.size == 0) {
        throw LiteFlowParseException("PRE executable list is empty.")
    }
    var opIdx = -1
    val condition = PreCondition()
    for (executable in executables) {
        opIdx++
        if (executable == null) {
            val msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx)
            throw LiteFlowParseException(msg)
        }
        condition.addExecutable(executable)
    }
    return condition
}

@Throws(Throwable::class)
fun FINALLY(vararg executables: Executable?): FinallyCondition? {
    if (executables.size == 0) {
        throw LiteFlowParseException("FINALLY executable list is empty.")
    }
    var opIdx = -1
    val condition = FinallyCondition()
    for (executable in executables) {
        opIdx++
        if (executable == null) {
            val msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx)
            throw LiteFlowParseException(msg)
        }
        condition.addExecutable(executable)
    }
    return condition
}

val chainManager = ChainManager()

fun chain(chainName: String, action: () -> Condition?): Chain {
    val condition = action()
    val chain = Chain(chainName)
    if (condition != null) {
        val preConditionList = ArrayList<Condition>()
        val finallyConditionList = ArrayList<Condition>()
        for (executable in condition.executableList) {
            if (executable is PreCondition) {
                preConditionList.add(executable)
            } else if (executable is FinallyCondition) {
                finallyConditionList.add(executable)
            }
        }
        val conditionList = ArrayList<Condition>()
        conditionList.add(condition)
        chain.conditionList = conditionList
        chain.preConditionList = preConditionList
        chain.finallyConditionList = finallyConditionList
    }
    chainManager.addChain(chain)
    return chain
}

val SWITCH_COMPONENT_CLASS = NodeSwitchComponent::class.java

val nodeManager = NodeManager()

inline fun <reified T : NodeComponent> node(): NodeCondition {
    val clazzName = T::class.simpleName
    val beanName = StrUtil.lowerFirst(clazzName)
    return node<T>(beanName)
}

inline fun <reified T : NodeComponent> node(id: String): NodeCondition {
    val clazz = T::class.java
    val clazzName = T::class.simpleName
    val beanName = StrUtil.lowerFirst(clazzName)
    val nodePropBean = NodePropBean()
        .setClazz(T::class.simpleName)
        .setId(beanName)
    val type = if (clazz.isAssignableFrom(SWITCH_COMPONENT_CLASS)) NodeTypeEnum.SWITCH else NodeTypeEnum.COMMON
    if (nodePropBean != null) {
        val node = Node(id, beanName, type, clazzName)
        nodeManager.addNode(id, beanName, type, clazzName)
        return NodeCondition(node)
    }
    throw LiteFlowParseException("Not create the component info from class ${clazz.name}")
}

fun ref(expression: String, type: String? = null): RefValueHandler {
    if (StrUtil.isBlank(expression)) {
        throw LiteFlowParseException("ref func message must be not null or empty")
    }
    return RefValueHandler(expression, type)
}

fun Throw(throwable: Throwable?, name: String): NodeCondition {
    if (throwable == null) {
        throw LiteFlowParseException("Throw func throwable must be not null")
    }
    val node = Node(ThrowComponent(throwable).setName(name))
    return NodeCondition(node)
}


fun Throw(valueHandler: ValueHandler?, name: String): NodeCondition {
    if (valueHandler == null) {
        throw LiteFlowParseException("Throw func valueHandler must be not null")
    }
    val node = Node(ThrowComponent(valueHandler).setName(name))
    return NodeCondition(node)
}

fun Condition.id(id: String): Condition {
    return this.setId(id)
}

fun ChainProxy.id(id: String): ChainProxy {
    return this.setId(id)
}

fun Chain.id(id: String): ChainProxy {
    return ChainProxy(this).setId(id)
}

fun map(): MutableMap<String, Any> {
    return mutableMapOf()
}

fun MutableMap<String, Any>.add(key: String, value: Any): MutableMap<String, Any> {
    this[key] = value
    return this
}

fun NodeCondition.property(key: String, value: Any): NodeCondition {
    this.addProperty(key, value)
    return this
}

fun NodeCondition.swap(key: String, value: Any): NodeCondition {
    this.addSwapHandler(key, value)
    return this
}

fun NodeCondition.log(level: Level, message: String, vararg args: Any): NodeCondition {
    this.addNodeAroundCondition(
        LogNodeAround(
            message,
            args,
            level
        )
    )
    return this
}

fun NodeCondition.strFmt(parameterName: String, message: String, vararg args: Any): NodeCondition {
    this.addNodeAroundCondition(StringFormatNodeAround(message, args, parameterName))
    return this
}

fun Condition.ignoreError(v: Boolean): Condition {
    this.isErrorResume = v
    return this
}

fun WhenCondition.threadPool(poolName: String): WhenCondition {
    this.threadExecutorName = poolName
    return this
}

fun SwitchCondition.to(vararg executables: Executable?): SwitchCondition {
    for (executable in executables) {
        if (executable != null) {
            this.addTargetItem(executable)
        }
    }
    return this
}