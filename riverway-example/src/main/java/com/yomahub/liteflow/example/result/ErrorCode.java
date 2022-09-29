package com.yomahub.liteflow.example.result;

public enum ErrorCode {
    SUCCESS(0, "response.success"),
    // system error
    SYS_INVALID_FILE(1, "response.failure.sysfail"),
    SYS_UP_REQ_ISNULL(2, "response.failure.reqisnull"),

    SYS_MOBILE_VERIFY_FAIL(3, "userbase.error.format.mobile"),
    SYS_PARAMETER_ERROR(4, "check.parm.error"),
    SYS_SEND_VERIFY_CODE_ERROR(5, "response.failure.bankcard.sendverifycode"),
    SYS_JSON_FORMAT_ERROR(6, "response.failure.sysfail"),
    SYS_TOKEN_INVALID(7, "response.failure.token.invalid"),
    SYS_VALIDATE_CODE_FAIL(8, "response.error.verifycodeerror"),
    SYS_OP_TOO_FREQUENTLY(9, "response.failure.sysoptoofrequently"),
    SYS_BUSY_OPERATION(12, "response.failure.operationbusy"),
    SYS_LIMIT_OPERATION(13, "response.failure.operation.limit"),
    SYS_FAIL(10, "response.failure.sysfail"),
    SYS_TIMEOUT(11, "response.failure.systimeout"),
    SYS_TRANS_FAIL(14, "response.failure.trans.fail"),
    SYS_USER_LOGOFF(15, "response.error.user.logoff"),
    SYS_RPC_ERROR(16, "response.failure.sysrpcerror"),
    SYS_TOKEN_NOT_MATCH(17, "response.failure.token.existed"),
    SYS_MOBILE_BUSY_OPERATION(18, "response.failure.mobileoperationbusy"),
    SYS_TEL_REVIEW_FAIL(19, "response.tel.review.fail"),
    SYS_INVALID_TASK(20, "response.failure.sysfail"),
    SYS_NETWORK_ERROR(21, "response.network.error"),
    USER_NOT_EXIST(22, "response.error.userisnotexisit"),
    SYS_PARAM_ERROR(23, "response.failure.sysparamerror"),
    SYS_EMAIL_BUSY_OPERATION(24, "response.failure.emailoperationbusy"),
    SYS_EMAIL_BIND_FAIL(25, "response.failure.emailBindFail"),
    SYS_OBTAIN_EMAIL_VD_FAIL(26, "response.failure.obtainEmailVD"),


    DATA_CACHE_PUT_ERR(901, "response.failure.sysparamerror"),
    DATA_CACHE_GET_ERR(902, "response.failure.sysparamerror"),
    DATA_CACHE_EVICT_ERR(903, "response.failure.sysparamerror"),
    DATA_CACHE_CLEAR_ERR(904, "response.failure.sysparamerror"),
    //错误码分段定义
    //通用  1-999

    //注册登录 1000-1199
    USERBASE_ERROR_PASSWORD_FORMAT_ERROR(1000, "userbase.error.password.format.error"),
    USERBASE_ERROR_REGISTER_STATUS_6(1001, "userbase.error.register.status6"),
    USERBASE_ERROR_VALID_VERIFY_CODE(1002, "userbase.error.valid.verify.code"),
    USERBASE_ERROR_PASSWORD_ERROR(1003, "userbase.error.password.error"),
    USERBASE_ERROR_INVITATION_CODE(1004, "userbase.error.invitation.code"),
    OUTERCHANNEL_WRONG_CHANNEL(1005, "oriole.error.wrong.channel"),
    USERBASE_ERROR_USER_LOCKED(1006, "userbase.error.user.locked"),
    USERBASE_MAIN_APP_USER_CROSS(1008, "useraccount.error.null"),
    USERBASE_VEST_APP_USER_CROSS(1009, "useraccount.error.null"),
    USERBASE_ERROR_PASSWORD_BEFORE_LOCKED(1010, "userbase.error.password.error.beforlocked"),
    USERBASE_ERROR_MOBILE_PASSWORD_ERROR(1011, "userbase.error.mobile.password.error"),
    USERBASE_REGISTER_OTHER_CHECKTOKEN_ERR(1012, "response.failure.sysparamerror"),
    USERBASE_ERROR_VERIFY_CODE_ERROR_TIMES(1013, "userbase.error.verify.code.error.times"),//原1047
    USERBASE_ERROR_USER_LOCKED_AND_SEND_GRAPHIC_VERIFICATION(1014, "userbase.error.user.locked"),
    USERBASE_ERROR_GRAPHIC_VERIFICATION_ERROR(1015, "userbase.error.picture.valid.error"),
    USERBASE_ERROR_GRAPHIC_VERIFICATION_NULL(1016, "userbase.error.invalid.picture.verify"),
    //此错误码前端会弹出图形验证码
    USERBASE_ERROR(1017, "userbase.error.mobile.error"),
    //用户被冻结
    USERLIGHTING_ERROR_USERFREEZE(1018, "response.failure.loanuserfreeze"),

