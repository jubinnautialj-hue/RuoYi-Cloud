-- ----------------------------
-- Flowable工作流菜单和权限SQL脚本
-- 数据库: ry-cloud
-- ----------------------------

-- 流程设计菜单 (一级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3000, '流程设计', 0, 2, 'flowable-design', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'form', 'admin', NOW(), '', NULL, '');

-- 流程模型管理 (流程设计下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3001, '流程模型管理', 3000, 1, 'flowable/model/index', 'flowable/model/index', NULL, '', 1, 0, 'C', '0', '0', 'flowable:model:list', 'list', 'admin', NOW(), '', NULL, '');

-- 流程模型管理按钮权限
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3002, '流程模型查询', 3001, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:model:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3003, '流程模型新增', 3001, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:model:add', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3004, '流程模型修改', 3001, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:model:edit', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3005, '流程模型删除', 3001, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:model:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3006, '流程模型部署', 3001, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:model:deploy', '#', 'admin', NOW(), '', NULL, '');

-- 流程定义管理 (流程设计下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3007, '流程定义管理', 3000, 2, 'flowable/definition/index', 'flowable/definition/index', NULL, '', 1, 0, 'C', '0', '0', 'flowable:definition:list', 'tree-table', 'admin', NOW(), '', NULL, '');

-- 流程定义管理按钮权限
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3008, '流程定义查询', 3007, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:definition:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3009, '流程定义部署', 3007, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:definition:deploy', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3010, '流程定义修改', 3007, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:definition:edit', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3011, '流程定义删除', 3007, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:definition:remove', '#', 'admin', NOW(), '', NULL, '');

-- 我的事务菜单 (一级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3100, '我的事务', 0, 3, 'flowable-task', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'message', 'admin', NOW(), '', NULL, '');

-- 待办任务 (我的事务下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3101, '待办任务', 3100, 1, 'flowable/task/todo', 'flowable/task/todo', NULL, '', 1, 0, 'C', '0', '0', 'flowable:task:todolist', 'edit', 'admin', NOW(), '', NULL, '');

-- 待办任务按钮权限
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3102, '任务查询', 3101, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:task:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3103, '任务认领', 3101, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:task:claim', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3104, '任务取消认领', 3101, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:task:unclaim', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3105, '任务办理', 3101, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:task:complete', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3106, '任务委派', 3101, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:task:delegate', '#', 'admin', NOW(), '', NULL, '');

-- 已办任务 (我的事务下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3110, '已办任务', 3100, 2, 'flowable/task/done', 'flowable/task/done', NULL, '', 1, 0, 'C', '0', '0', 'flowable:task:donelist', 'documentation', 'admin', NOW(), '', NULL, '');

-- 我发起的 (我的事务下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3120, '我发起的', 3100, 3, 'flowable/task/myProcess', 'flowable/task/myProcess', NULL, '', 1, 0, 'C', '0', '0', 'flowable:task:myProcess', 'people', 'admin', NOW(), '', NULL, '');

-- 流程运维菜单 (一级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3200, '流程运维', 0, 4, 'flowable-operation', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'monitor', 'admin', NOW(), '', NULL, '');

-- 运行中实例 (流程运维下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3201, '运行中实例', 3200, 1, 'flowable/instance/running', 'flowable/instance/running', NULL, '', 1, 0, 'C', '0', '0', 'flowable:instance:runningList', 'guide', 'admin', NOW(), '', NULL, '');

-- 运行中实例按钮权限
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3202, '实例查询', 3201, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:instance:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3203, '实例修改', 3201, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:instance:edit', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3204, '实例删除', 3201, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'flowable:instance:remove', '#', 'admin', NOW(), '', NULL, '');

-- 历史实例 (流程运维下的二级菜单)
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3210, '历史实例', 3200, 2, 'flowable/instance/history', 'flowable/instance/history', NULL, '', 1, 0, 'C', '0', '0', 'flowable:instance:historyList', 'log', 'admin', NOW(), '', NULL, '');
