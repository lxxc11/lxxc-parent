-- ----------------------------
-- Records of tb_name
-- ----------------------------
BEGIN;
-- ----------------------------
-- Records of tb_user
-- ----------------------------

INSERT INTO "sys_user" ("id", "user_name", "real_name", "password", "salt", "sex", "status", "contact", "email", "effective_time", "password_update_time", "history_password", "public_key", "private_key", "create_by", "create_time", "update_by", "update_time", "del_flag", "position", "super_flag", "head_picture") VALUES ('ffc097d21ef85a38eab847c9bdf6cc86', 'admin', 'BCwL38RA44iKtbiVB2VvdQLEoYjyQQCYRWBdKf5xEy+40nqnr468uEv5mtIo6fcSH7xi4T83MdApvGdJSQpat7rFxiIJS1DzH4wjZP6OgBl8HNHq0yUjzQu8N/CGaETqrhMIHFlX77+d3w==', '116d9714e089d937e577e1507bd9415a', 'onX3k#3j', 0, 't', 'BEs23CobKVDbmlDdEH8NTMU1vgks3A55pHQtyNp82IeGWR0Kf5b5fbrbI2Hn4gkCMEQPPk2HDwQLTUWnL2hFRKDapuHLHaIioa+1ms2IT6SWi2wO+Vh2Wx2SMPddrZBdG90F7STx//R6GmE8', 'BODjO4znYIkqWsDQbqx5corL5JRLjV/yw+yrOfH/+/Im7O9Ss6GdKROqc2O8L8uVF/3ae6ApJfMyt/ah8iY5hVCDQd7TjiPsW/wd2kQ3EuiAG6DHOvpktrO+Pw1fpDymHl8TZVU08s5uvusJuttpzAxZug==', '2030-01-01 00:00:00', '2023-05-08 14:52:34', '116d9714e089d937e577e1507bd9415a,999d466ef3208d0607db40a442572b10,1c3c4acbef1688cc8a5288c2e66d8dfa,63855b93bdb783608420f7156fa2b5c9,116d9714e089d937e577e1507bd9415a', 'MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEDWC6N+/Jb45y5hNAhv40a/ZQjhTqslu3pVP32x8QvyBHna/7WwQDfF9PCn9Zix4RS8S1iv+PNlXQUgxlhLfFZg==', 'MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgQlz/BMPcD0crwkTRIHTnV7Q/TXjmI9NiYM2WE83izIGgCgYIKoEcz1UBgi2hRANCAAQNYLo378lvjnLmE0CG/jRr9lCOFOqyW7elU/fbHxC/IEedr/tbBAN8X08Kf1mLHhFLxLWK/482VdBSDGWEt8Vm', '6ed2fca284854290bd279f6bb5afee38', '2023-04-19 21:55:32', '6ed2fca284854290bd279f6bb5afee38', '2023-04-19 21:59:13', 'f', 'BIT0hJ7WEzWF0nO11uVPvnZnPAd1/skxOzrjg0AefqokMMLGeSyPrmXiW/wkc8AzamKe80XaQE6e4H2c0z1klQykqbvT7+XPs0CiAhp4kS2W5i0POpV6zJOxE/J4mS0fABRAgNBQEg==', 't', 'https://s3.huoshi.cloud/baoding/测试图片_1683528833362.png');