    TRANS_PASS_RESET_MAX(1019, "trans.pass.reset.max"),
    TRANS_PASS_MODIFY_MAX(1020, "trans.pass.modify.max"),
    USERBASE_ERROR_VERIFY_CODE_EXPIRED(1021, "userbase.error.verify.code.expired"),
    USERBASE_ERROR_EMAIL_BIND(1022, "userbase.error.email.bind.fail"),
    USERBASE_ERROR_EMAIL_BOUND(1023, "userbase.error.email.bound.fail"),
    USERBASE_ERROR_TOKEN_TIMEOUT(1024, "userbase.error.token.timeout"),
    USERBASE_ERROR_VALID_VERIFY_CODE_EMAIL(1025, "userbase.error.valid.verify.code.email"),
    USERBASE_ERROR_EMAIL_BOUND_SUBMIT_REPEAT(1026, "userbase.error.email.bound.submit.repeat"),
    //账单   1200-1399

    //优惠券  1400-1599

    //用户  1600-1799
    USERBASE_BLACKLIST_LIMIT(1613, "response.user.limit.the.login"),
    USERACCOUNT_ERROR_ACCOUNT_NOTEXISTS(1799, "userbase.error.userbase.notexist"),

    USERBASE_ERROR_NULL_CODETYPE(1614, "userbase.error.null.codetype"),
    USERBASE_ERROR_NULL_MOBILE(1615, "userbase.error.null.mobile"),
    USERBASE_ERROR_FORMAT_MOBILE(1616, "userbase.error.format.mobile"),
    USERBASE_INVALID_PICTURE_VERIFY(1617, "userbase.error.invalid.picture.verify"),
    USERBASE_ERROR_PICTURE_VALID_ERROR(1618, "userbase.error.picture.valid.error"),

    USERBASE_ERROR_PICTURE_HAS_VALIDED(1619, "userbase.error.picture.repeat.valid"),
    USERBASE_ERROR_USER_NOTEXIST(1620, "userbase.error.userbase.notexist"),
    USERBASE_ERROR_REGISTER_STATUS_4(1621, "userbase.error.register.status4"),

    USERBASE_SEND_SMS_LIMIT_TIME_SIXTY(1622, "response.failure.operationbusy"),
    USERBASE_ERROR_VERIFYCODE_RETRY(1623, "userbase.error.verifycode.retry"),
    USERBASE_ERROR_USERFREEZE(1624, "response.failure.loanuserfreeze"),
    USERBASE_ERROR_TIMELESSTHEN60S(1625, "response.failure.loanuserfreeze"),
    USERBASE_ERROR_VERIFYCODEERROR(1626, "response.error.verifycodeerror"),
    USERBASE_ERROR_IDCARDERROR(1627, "response.error.idcarderror"),
    USERALIGHTING_ERROR_UPDATE(1628, "response.failure.sysfail"),
    USERBASE_ERROR_BLANK_UESRGID(1629, "response.failure.illegalUser"),
    BANKCARD_ERROR(1630, "response.error.modifytrans.bankcarderror"),

    USER_STATE_ERROR(1631, "loan.user.state.error"),
    USER_ALRADY_REGISTERED(1632, "response.user.already.registered"),

