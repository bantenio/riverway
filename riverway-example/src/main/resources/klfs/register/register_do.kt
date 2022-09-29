package klfs.register

import com.yomahub.liteflow.example.components.ChannelSwitchComponent
import com.yomahub.liteflow.example.components.PreProcessNodeComponent
import com.yomahub.liteflow.example.components.SendVerifyCodeSmsRequestComponent
import com.yomahub.liteflow.example.result.ErrorCode
import com.yomahub.liteflow.parser.kotlin.dsl.*
import klfs.w_BizErr
import org.slf4j.event.Level


val RegisterDo = chain("register_do") {
    THEN(
        node<PreProcessNodeComponent>(),
        apply("@loanService", "loan", "#requestBody.channel"),
        SWITCH(
            node<ChannelSwitchComponent>()
                .swap("channel", ref("#requestBody.channel"))
                .property("channel.logic.mapping", map().add("2", "len4").add("21", "len6"))
        ).to(
            node<SendVerifyCodeSmsRequestComponent>()
                .swap("mobile", ref("#requestBody.zuid"))
                .property("sms.len", 4)
                .id("len4"),
            node<SendVerifyCodeSmsRequestComponent>()
                .swap("mobile", ref("#requestBody.zuid"))
                .property("sms.len", 6)
                .id("len6"),
            Throw(w_BizErr(ErrorCode.UNKNOWN), "other")
                .log(Level.INFO, "hello world")
        )
    )
}