-- ----------------------------
-- Flowable工作流数据库脚本
-- 数据库: ry-flowable
-- 注意: Flowable启动时会自动创建所需的表，此脚本主要用于创建数据库和配置
-- ----------------------------

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `ry-flowable` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- 使用数据库
USE `ry-flowable`;

-- ----------------------------
-- Flowable自动创建的表说明 (启动时自动创建):
-- ----------------------------
-- 1. ACT_RE_*: 流程定义和部署相关表
--    - ACT_RE_DEPLOYMENT: 部署表
--    - ACT_RE_PROCDEF: 流程定义表
--    - ACT_RE_MODEL: 模型表
--
-- 2. ACT_RU_*: 运行时相关表
--    - ACT_RU_EXECUTION: 执行实例表
--    - ACT_RU_TASK: 任务表
--    - ACT_RU_VARIABLE: 变量表
--    - ACT_RU_IDENTITYLINK: 身份联系表
--    - ACT_RU_JOB: 作业表
--
-- 3. ACT_HI_*: 历史相关表
--    - ACT_HI_PROCINST: 历史流程实例表
--    - ACT_HI_ACTINST: 历史活动实例表
--    - ACT_HI_TASKINST: 历史任务实例表
--    - ACT_HI_VARINST: 历史变量实例表
--    - ACT_HI_DETAIL: 历史详情表
--    - ACT_HI_COMMENT: 历史评论表
--    - ACT_HI_ATTACHMENT: 历史附件表
--
-- 4. ACT_GE_*: 通用表
--    - ACT_GE_BYTEARRAY: 字节数组表
--    - ACT_GE_PROPERTY: 属性表
--
-- 5. ACT_ID_*: 身份管理表 (默认禁用，使用RuoYi用户体系)
--    - ACT_ID_USER: 用户表
--    - ACT_ID_GROUP: 用户组表
--    - ACT_ID_MEMBERSHIP: 成员关系表
--    - ACT_ID_INFO: 用户信息表
-- ----------------------------

-- 注意:
-- 1. Flowable会在首次启动时自动创建以上所有表
-- 2. 如果需要手动初始化，可以执行Flowable官方提供的create脚本
-- 3. 数据库连接信息需要配置在Nacos配置中心或application.yml中
