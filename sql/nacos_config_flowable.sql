-- ============================================
-- Nacos 配置中心配置 - ruoyi-flowable 模块
-- 需要在 ry-config 数据库中执行
-- ============================================

USE `ry-config`;

-- 1. 添加 ruoyi-flowable-dev.yml 配置
-- 注意：请根据实际环境修改数据库连接、Redis密码等配置
INSERT INTO `config_info` (`data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) 
VALUES (
    'ruoyi-flowable-dev.yml',
    'DEFAULT_GROUP',
    '# spring配置
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: jingneng2023
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: ruoyi
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        connectTimeout: 30000
        socketTimeout: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\=true;druid.stat.slowSqlMillis\=5000
      datasource:
          # 主库数据源（业务数据）
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: root
          # Flowable工作流数据源
          flowable:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/ry-flowable?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: root

# Flowable配置
flowable:
  database-schema-update: true
  history-level: full
  db-history-used: true
  async-executor-activate: true
  idm:
    enabled: false

# mybatis-plus配置
mybatis-plus:
  type-aliases-package: com.ruoyi.flowable.domain
  mapper-locations: classpath:mapper/**/*.xml

# springdoc配置
springdoc:
  gatewayUrl: http://localhost:8081/${spring.application.name}
  api-docs:
    enabled: true
  info:
    title: ''工作流模块接口文档''
    description: ''工作流模块接口描述''
    contact:
      name: RuoYi
      url: https://ruoyi.vip
',
    MD5('# spring配置\nspring:\n  data:\n    redis:\n      host: 127.0.0.1\n      port: 6379\n      password: jingneng2023\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: root\n          flowable:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/ry-flowable?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: root\n\n# Flowable配置\nflowable:\n  database-schema-update: true\n  history-level: full\n  db-history-used: true\n  async-executor-activate: true\n  idm:\n    enabled: false\n\n# mybatis-plus配置\nmybatis-plus:\n  type-aliases-package: com.ruoyi.flowable.domain\n  mapper-locations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8081/${spring.application.name}\n  api-docs:\n    enabled: true\n  info:\n    title: ''工作流模块接口文档''\n    description: ''工作流模块接口描述''\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n'),
    NOW(),
    NOW(),
    'nacos',
    '127.0.0.1',
    '',
    '',
    '工作流模块',
    'null',
    'null',
    'yaml',
    '',
    ''
);

-- 2. 更新 sentinel-ruoyi-gateway 添加 ruoyi-flowable 限流策略
-- 注意：如果 sentinel-ruoyi-gateway 配置不存在，请先创建
INSERT INTO `config_info` (`data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`)
SELECT 
    'sentinel-ruoyi-gateway',
    'DEFAULT_GROUP',
    '[\n    {\n        \"resource\": \"ruoyi-auth\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-system\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-gen\",\n        \"count\": 200,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-file\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-application\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-flowable\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]',
    MD5('[\n    {\n        \"resource\": \"ruoyi-auth\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-system\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-gen\",\n        \"count\": 200,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-file\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-application\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"ruoyi-flowable\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]'),
    NOW(),
    NOW(),
    'nacos',
    '127.0.0.1',
    '',
    '',
    'Gateway限流配置',
    'null',
    'null',
    'json',
    '',
    ''
WHERE NOT EXISTS (SELECT 1 FROM `config_info` WHERE `data_id` = 'sentinel-ruoyi-gateway');