-- INSERT INTO "sys_platform" ("id", "system_name", "system_en_name", "url", "domain_name", "logo", "status", "create_by", "create_time", "update_by", "update_time", "del_flag", "sort_order", "super_power") VALUES (md5(random()::varchar), '用户中心', 'user-center', NULL, NULL, NULL, 't', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', -1, 'f');
INSERT INTO "sys_platform" ("id", "system_name", "system_en_name", "url", "domain_name", "logo", "status", "create_by", "create_time", "update_by", "update_time", "del_flag", "sort_order", "super_power") VALUES ('2ad5fba4a6ba6c36f7a02b5d13e658f3', '用户中心', 'user-center', NULL, NULL, NULL, 't', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', -1, 'f');
INSERT INTO "sys_menu" ("id", "parent_id", "platform_id", "menu_name", "menu_en_name", "type", "url", "sort_order", "status", "icon", "create_by", "create_time", "update_by", "update_time", "del_flag", "other_data") VALUES ('eff9f1bd2867a6b2a8bcd34da87ba302', '0', '2ad5fba4a6ba6c36f7a02b5d13e658f3', '用户管理', 'um', 1, '/user/list', 1, 't', '', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', NULL);
INSERT INTO "sys_menu" ("id", "parent_id", "platform_id", "menu_name", "menu_en_name", "type", "url", "sort_order", "status", "icon", "create_by", "create_time", "update_by", "update_time", "del_flag", "other_data") VALUES ('dea1171ebdd0cb89a58db08eeabc8221', '0', '2ad5fba4a6ba6c36f7a02b5d13e658f3', '角色管理', 'rm', 1, '/role/list', 2, 't', '', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', NULL);
INSERT INTO "sys_menu" ("id", "parent_id", "platform_id", "menu_name", "menu_en_name", "type", "url", "sort_order", "status", "icon", "create_by", "create_time", "update_by", "update_time", "del_flag", "other_data") VALUES ('675a04cb7f79b4c4fb0805006b9d1e64', '0', '2ad5fba4a6ba6c36f7a02b5d13e658f3', '部门管理', 'dm', 1, '/department/list', 3, 't', '', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', NULL);
INSERT INTO "sys_menu" ("id", "parent_id", "platform_id", "menu_name", "menu_en_name", "type", "url", "sort_order", "status", "icon", "create_by", "create_time", "update_by", "update_time", "del_flag", "other_data") VALUES ('cc0e3d149b04cd4afb58c686f5afc1ca', '0', '2ad5fba4a6ba6c36f7a02b5d13e658f3', '菜单管理', 'mm', 1, '/menu/list', 4, 't', '', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', NULL);
INSERT INTO "sys_menu" ("id", "parent_id", "platform_id", "menu_name", "menu_en_name", "type", "url", "sort_order", "status", "icon", "create_by", "create_time", "update_by", "update_time", "del_flag", "other_data") VALUES ('84aa10a70f493b682c1f324ae3aa06fb', '0', '2ad5fba4a6ba6c36f7a02b5d13e658f3', '字典管理', 'wm', 1, '/wordbook/list', 1, 't', '', 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'f', NULL);

COMMIT;


-- <------------------------------------------
-- 2.1.1-SNAPSHOT版本新增语句
INSERT INTO sys_function (id, menu_id, function_name, url, status, icon, create_by, create_time, update_by, update_time, del_flag, function_tag) VALUES(md5(random()::varchar), 'eff9f1bd2867a6b2a8bcd34da87ba302', '新增', NULL, true, NULL, 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), false, 'user:add');
INSERT INTO sys_function (id, menu_id, function_name, url, status, icon, create_by, create_time, update_by, update_time, del_flag, function_tag) VALUES(md5(random()::varchar), 'eff9f1bd2867a6b2a8bcd34da87ba302', '删除', NULL, true, NULL, 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), false, 'user:del');
INSERT INTO sys_function (id, menu_id, function_name, url, status, icon, create_by, create_time, update_by, update_time, del_flag, function_tag) VALUES(md5(random()::varchar), 'eff9f1bd2867a6b2a8bcd34da87ba302', '查询', NULL, true, NULL, 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), false, 'user:query');
INSERT INTO sys_function (id, menu_id, function_name, url, status, icon, create_by, create_time, update_by, update_time, del_flag, function_tag) VALUES(md5(random()::varchar), 'eff9f1bd2867a6b2a8bcd34da87ba302', '修改', NULL, true, NULL, 'ffc097d21ef85a38eab847c9bdf6cc86', now(), 'ffc097d21ef85a38eab847c9bdf6cc86', now(), false, 'user:update');

-- >------------------------------------------

