DROP TABLE IF EXISTS `leaf_node`;

CREATE TABLE `leaf_node`
(
    `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `system_id`      varchar(20)   NOT NULL ,
    `group_id`      int(11)   NOT NULL ,
    `biz_tag`     varchar(128) NOT NULL DEFAULT '',
    `max_id`      bigint(20)   NOT NULL DEFAULT '1',
    `fill_zero`      int(4)   NOT NULL DEFAULT '1',
    `step`        int(11)      NOT NULL,
    `description` varchar(256)          DEFAULT NULL,
    `enable_flag`   tinyint(1) unsigned NOT NULL COMMENT '状态：0停用，1启用',
    `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_systemid_groupid_biztag` (`system_id`,`group_id`,`biz_tag`)
) ENGINE = InnoDB;