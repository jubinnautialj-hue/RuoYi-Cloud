SELECT id, data_id, group_id, tenant_id, gmt_create, gmt_modified, src_user, src_ip 
FROM `ry-config`.config_info 
WHERE data_id = 'ruoyi-job-dev.yml';
