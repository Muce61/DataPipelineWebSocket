package com.mc.enums;

public enum RespEnum {

    //Config Error 1xxx  基础配置异常
    SYSTEM_ERROR(1000, "系统错误"),
    DATABASE_ERROR(1001, "数据库异常"),
    CONNECTION_ERROR(1002, "网络连接请求失败"),

    API_CONSUMER_EXISTS(4444, "已存在订阅"),
    API_SECRET_NOT_EXISTS(5555, "无订阅权限"),
    //Success 2xxx  业务逻辑成功
    /**
     * 特定使用,谨慎修改
     */
    OPERATE_SUCCESS(2003, "操作成功"),
    QUERY_SUCCESS(2007, "查询成功"),

    //Error 3xxx  业务逻辑失败
    OPERATE_FAILURE(3000, "操作失败"),
    /**
     * 特定使用,谨慎修改
     */
    OPERATE_REPEAT(3001, "操作频繁，请稍后再试"),
    /**
     * 特定使用,谨慎修改
     */
    NO_LOGIN(3004, "用户未登录"),
    NO_RECORD(3007, "没有查到相关记录"),
    INVALID_MOBILE(3010, "无效的手机号码"),
    INVALID_EMAIL(3011, "无效的邮箱"),
    INVALID_FILE(3017, "无效的文件"),
    BAD_FILE_TYPE(3018, "文件类型不支持"),
    UPLOAD_FILE_ERROR(3019, "文件上传失败"),
    DOWNLOAD_ERROR(3020, "文件下载失败"),
    PDF_CREATE_ERROR(3021, "生成PDF文件失败"),
    FLIGHT_PLAN_STATS_ERROR(3022, "当前状态不支持操作"),
    NO_AIR_SPACE_APPLY(3023, "请先关联空域申请"),
    AIR_SPACE_APPLY_EMPTY(3024, "关联空域申请信息查询失败，请稍后重试"),
    AIR_SPACE_APPLY_ERROR(3025, "关联空域申请记录不存在，请重新选择"),
    SERIAL_NO_ERROR(3026, "申请编号生成失败，请稍后重试"),
    AIR_SPACE_APPLY_FILE_EMPTY(3027, "关联空域申请的文件记录不存在，请重新上传"),
    APPLY_PERSON_SAVE_ERROR(3028, "保存申请人信息失败"),
    AIR_SPACE_EXPIRE_ERROR(3029, "关联的空域申请已过期，请重新选择"),
    XML_CREATE_ERROR(3030, "生成xml文件失败"),
    APPLY_CANCEL_STATE_ERROR(3031, "关联的空域申请未审批通过，请重新选择"),
    FLIGHT_TIME_EMPTY(3032, "飞行计划【开始时间】和【结束时间】不能为空"),
    FLIGHT_END_TIME_ERROR(3033, "飞行计划【结束时间】不能早于当前时间"),
    FLIGHT_START_END_TIME_ERROR(3034, "飞行计划【结束时间】不能早于【开始时间】"),
    FLIGHT_PLAN_TIME_ERROR(3035, "【非紧急任务】需提前1天进行申请"),
    FILE_SUM_ERROR(3036, "上传文件个数超限，请重新提交"),
    SIZE_LIMIT_ERROR(3037, "文件大小超过限制"),
    AIR_SPACE_APPLY_SAVE_ERROR(3038, "生成空域申请失败，请稍后重试"),
    LEGAL_ID_NEGATIVE_EMPTY(3039, "请先上传【法人身份证反面照片】"),
    LEGAL_ID_POSITIVE_ERROR(3040, "请先上传【法人身份证正面照片】"),
    NOT_CRIME_FILE_TYPE_ERROR(3041, "请上传正确的【无犯罪记录证明】，支持文档（pdf）或图片（jpg、jpeg、png）"),
    FLIGHT_LICENSE_FILE_TYPE_ERROR(3042, "请上传正确的【飞行许可文件】，支持文档（pdf）或图片（jpg、jpeg、png）"),
    PERSON_NAME_EMPTY(3043, "请填写个人姓名"),
    PERSON_PHONE_EMPTY(3044, "请填写个人手机号"),
    PERSON_EMAIL_EMPTY(3045, "请填写个人邮箱"),
    PERSON_ID_NO_EMPTY(3046, "请填写个人证件号码"),
    COMPANY_NAME_EMPTY(3047, "请填写企业名称"),
    COMPANY_TYPE_EMPTY(3048, "请填写企业类型"),
    COMPANY_ADDR_EMPTY(3049, "请填写企业注册地址"),
    COMPANY_BUSINESS_LICENSE_CODE_EMPTY(3050, "请填写企业统一社会信用代码或注册号"),
    COMPANY_LEGAL_NAME_EMPTY(3051, "请填写企业法人名称"),
    COMPANY_LEGAL_PHONE_EMPTY(3052, "请填写企业法人手机号"),
    COMPANY_LEGAL_ID_NO_EMPTY(3053, "请填写企业法人证件号码"),
    NOT_CRIME_FILE_EMPTY(3054, "请先上传飞手【无犯罪记录证明】"),
    COMPANY_CONTACT_NAME_EMPTY(3055, "请填写企业联系人"),
    COMPANY_CONTACT_PHONE_EMPTY(3056, "请填写企业联系人手机号"),
    COMPANY_BUSINESS_SCOPE_EMPTY(3057, "请填写企业主要经营范围"),
    COMPANY_BUSINESS_LICENSE_FILES_EMPTY(3058, "请先上传【企业营业执照】"),
    ID_POSITIVE_FILES_EMPTY(3059, "请先上传【身份证正面照片】"),
    ID_NEGATIVE_FILES_EMPTY(3060, "请先上传【身份证反面照片】"),
    UAV_EMPTY_WEIGHT_RANGE_ERROR(3061, "请输入正确的无人机空机重量"),
    UAV_FLIGHT_WEIGHT_RANGE_ERROR(3062, "请输入正确的无人机最大起飞重量"),
    COMPANY_OPS_CONTACTS_NAME_EMPTY(3063, "请填写【企业运营联系人名称】"),
    COMPANY_OPS_CONTACTS_PHONE_EMPTY(3064, "请填写【企业运营联系人手机号码】"),
    COMPANY_OPS_STANDBY_NAME_EMPTY(3065, "请填写【企业运营备用人名称】"),
    COMPANY_OPS_STANDBY_PHONE_EMPTY(3066, "请填写【企业运营备用人手机号码】"),
    DRIVERS_LICENSE_FRONT_EMPTY(3067, "请先上传【无人机驾驶员合格证-正面】"),
    DRIVERS_LICENSE_BACK_EMPTY(3068, "请先上传【无人机驾驶员合格证-反面】"),
    APPLY_PERSON_EMPTY(3069, "申请人信息为空"),
    FLYER_EMPTY(3070, "飞手信息为空"),
    UAV_LIST_EMPTY(3071, "查询无人机信息为空，请核实后再操作"),
    FLIGHT_ACTIVITY_LIST_EMPTY(3072, "查询飞行活动信息为空，请核实后再操作"),
    FLIGHT_TIME_ERROR_EMPTY(3073, "飞行计划时间不在空域申请时间段内"),
    FLIGHT_ACTIVITY_START_TIME_ERROR(3074, "一般飞行活动或长期飞行活动，创建时间应在飞行开始时间前一日12时前"),
    FLIGHT_ACTIVITY_START_EMERGENCY_TIME_ERROR(3074, "紧急飞行活动，创建时间应在飞行开始时间30分钟之前"),
    FLIGHT_ACTIVITY_START_END_TIME_ERROR(3075, "飞行活动【结束时间】不能早于【开始时间】"),