    USERBASE_ERROR_CREDENTIAL_ERROR(1633, "response.error.credential.error"),
    USERACCOUNT_CHANGE_MOBILE_ERROR(1634, "useraccount.mobile.change.error"),

    USERACCOUNT_CHANGE_MOBILE_SAME_ERROR(1637, "useraccount.mobile.change.same.error"),
    USER_TRADE_LOCKED(1638, "loan.user.trade.locked"),

    //修改手机号
    BVN_ERROR(1639, "response.error.modifymobile.bvn.error"),
    PHONE_NUMBER_ERROR(1640, "response.error.modifymobile.phonenumber.error"),
    BVN_BANKCARD_ERROR(1641, "response.error.modifymobile.bvn.bankcard.error"),
    PASSWORD_NOT_MATCH_ERROR(1642, "response.error.modifymobile.password.not.match.error"),
    SAME_PHONE_NUMBER_ERROR(1643, "response.error.modifymobile.same.phone.number.error"),

    //消息   1800-1999
    //授信  2000-2199
    CREDIT_ID_CARD_NULL(2001, "response.error.idcarderror"),
    CREDIT_NOTIFY_TYPE_NULL(2002, "response.failure.notify.type.null"),
    CREDIT_MSG_NULL(2003, "response.failure.sysfail"),
    USER_ACCOUNT_ERROR_ACCOUNT_NOT_EXISTS(2006, "user.outer.account.null"),

    //首页  2200-2399
    USER_ACCOUNT_NOT_EXIT(2200, "useraccount.error.null"),
    //配置  2400-2599
    CONFIGPRODUCT_ERROR_PRODUCT_NOTEXISTS(2599, "response.failure.sysparamerror"),
    //借款  2600-2799
    LOAN_PASSWORD_ERROR(2600, "response.error.password_error"),
    LOAN_MULTI_RATE_ERROR(2601, "response.failure.loan.multi.rate"),
    LOAN_RATE_ERROR(2602, "loan.rate.error"),
    LOAN_APPALY_ORDER_EXIST(2603, "loan.apply_order_is_exist"),
    LOAN_APPALY_AMOUNT_MULTIPLE_ERROR(2605, "loan.apply_loan_amount_is_multiple"),
    LOAN_APPALY_AMOUNT_LOWEST_ERROR(2606, "loan.apply_loan_amount_lowest"),
    LOAN_APPALY_AMOUNT_HIGHEST_ERROR(2606, "loan.apply_loan_amount_highest"),
    LOAN_USER_HAS_OVERDUE_ORDER(2607, "loan.apply_user_is_overdue"),
    LOAN_USER_BUSIC_AUTH_FAIL(2608, "response.error.fengkong"),
    LOAN_AMOUNT_ERROR(2609, "loan.apply_loan_amount_than"),
    LOAN_ROUTE_ERROR(2610, "loan.apply_loan_route_error"),
    LOAN_REFUSE_ERROR(2611, "loan.apply_loan.refuse.error"),
    LOAN_USER_STATE_ERROR(2612, "loan.confirm_user_state"),
    LOAN_CONFIRM_NOT_EXIST(2613, "loan.confirm_order_not_exist"),
    LOAN_CONFIRM_NOT_AUTH_PASS(2614, "loan.confirm_not_auth_pass"),
    LOAN_CONFIRM_IS_ALREADY(2615, "loan.confirm_loan_already"),
    LOAN_IS_NOT_APPLY(2616, "loan.not_apply"),
    LOAN_CONFIRM_IN_PROGRESS(2709, "loan.confirm_in_progress"),

    WITHDRAW_ALREADY_EXISTS(2617, "withdraw.already.exists"),
    DIFFERENT_TYPES_OF_LOANS_SHORT(2618, "loan.different_types_of_loans_short"),
    DIFFERENT_TYPES_OF_LOANS_SUB(2619, "loan.different_types_of_loans_sub"),
    //拒贷
    LOAN_APPLY_REFUSE_FLAG_CAN_NOT_LOAN(2708, "loan.apply_loan.refuse.error"),


