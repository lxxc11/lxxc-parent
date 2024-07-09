-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user"
(
    "id"                   varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
    "user_name"            varchar(100) COLLATE "pg_catalog"."default",
    "real_name"            varchar(600) COLLATE "pg_catalog"."default",
    "password"             varchar(255) COLLATE "pg_catalog"."default",
    "salt"                 varchar(45) COLLATE "pg_catalog"."default",
    "sex"                  int2,
    "status"               bool,
    "contact"              varchar(600) COLLATE "pg_catalog"."default",
    "email"                varchar(600) COLLATE "pg_catalog"."default",
    "effective_time"       timestamp(0),
    "password_update_time" timestamp(0),
    "history_password"     varchar(500) COLLATE "pg_catalog"."default",
    "public_key"           varchar(255) COLLATE "pg_catalog"."default",
    "private_key"          varchar(255) COLLATE "pg_catalog"."default",
    "create_by"            varchar(32) COLLATE "pg_catalog"."default",
    "create_time"          timestamp(0),
    "update_by"            varchar(32) COLLATE "pg_catalog"."default",
    "update_time"          timestamp(0),
    "del_flag"             bool,
    "position"             varchar(600) COLLATE "pg_catalog"."default",
    "super_flag"           bool,
    "head_picture"         varchar(255) COLLATE "pg_catalog"."default",
    "type"                 int2 DEFAULT 1,
    CONSTRAINT "user_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_user"."id" IS 'id';
COMMENT ON COLUMN "sys_user"."user_name" IS '登录账号';
COMMENT ON COLUMN "sys_user"."real_name" IS '真实姓名';
COMMENT ON COLUMN "sys_user"."password" IS '密码';
COMMENT ON COLUMN "sys_user"."salt" IS 'md5密码盐';
COMMENT ON COLUMN "sys_user"."sex" IS '性别(0-默认未知,1-男,2-女)';
COMMENT ON COLUMN "sys_user"."status" IS '启用状态（false停用，true启用）';
COMMENT ON COLUMN "sys_user"."contact" IS '联系方式';
COMMENT ON COLUMN "sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "sys_user"."effective_time" IS '有效时间';
COMMENT ON COLUMN "sys_user"."password_update_time" IS '密码更新时间';
COMMENT ON COLUMN "sys_user"."history_password" IS '历史密码（保存历史五次的密码）';
COMMENT ON COLUMN "sys_user"."public_key" IS 'SM2公钥';
COMMENT ON COLUMN "sys_user"."private_key" IS 'SM2私钥';
COMMENT ON COLUMN "sys_user"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user"."del_flag" IS '是否删除';
COMMENT ON COLUMN "sys_user"."position" IS '职务';
COMMENT ON COLUMN "sys_user"."super_flag" IS '是否是超级管理员';
COMMENT ON COLUMN "sys_user"."head_picture" IS '头像';
COMMENT ON COLUMN "sys_user"."type" IS '用户类型（1.平台类型。2.业务类型）';
COMMENT ON TABLE "sys_user" IS '用户中心-用户表';


-- ----------------------------
-- Table structure for sys_platform
-- ----------------------------
DROP TABLE IF EXISTS "sys_platform";
CREATE TABLE "sys_platform"
(
    "id"             varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "system_name"    varchar(255) COLLATE "pg_catalog"."default",
    "system_en_name" varchar(255) COLLATE "pg_catalog"."default",
    "url"            varchar(255) COLLATE "pg_catalog"."default",
    "domain_name"    varchar(255) COLLATE "pg_catalog"."default",
    "logo"           varchar(255) COLLATE "pg_catalog"."default",
    "status"         bool,
    "create_by"      varchar(32) COLLATE "pg_catalog"."default",
    "create_time"    timestamp(0),
    "update_by"      varchar(32) COLLATE "pg_catalog"."default",
    "update_time"    timestamp(0),
    "del_flag"       bool,
    "sort_order"     int8,
    "super_power"    bool,
    CONSTRAINT "sys_platform_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_platform"."id" IS 'id';
COMMENT ON COLUMN "sys_platform"."system_name" IS '系统名称';
COMMENT ON COLUMN "sys_platform"."system_en_name" IS '英文名称';
COMMENT ON COLUMN "sys_platform"."url" IS 'url';
COMMENT ON COLUMN "sys_platform"."domain_name" IS '域名';
COMMENT ON COLUMN "sys_platform"."logo" IS 'logo';
COMMENT ON COLUMN "sys_platform"."status" IS '系统状态（false停用，true启用）';
COMMENT ON COLUMN "sys_platform"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_platform"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_platform"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_platform"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_platform"."del_flag" IS '是否删除';
COMMENT ON COLUMN "sys_platform"."sort_order" IS '排序';
COMMENT ON COLUMN "sys_platform"."super_power" IS '是否被超级管理员管理';
COMMENT ON TABLE "sys_platform" IS '平台管理表';


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "role_name"   varchar(255) COLLATE "pg_catalog"."default",
    "description" varchar(255) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_role"."id" IS 'id';
COMMENT ON COLUMN "sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "sys_role"."description" IS '权限描述';
COMMENT ON COLUMN "sys_role"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_role" IS '用户中心-角色表';


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu"
(
    "id"           varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id"    varchar(32) COLLATE "pg_catalog"."default",
    "platform_id"  varchar(32) COLLATE "pg_catalog"."default",
    "menu_name"    varchar(50) COLLATE "pg_catalog"."default",
    "menu_en_name" varchar(50) COLLATE "pg_catalog"."default",
    "type"         int2,
    "url"          varchar(500) COLLATE "pg_catalog"."default",
    "sort_order"   int8,
    "status"       bool,
    "icon"         varchar(50) COLLATE "pg_catalog"."default",
    "create_by"    varchar(32) COLLATE "pg_catalog"."default",
    "create_time"  timestamp(0),
    "update_by"    varchar(32) COLLATE "pg_catalog"."default",
    "update_time"  timestamp(0),
    "del_flag"     bool,
    "other_data"   varchar(500) COLLATE "pg_catalog"."default",
    CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_menu"."id" IS 'id';
COMMENT ON COLUMN "sys_menu"."parent_id" IS '父级id';
COMMENT ON COLUMN "sys_menu"."platform_id" IS '平台id';
COMMENT ON COLUMN "sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "sys_menu"."menu_en_name" IS '英文名称';
COMMENT ON COLUMN "sys_menu"."type" IS '菜单类型（1目录 2菜单）';
COMMENT ON COLUMN "sys_menu"."url" IS '菜单URL';
COMMENT ON COLUMN "sys_menu"."sort_order" IS '排序';
COMMENT ON COLUMN "sys_menu"."status" IS '菜单状态（false停用 true启用）';
COMMENT ON COLUMN "sys_menu"."icon" IS '菜单ICON';
COMMENT ON COLUMN "sys_menu"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_menu"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_menu"."del_flag" IS '是否删除';
COMMENT ON COLUMN "sys_menu"."other_data" IS '其他数据集（前端所用字段，不涉及后端逻辑）';
COMMENT ON TABLE "sys_menu" IS '系统管理-菜单表';


-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS "sys_function";
CREATE TABLE "sys_function"
(
    "id"            varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "menu_id"       varchar(32) COLLATE "pg_catalog"."default",
    "function_name" varchar(255) COLLATE "pg_catalog"."default",
    "url"           varchar(255) COLLATE "pg_catalog"."default",
    "status"        bool,
    "icon"          varchar(255) COLLATE "pg_catalog"."default",
    "create_by"     varchar(32) COLLATE "pg_catalog"."default",
    "create_time"   timestamp(0),
    "update_by"     varchar(32) COLLATE "pg_catalog"."default",
    "update_time"   timestamp(0),
    "del_flag"      bool,
    CONSTRAINT "sys_function_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_function"."id" IS 'id';
COMMENT ON COLUMN "sys_function"."menu_id" IS '菜单id';
COMMENT ON COLUMN "sys_function"."function_name" IS '功能点名称';
COMMENT ON COLUMN "sys_function"."url" IS '功能点URL';
COMMENT ON COLUMN "sys_function"."status" IS '功能点状态（false停用 true启用）';
COMMENT ON COLUMN "sys_function"."icon" IS '功能页面ICON';
COMMENT ON COLUMN "sys_function"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_function"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_function"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_function"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_function"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_function" IS '功能表';


-- ----------------------------
-- Table structure for sys_data_set
-- ----------------------------
DROP TABLE IF EXISTS "sys_data_set";
CREATE TABLE "sys_data_set"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "menu_id"     varchar(32) COLLATE "pg_catalog"."default",
    "data_name"   varchar(255) COLLATE "pg_catalog"."default",
    "content_id"  varchar(32) COLLATE "pg_catalog"."default",
    "content"     varchar(255) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_data_set_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_data_set"."id" IS 'id';
COMMENT ON COLUMN "sys_data_set"."menu_id" IS '菜单id';
COMMENT ON COLUMN "sys_data_set"."data_name" IS '数据集名称';
COMMENT ON COLUMN "sys_data_set"."content_id" IS '数据集内容id';
COMMENT ON COLUMN "sys_data_set"."content" IS '数据集内容';
COMMENT ON COLUMN "sys_data_set"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_data_set"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_data_set"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_data_set"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_data_set"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_data_set" IS '数据集表';


-- ----------------------------
-- Table structure for sys_data_set_index
-- ----------------------------
DROP TABLE IF EXISTS "sys_data_set_index";
CREATE TABLE "sys_data_set_index"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "data_set_id" varchar(32) COLLATE "pg_catalog"."default",
    "parent_id"   varchar(32) COLLATE "pg_catalog"."default",
    "index_id"    varchar(32) COLLATE "pg_catalog"."default",
    "key"         varchar(255) COLLATE "pg_catalog"."default",
    "label"       varchar(255) COLLATE "pg_catalog"."default",
    "value"       varchar(255) COLLATE "pg_catalog"."default",
    "sort_order"  int8,
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_data_set_index_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_data_set_index"."id" IS 'id';
COMMENT ON COLUMN "sys_data_set_index"."data_set_id" IS '数据集id';
COMMENT ON COLUMN "sys_data_set_index"."parent_id" IS '父id';
COMMENT ON COLUMN "sys_data_set_index"."index_id" IS '指标id';
COMMENT ON COLUMN "sys_data_set_index"."key" IS '字典key';
COMMENT ON COLUMN "sys_data_set_index"."label" IS '字典label';
COMMENT ON COLUMN "sys_data_set_index"."value" IS '字典value';
COMMENT ON COLUMN "sys_data_set_index"."sort_order" IS '排序';
COMMENT ON COLUMN "sys_data_set_index"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_data_set_index"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_data_set_index"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_data_set_index"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_data_set_index"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_data_set_index" IS '数据集指标表';


-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS "sys_depart";
CREATE TABLE "sys_depart"
(
    "id"             varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id"      varchar(36) COLLATE "pg_catalog"."default",
    "depart_name"    varchar(255) COLLATE "pg_catalog"."default",
    "type"           int2,
    "district_level" varchar(255) COLLATE "pg_catalog"."default",
    "create_by"      varchar(32) COLLATE "pg_catalog"."default",
    "create_time"    timestamp(0),
    "update_by"      varchar(32) COLLATE "pg_catalog"."default",
    "update_time"    timestamp(0),
    "del_flag"       bool,
    CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_depart"."id" IS 'id';
COMMENT ON COLUMN "sys_depart"."parent_id" IS '父id';
COMMENT ON COLUMN "sys_depart"."depart_name" IS '部门名称';
COMMENT ON COLUMN "sys_depart"."type" IS '类型（1地区 2部门）';
COMMENT ON COLUMN "sys_depart"."district_level" IS '行政级别';
COMMENT ON COLUMN "sys_depart"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_depart"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_depart"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_depart"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_depart"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_depart" IS '用户中心-部门表';


-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "user_id"     varchar(32) COLLATE "pg_catalog"."default",
    "role_id"     varchar(32) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_user_role"."id" IS 'id';
COMMENT ON COLUMN "sys_user_role"."user_id" IS '用户id';
COMMENT ON COLUMN "sys_user_role"."role_id" IS '角色id';
COMMENT ON COLUMN "sys_user_role"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_user_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user_role"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_user_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user_role"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_user_role" IS '用户中心-用户角色表';


-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_depart";
CREATE TABLE "sys_user_depart"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "user_id"     varchar(32) COLLATE "pg_catalog"."default",
    "depart_id"   varchar(32) COLLATE "pg_catalog"."default",
    "type"        int2,
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_user_depart_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_user_depart"."id" IS 'id';
COMMENT ON COLUMN "sys_user_depart"."user_id" IS '用户id';
COMMENT ON COLUMN "sys_user_depart"."depart_id" IS '部门id';
COMMENT ON COLUMN "sys_user_depart"."type" IS '部门类型（0所属部门 1兼职部门）';
COMMENT ON COLUMN "sys_user_depart"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_user_depart"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user_depart"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_user_depart"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user_depart"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_user_depart" IS '用户中心-用户部门表';


-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "role_id"     varchar(32) COLLATE "pg_catalog"."default",
    "menu_id"     varchar(32) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_role_menu"."id" IS 'id';
COMMENT ON COLUMN "sys_role_menu"."role_id" IS '角色id';
COMMENT ON COLUMN "sys_role_menu"."menu_id" IS '菜单id';
COMMENT ON COLUMN "sys_role_menu"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_role_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role_menu"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_role_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role_menu"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_role_menu" IS '用户中心-角色菜单表';


-- ----------------------------
-- Table structure for sys_role_menu_function
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu_function";
CREATE TABLE "sys_role_menu_function"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "role_id"     varchar(32) COLLATE "pg_catalog"."default",
    "menu_id"     varchar(32) COLLATE "pg_catalog"."default",
    "function_id" varchar(32) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_role_menu_function_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_role_menu_function"."id" IS 'id';
COMMENT ON COLUMN "sys_role_menu_function"."role_id" IS '角色id';
COMMENT ON COLUMN "sys_role_menu_function"."menu_id" IS '菜单id';
COMMENT ON COLUMN "sys_role_menu_function"."function_id" IS '功能id';
COMMENT ON COLUMN "sys_role_menu_function"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_role_menu_function"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role_menu_function"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_role_menu_function"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role_menu_function"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_role_menu_function" IS '角色-菜单-功能表';


-- ----------------------------
-- Table structure for sys_role_menu_data
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu_data";
CREATE TABLE "sys_role_menu_data"
(
    "id"                varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "role_id"           varchar(32) COLLATE "pg_catalog"."default",
    "menu_id"           varchar(32) COLLATE "pg_catalog"."default",
    "data_set_id"       varchar(32) COLLATE "pg_catalog"."default",
    "data_set_index_id" varchar(32) COLLATE "pg_catalog"."default",
    "create_by"         varchar(32) COLLATE "pg_catalog"."default",
    "create_time"       timestamp(0),
    "update_by"         varchar(32) COLLATE "pg_catalog"."default",
    "update_time"       timestamp(0),
    "del_flag"          bool,
    CONSTRAINT "sys_role_menu_data_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_role_menu_data"."id" IS 'id';
COMMENT ON COLUMN "sys_role_menu_data"."role_id" IS '角色id';
COMMENT ON COLUMN "sys_role_menu_data"."menu_id" IS '菜单id';
COMMENT ON COLUMN "sys_role_menu_data"."data_set_id" IS '数据集id';
COMMENT ON COLUMN "sys_role_menu_data"."data_set_index_id" IS '数据集指标id';
COMMENT ON COLUMN "sys_role_menu_data"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_role_menu_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role_menu_data"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_role_menu_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role_menu_data"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_role_menu_data" IS '角色-菜单-数据表';


-- ----------------------------
-- Table structure for sys_role_platform
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_platform";
CREATE TABLE "sys_role_platform"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "role_id"     varchar(32) COLLATE "pg_catalog"."default",
    "platform_id" varchar(32) COLLATE "pg_catalog"."default",
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_role_platform_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_role_platform"."id" IS 'id';
COMMENT ON COLUMN "sys_role_platform"."role_id" IS '角色id';
COMMENT ON COLUMN "sys_role_platform"."platform_id" IS '平台id';
COMMENT ON COLUMN "sys_role_platform"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_role_platform"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role_platform"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_role_platform"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role_platform"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_role_platform" IS '角色平台表';


-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict";
CREATE TABLE "sys_dict"
(
    "id"          varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "platform_id" varchar(32) COLLATE "pg_catalog"."default",
    "parent_id"   varchar(32) COLLATE "pg_catalog"."default",
    "key"         varchar(255) COLLATE "pg_catalog"."default",
    "label"       varchar(255) COLLATE "pg_catalog"."default",
    "value"       varchar(255) COLLATE "pg_catalog"."default",
    "status"      bool,
    "sort_order"  int8,
    "create_by"   varchar(32) COLLATE "pg_catalog"."default",
    "create_time" timestamp(0),
    "update_by"   varchar(32) COLLATE "pg_catalog"."default",
    "update_time" timestamp(0),
    "del_flag"    bool,
    CONSTRAINT "sys_dict_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_dict"."id" IS 'id';
COMMENT ON COLUMN "sys_dict"."platform_id" IS '平台id';
COMMENT ON COLUMN "sys_dict"."parent_id" IS '父id';
COMMENT ON COLUMN "sys_dict"."key" IS '字典key';
COMMENT ON COLUMN "sys_dict"."label" IS '字典label';
COMMENT ON COLUMN "sys_dict"."value" IS '字典value';
COMMENT ON COLUMN "sys_dict"."status" IS '菜单状态（false停用 true启用）';
COMMENT ON COLUMN "sys_dict"."sort_order" IS '排序';
COMMENT ON COLUMN "sys_dict"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_dict"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict"."del_flag" IS '是否删除';
COMMENT ON TABLE "sys_dict" IS '用户中心-字典表';


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "sys_log";
CREATE TABLE "sys_log"
(
    "id"            varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "platform_id"   varchar(32) COLLATE "pg_catalog"."default",
    "log_type"      int4,
    "log_content"   varchar(1000) COLLATE "pg_catalog"."default",
    "operate_type"  int4,
    "user_name"     varchar(32) COLLATE "pg_catalog"."default",
    "real_name"     varchar(100) COLLATE "pg_catalog"."default",
    "ip"            varchar(100) COLLATE "pg_catalog"."default",
    "method"        varchar(500) COLLATE "pg_catalog"."default",
    "request_param" text COLLATE "pg_catalog"."default",
    "request_type"  varchar(10) COLLATE "pg_catalog"."default",
    "cost_time"     int8,
    "create_by"     varchar(32) COLLATE "pg_catalog"."default",
    "create_time"   timestamp(0),
    "update_by"     varchar(32) COLLATE "pg_catalog"."default",
    "update_time"   timestamp(0),
    CONSTRAINT "sys_log_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "sys_log"."id" IS 'id';
COMMENT ON COLUMN "sys_log"."platform_id" IS '平台id';
COMMENT ON COLUMN "sys_log"."log_type" IS '日志类型（1登录日志，2操作日志）';
COMMENT ON COLUMN "sys_log"."log_content" IS '日志内容';
COMMENT ON COLUMN "sys_log"."operate_type" IS '操作类型（1查询，2添加，3修改，4删除，5导入，6导出，7登录，8退出，9审核）';
COMMENT ON COLUMN "sys_log"."user_name" IS '操作用户账号';
COMMENT ON COLUMN "sys_log"."real_name" IS '操作用户名称';
COMMENT ON COLUMN "sys_log"."ip" IS 'IP';
COMMENT ON COLUMN "sys_log"."method" IS '请求java方法';
COMMENT ON COLUMN "sys_log"."request_param" IS '请求参数';
COMMENT ON COLUMN "sys_log"."request_type" IS '请求类型';
COMMENT ON COLUMN "sys_log"."cost_time" IS '耗时（毫秒 ms）';
COMMENT ON COLUMN "sys_log"."create_by" IS '创建人';
COMMENT ON COLUMN "sys_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_log"."update_by" IS '更新人';
COMMENT ON COLUMN "sys_log"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_log" IS '日志';

-- <------------------------------------------
-- 2.1.1-SNAPSHOT版本新增语句
ALTER TABLE sys_function ADD COLUMN function_tag varchar(50);
COMMENT ON COLUMN "sys_function"."function_tag" IS '功能标识';

-- >------------------------------------------
