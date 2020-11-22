CREATE TABLE `distributed_transcations` (
  `id` varchar(36) NOT NULL,
  `classname` varchar(255) DEFAULT NULL,
  `methodname` varchar(255) DEFAULT NULL,
  `failreason` varchar(4096) DEFAULT NULL,
  `lastretrytime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `inserttimeforhis` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `operatetimeforhis` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `paramcontents` varchar(1024) DEFAULT NULL,
  `paramclass` varchar(512) DEFAULT NULL,
  `retrycount` int(1) DEFAULT NULL,
  `validstatus` varchar(1) DEFAULT NULL,
  `beanname` varchar(255) DEFAULT NULL,
  `threadid` varchar(255) DEFAULT NULL,
  `extradata` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