    //活动  2800-2999
    VOUCHER_ERROR_NOTRADEUSEDVOUCHER(2800, "response.voucher.notradeusedvoucher"),
    VOUCHER_ERROR_NO_FRIEDNS_ARE_INITED(2801, "response.no.friends.are.invited"),
    VOUCHER_ERROR_USER_HAVE_BEEN_INVITED(2802, "response.users.have.been.invited"),
    VOUCHER_ERROR_VOUCHER_ISNOTVAILD(2803, "response.voucher.isnotvalid"),
    VOUCHER_ERROR_ACTITIES_HAV_FAILED(2804, "response.activities.have.failed"),


    //还款  3000-3199
    CHANNEL_TYPE_IS_NULL(3000, "response.failure.sysfail"),
    LOAN_RECORD_NOTEXISTS(3001, "loan.record.notexists"),
    REPAYMENT_RECORD_NOTEXISTS(3002, "repayment.record.notexists"),
    SYS_PAYMENT_TIME_ERROR(3003, "repayment.paytime.error"),
    LOAN_STATUS_ERROR(3004, "loan.status.error"),
    REPAYMENT_AMOUNT_ILLEGAL(3005, "repayment.amount.illegal"),
    REPAYMENT_STATUS_FAIL(3006, "repayment.status.fail"),
    REDIRECT_REPAYMENT_FAIL(3007, "redirect.repayment.fail"),
    DEDUCT_REPAYMENT_PROCESS(3008, "deduct.repayment.processing"),
    OVERDUE_CALCULATE_LOCKED(3009, "overdue.calculate.now"),
    DYNAMIC_VIRTUAL_ACCOUNT_NOT_EXISTS(3010, "dynamic.VA.not.exists"),
    DEDUCT_REPAYMENT_REPEAT(3011, "deduct.repayment.repeat"),
    /**
     * 借款未逾期
     */
    LOAN_NOT_OVERDUE(3012, "loan.not.overdue"),
    BANKCARD_REPAYMENT_FAILED_TIMES_EXCEEDED(3013, "bankcard.repayment.failed.times.exceeded"),

    RENEW_STATE_ERROR(3014, "renew.state.error"),
    RENEW_ALREADY_FINISH(3015, "renew.already.finish"),
    TRANS_FAIL_ROUTE_CHANNEL(3021, "response.failure.sysfail"),
    //续期相关
    //3100-3199
    RENEW_RULE_NOT_EXIST(3100, "renew.rule.not.exist"),

    LOAN_EXCEEDING_RULE_NOT_MATCH(3150, "exceeding.rule.not.match"),
    LOAN_ALREADY_EXCEEDING(3151, "exceeding.loan.already.exceeded"),
    LOAN_EXCEEDING_EXPIRED(3152, "exceeding.permit.expired"),

    //退款  3200-3399
    REFUND_AMT_NOT_MATCH(3200, "refund.amt.mot.match"),
    REFUND_STATUS_ERROR(3201, "refund.status.error"),
    REFUND_OLD_WITHDRAW_NOT_FAIL(3202, "refund.old.withdraw.not.fail"),

    //帮助  3400-3599

    //绑卡 3600-3699
    //数据库异常
    BANKCARD_DATABASE_ERROR(3600, "response.failure.sysfail"),
    //绑卡超出限制数量
    BANKCARD_BEYOND_MAX(3601, "bankcard.exceed.limit"),
    //该卡正在处理中
    BANKCARD_BIND_PROCESS(3602, "bankcard.bind.process"),
    //该卡已其他用户被绑定
    BANKCARD_BIND_SUCCESS_BY_OTHER(3603, "bankcard.bind.success.by.other"),
    //该卡已绑定成功
    BANKCARD_ALREADY_BOUND_SUC(3604, "bankcard.bind.already.success"),
    //绑卡相关
    BANK_CARD_NOT_EXIST(3605, "response.error.cardisnotself"),
    BANK_CARD_INFO_ERROR(3606, "response.failure.bankcard.parameter.empty"),
    // 当前卡无法删除，有在途订单
    BANKCARD_NOT_CAN_DELETE(3607, "response.cannot.del"),
    // 删除失败
    BANKCARD_DEL_FAIL(3608, "response.cannot.del"),
    // 银行列表未配置
    BANKCARD_BANK_CONFIG_EMPTY(3609, "bankcard.bank.config.null"),
    BANKCARD_BIND_FAILED_MAX(3610, "bankcard.bind.failed.max"),

