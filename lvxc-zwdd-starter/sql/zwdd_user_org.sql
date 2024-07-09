-- hz_ads_prv.flw_zwdd_organization definition

-- Drop table

-- DROP TABLE hz_ads_prv.flw_zwdd_organization;

CREATE TABLE hz_ads_prv.flw_zwdd_organization (
	organization_code varchar(255) NOT NULL, -- 组织code
	organization_name varchar(255) NULL, -- 组织名称
	display_order int4 NULL, -- 排序
	parent_code varchar(255) NULL, -- 父组织Code
	parent_name varchar(255) NULL, -- 父组织Name
	status varchar(255) NULL, -- 组织状态
	type_code varchar(255) NULL, -- 组织类型code
	type_name varchar(255) NULL, -- 组织类型名称
	gmt_create timestamp NULL, -- 组织创建时间
	responsible_employee_codes varchar(255) NULL, -- 负责人code，|拼接多个
	business_strip_codes varchar(255) NULL, -- 条线codes,多个用|拼接
	address varchar(255) NULL, -- 详细地址
	contact_employee_code varchar(255) NULL, -- 联系人code
	contact_number varchar(255) NULL, -- 联系人号码
	division_code varchar(255) NULL, -- 行政区划code
	short_name varchar(255) NULL, -- 组织全称
	institution_code varchar(255) NULL, -- 组织机构代码
	other_name varchar(255) NULL, -- 规范化简称
	postal_code varchar(255) NULL, -- 邮政编码
	remarks varchar(255) NULL, -- 备注
	unified_social_credit_code varchar(255) NULL, -- 统一社会信用代码
	institution_level_code varchar(255) NULL, -- 机构/单位级别
	create_time timestamp NULL, -- 创建时间
	update_time timestamp NULL, -- 修改时间
	CONSTRAINT flw_zwdd_organization_pkey PRIMARY KEY (organization_code)
);
COMMENT ON TABLE hz_ads_prv.flw_zwdd_organization IS '浙政钉组织机构数据';

-- Column comments

COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.organization_code IS '组织code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.organization_name IS '组织名称';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.display_order IS '排序';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.parent_code IS '父组织Code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.parent_name IS '父组织Name';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.status IS '组织状态';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.type_code IS '组织类型code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.type_name IS '组织类型名称';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.gmt_create IS '组织创建时间';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.responsible_employee_codes IS '负责人code，|拼接多个';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.business_strip_codes IS '条线codes,多个用|拼接';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.address IS '详细地址';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.contact_employee_code IS '联系人code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.contact_number IS '联系人号码';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.division_code IS '行政区划code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.short_name IS '组织全称';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.institution_code IS '组织机构代码';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.other_name IS '规范化简称';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.postal_code IS '邮政编码';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.remarks IS '备注';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.unified_social_credit_code IS '统一社会信用代码';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.institution_level_code IS '机构/单位级别';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.create_time IS '创建时间';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_organization.update_time IS '修改时间';


-- hz_ads_prv.flw_zwdd_user definition

-- Drop table

-- DROP TABLE hz_ads_prv.flw_zwdd_user;

CREATE TABLE hz_ads_prv.flw_zwdd_user (
	employee_code varchar(255) NOT NULL, -- 员工Code
	employee_name varchar(255) NULL, -- 员工名称
	gmt_create timestamp NULL, -- 员工创建时间
	emp_gender varchar(255) NULL, -- 员工性别(男性 1 女性 2 未说明的性别 9 未知的性别 0)
	emp_political_status_code varchar(255) NULL, -- 政治面貌
	emp_job_level_code varchar(255) NULL, -- 职级
	emp_budgeted_post_code varchar(255) NULL, -- 编制
	status varchar(255) NULL, -- 人员状态，A 在职； F 离职
	gov_employee_positions jsonb NULL, -- 组织范围下的任职信息
	create_time timestamp NULL, -- 创建时间
	update_time timestamp NULL, -- 修改时间
	gov_emp_pos_phone_no varchar(255) NULL, -- 办公号码
	organization_code varchar(255) NULL, -- 组织code
	gov_emp_pos_job varchar(255) NULL, -- 职位
	organization_name varchar(255) NULL, -- 组织name
	CONSTRAINT flw_zwdd_user_pkey PRIMARY KEY (employee_code)
);
COMMENT ON TABLE hz_ads_prv.flw_zwdd_user IS '浙政钉用户表';

-- Column comments

COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.employee_code IS '员工Code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.employee_name IS '员工名称';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.gmt_create IS '员工创建时间';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.emp_gender IS '员工性别(男性 1 女性 2 未说明的性别 9 未知的性别 0)';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.emp_political_status_code IS '政治面貌';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.emp_job_level_code IS '职级';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.emp_budgeted_post_code IS '编制';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.status IS '人员状态，A 在职； F 离职';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.gov_employee_positions IS '组织范围下的任职信息';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.create_time IS '创建时间';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.update_time IS '修改时间';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.gov_emp_pos_phone_no IS '办公号码';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.organization_code IS '组织code';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.gov_emp_pos_job IS '职位';
COMMENT ON COLUMN hz_ads_prv.flw_zwdd_user.organization_name IS '组织name';