    //Client Error 4xxx  客户端错误  仿照4xx的http错误
    BAD_REQUEST(4000, "错误的请求参数"),
    UNAUTHORIZED(4001, "未经授权"),
    PAYMENT_REQUIRED(4002, "付费请求"),
    FORBIDDEN(4003, "资源不可用"),
    NOT_FOUND(4004, "无效的访问路径"),
    METHOD_NOT_ALLOWED(4005, "不合法的请求方式"),
    NOT_ACCEPTABLE(4006, "不可接受"),
    PROXY_AUTHENTICATION_REQUIRED(4007, "需要代理身份验证"),
    REQUEST_TIMEOUT(4008, "请求超时"),
    CONFLICT(4009, "指令冲突"),
    GONE(4010, "文档永久地离开了指定的位置"),
    LENGTH_REQUIRED(4011, "需要CONTENT-LENGTH头请求"),
    PRECONDITION_FAILED(4012, "前提条件失败"),
    REQUEST_ENTITY_TOO_LARGE(4013, "请求实体太大"),
    REQUEST_URI_TOO_LONG(4014, "请求URI太长"),
    UNSUPPORTED_MEDIA_TYPE(4015, "不支持的媒体类型"),
    REQUESTED_RANGE_NOT_SATISFIABLE(4016, "请求的范围不可满足"),
    EXPECTATION_FAILED(4017, "期望失败"),


    //Server Error 5xxx  服务器错误  仿照5xx的http错误
    INTERNAL_SERVER_ERROR(5000, "内部服务器错误"),
    NOT_IMPLEMENTED(5001, "未实现"),
    BAD_GATEWAY(5002, "错误的网关"),
    SERVICE_UNAVAILABLE(5003, "服务不可用"),
    GATEWAY_TIMEOUT(5004, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(5005, "HTTP版本不支持"),


    //终极赖皮手段
    UNKNOWN_ERROR(0000, "未知错误");

    private Integer code;
    private String msg;

    private RespEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