    BANKCARD_BIND_FAILED(3611, "bankcard.bind.failed"),
    // 银行卡 code或者 name 错误
    BANKCODE_BANKNAME_ERROR(3612, "bankcode.bankname.error"),
    // 删除失败
    BANKCARD_MODIFY_FAIL(3613, "response.cannot.modify"),
    //绑卡超出限制数量(账户级别)
    BANKCARD_BEYOND_MAX_CARDNO(3614, "bankcard.exceed.limit.cardno"),
    BANKCARD_BIN_VERIFY_ERROR(3615, "Please enter the correct bank card number"),
    BANKCARD_CARD_TYPE_ERROR(3616, "unsupported.card.type"),
    BANKCARD_UPDATE_ERROR_LOAN_EXIST(3617, "record loan exists."),

    // 手机号不存在
    USERBASE_ERROR_MOBILE_NOTEXIST(1035, "response.mobile.notexist"),
    // 注册手机号存在
    USERBASE_ERROR_MOBILE_ISEXIST(1036, "response.register.mobile.is.exist"),
    //邮箱不存在
    USERBASE_EXTEND_ERROR_EMAIL_NOTEXIST(1037, "response.email.notexist"),
    // 访问太频繁
    TOO_MANY_VISITS(1038, "too.many.visits"),
    //申请借款超出限制
    LOAN_APPLY_TIMES_OVER_LIMIT(1039, "loan.apply.times.over.limit"),
    // 时间规范
    IS_EXCEEDTIME_ADD_FEED(11001, "response.failure.add.feedback"),

    //额度减免
    REDUCE_INVALID_AMOUNT(4027, "The allowance is not correct"),

    //重传风控
    USERACCOUNT_ERROR_PARMERROR(4028, "Failure of information retrieval, please refresh and retry"),

    //新设备打开信息上传
    // 重复上传
    REPORT_DEVICE_DUPLICATED_REPORT(4029, "Duplicated report"),

    //红包金额不满1000
    ACTIVITY_SEND_REDPACK_INSUFFICIENT1000(4101, "activity.send.redpack.insufficient1000"),
    ACTIVITY_SEND_REDPACK_INSUFFICIENT2000(4201, "activity.send.redpack.insufficient2000"),

    //红包金额不一致
    ACTIVITY_SEND_REDPACK_NOT_EQUAL(4104, "activity.send.redpack.notequal"),

    //提现红包 如果有待还款的订单 提示信息
    ACTIVITY_REDPACK_WITHDRAWAL(4103, "activity.redpack.withdrawal"),


    NO_RECORD(4102, "no record"),


    //资金计划错误
    FUND_PLAN_ERROR(5103, "fund plan error"),

    //BVN 为空或位数不够：请输入11位BVN号
    BVN_FORMAT_ERROR(6101, "bvn format error"),

    BVN_HAS_EXIST_ERROR(6102, "The BVN already exists"),
    //今天验证次数已达到上限
    BVN_CHECK_TWO_TIMES(6103, "The number of verifications today has reached the online"),

    //和认证时填写的BVN一致
    BVN_CHECK_NOT_SAME(6104, "bvn checked not same"),
    //和认证时填写的BVN一致
    BVN_CHECK_SAME(6105, "Cannot be the same as the current bvn"),

    //不符合修改BVN条件
    BVN_NOT_UP_TO_STANDARD(6106, "You are not qualified to modify bvn"),

    //未知错误
    UNKNOWN(-1, "response.failure.sysfail"),

//    VALID_ERROR(6107,"valid param is not pass"),
    SHORT_LINK_ERROR_CODE(8808, "get short link fail");



    private final int code;
    private final String msg;

    ErrorCode(int code, String msgKey) {
        this.code = code;
        this.msg = msgKey;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ErrorCode from(int status) {
        for (ErrorCode code : values()) {
            if (code.code == status) {
                return code;
            }
        }
        return UNKNOWN;
    }
}
