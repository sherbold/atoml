CREATE DATABASE `atoml` /*!40100 DEFAULT CHARACTER SET latin1 */;

CREATE TABLE `results_morphtests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `algorithm` varchar(200) NOT NULL,
  `parameters` varchar(2000) DEFAULT NULL,
  `testcase` varchar(200) DEFAULT NULL,
  `iteration` int(11) DEFAULT NULL,
  `testsize` int(11) DEFAULT NULL,
  `deviations_counts` int(11) DEFAULT NULL,
  `deviations_scores` int(11) DEFAULT NULL,
  `pval_counts` double DEFAULT NULL,
  `pval_scores` double DEFAULT NULL,
  `passed_exact_score` tinyint(1) DEFAULT NULL,
  `passed_exact_class` tinyint(1) DEFAULT NULL,
  `passed_stat_class` tinyint(1) DEFAULT NULL,
  `passed_stat_score` tinyint(1) DEFAULT NULL,
  `passed` tinyint(1) DEFAULT NULL,
  `exception` varchar(200) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `stacktrace` longtext DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26590 DEFAULT CHARSET=latin1;

CREATE TABLE `results_smoketests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `algorithm` varchar(200) NOT NULL,
  `parameters` varchar(2000) DEFAULT NULL,
  `testcase` varchar(200) DEFAULT NULL,
  `iteration` int(11) DEFAULT NULL,
  `passed` tinyint(1) DEFAULT NULL,
  `exception` varchar(200) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `stacktrace` longtext DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25514 DEFAULT CHARSET=latin1;


CREATE VIEW `morphtest_view` AS select
  `results_morphtests`.`id` AS `id`,
  case when `results_morphtests`.`algorithm` like 'test%' then substr(`results_morphtests`.`algorithm`,locate('_',`results_morphtests`.`algorithm`) + 1,char_length(`results_morphtests`.`algorithm`) - locate('_',reverse(`results_morphtests`.`algorithm`)) - locate('_',`results_morphtests`.`algorithm`)) else substring_index(`results_morphtests`.`algorithm`,'_',1) end AS `framework`,
  case when `results_morphtests`.`algorithm` like 'test%' then right(`results_morphtests`.`algorithm`,locate('_',reverse(`results_morphtests`.`algorithm`)) - 1) else substr(`results_morphtests`.`algorithm`,locate('_',`results_morphtests`.`algorithm`) + 1,char_length(`results_morphtests`.`algorithm`) - locate('_',reverse(`results_morphtests`.`algorithm`)) - locate('_',`results_morphtests`.`algorithm`)) end AS `algorithm`,
  `results_morphtests`.`parameters` AS `parameters`,
  substring_index(substring_index(`results_morphtests`.`testcase`,'_',-2),'_',1) AS `testcase`,
  substring_index(`results_morphtests`.`testcase`,'_',-1) AS `dataset`,`results_morphtests`.`iteration` AS `iteration`,
  `results_morphtests`.`testsize` AS `testsize`,
  `results_morphtests`.`deviations_counts` AS `deviations_counts`,
  `results_morphtests`.`deviations_scores` AS `deviations_scores`,
  `results_morphtests`.`pval_counts` AS `pval_counts`,
  `results_morphtests`.`pval_scores` AS `pval_scores`,
  `results_morphtests`.`passed_exact_score` AS `passed_exact_score`,
  `results_morphtests`.`passed_exact_class` AS `passed_exact_class`,
  `results_morphtests`.`passed_stat_class` AS `passed_stat_class`,
  `results_morphtests`.`passed_stat_score` AS `passed_stat_score`,
  `results_morphtests`.`passed` AS `passed`,
  `results_morphtests`.`exception` AS `exception`,
  `results_morphtests`.`message` AS `message`,
  `results_morphtests`.`stacktrace` AS `stacktrace`
from `results_morphtests`;

CREATE VIEW `smoketest_view` AS select
`results_smoketests`.`id` AS `id`,
  case when `results_smoketests`.`algorithm` like 'test%' then substr(`results_smoketests`.`algorithm`,locate('_',`results_smoketests`.`algorithm`) + 1,char_length(`results_smoketests`.`algorithm`) - locate('_',reverse(`results_smoketests`.`algorithm`)) - locate('_',`results_smoketests`.`algorithm`)) else substring_index(`results_smoketests`.`algorithm`,'_',1) end AS `framework`,
  case when `results_smoketests`.`algorithm` like 'test%' then right(`results_smoketests`.`algorithm`,locate('_',reverse(`results_smoketests`.`algorithm`)) - 1) else substr(`results_smoketests`.`algorithm`,locate('_',`results_smoketests`.`algorithm`) + 1,char_length(`results_smoketests`.`algorithm`) - locate('_',reverse(`results_smoketests`.`algorithm`)) - locate('_',`results_smoketests`.`algorithm`)) end AS `algorithm`,
  `results_smoketests`.`parameters` AS `parameters`,
  substring_index(`results_smoketests`.`testcase`,'_',-1) AS `testcase`,
  `results_smoketests`.`iteration` AS `iteration`,
  `results_smoketests`.`passed` AS `passed`,
  `results_smoketests`.`exception` AS `exception`,
  `results_smoketests`.`message` AS `message`,
  `results_smoketests`.`stacktrace` AS `stacktrace`
from `results_smoketests`;
