SELECT id, data_id, MD5(content) as content_md5, SUBSTRING(content, 1, 500) as content_preview 
FROM `ry-config`.config_info 
WHERE data_id = 'ruoyi-job-dev.yml';
