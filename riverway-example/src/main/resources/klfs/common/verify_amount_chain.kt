package klfs.common

import com.yomahub.liteflow.components.NoOpComponent
import com.yomahub.liteflow.parser.kotlin.dsl.*
import klfs.register.RegisterDo

val VerifyAmountChain = chain("verifyAmountChain") {
    THEN(
        SWITCH(
            node<NoOpComponent>()
                .swap("loanType", ref("#loanType"))
        ).to(
            RegisterDo.id("loanProductTypeChain")
        )
    )
}