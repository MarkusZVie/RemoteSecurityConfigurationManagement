CREATE TABLE `applicants` (
	`applicant_id` int(11) NOT NULL AUTO_INCREMENT,
	`applicant_firstname` varchar(255),
	`applicant_lastname` varchar(255),
	`applicant_name` varchar(255),
	`applicant_password` varchar(255),
	`applicant_email` varchar(255),
	PRIMARY KEY (`applicant_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `roles` (
	`role_id` int(11) NOT NULL AUTO_INCREMENT,
	`role_name` varchar(255),
	`role_description` varchar(255),
	PRIMARY KEY (`role_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `has_role_applicant` (
  `applicant_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`applicant_id`,`role_id`),
  CONSTRAINT `has_role_applicant_fkapplicant` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `has_role_applicant_fkrole` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `applicantgroups` (
	`applicantgroup_id` int(11) NOT NULL AUTO_INCREMENT,
	`applicantgroup_name` varchar(255),
	`applicantgroup_description` varchar(255),
	PRIMARY KEY (`applicantgroup_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `has_applicantgroup_applicant` (
  `applicant_id` int(11) NOT NULL,
  `applicantgroup_id` int(11) NOT NULL,
  PRIMARY KEY (`applicant_id`,`applicantgroup_id`),
  CONSTRAINT `has_applicantgroup_fkapplicant` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `has_applicantgroup_fkapplicantgroup` FOREIGN KEY (`applicantgroup_id`) REFERENCES `applicantgroups` (`applicantgroup_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `jobs` (
	`job_id` int(11) NOT NULL AUTO_INCREMENT,
	`job_name` varchar(255),
	`job_description` varchar(255),
	PRIMARY KEY (`job_id`)
)DEFAULT CHARSET=utf8;


CREATE TABLE `has_applicant_job` (
  `applicant_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  PRIMARY KEY (`applicant_id`,`job_id`),
  CONSTRAINT `has_applicant_fk_applicant` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `has_applicant_fk_job` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `rscmclients` (
	`rscmclient_id` int(11) NOT NULL AUTO_INCREMENT,
	`applikation_key` varchar(255),
	`clientrsapublic_key` varchar(512),
	`created_on` datetime,
	`client_keypass` varchar(255),
	`client_port` varchar(255),
	`key_creation_date` datetime,
	`rscm_keypass` varchar(255),
	`rscm_password` varchar(255),
	`is_active` bit(1),
	PRIMARY KEY (`rscmclient_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `has_applicant_rscmclient` (
  `applicant_id` int(11) NOT NULL,
  `rscmclient_id` int(11) NOT NULL,
  PRIMARY KEY (`applicant_id`,`rscmclient_id`),
  CONSTRAINT `has_applicant_fk_applicant` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `has_applicant_fk_rscmclient` FOREIGN KEY (`rscmclient_id`) REFERENCES `rscmclients` (`rscmclient_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `environments` (
	`environment_id` int(11) NOT NULL AUTO_INCREMENT,
	`ip_range_begin` varchar(255),
	`ip_range_end` varchar(255),
	`environment_description` varchar(255),
	PRIMARY KEY (`environment_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `environmentthreats` (
	`environmentthreat_id` int(11) NOT NULL AUTO_INCREMENT,
	`threat_level` int(11),
	`threat_title` varchar(255),
	`threat_description` varchar(255),
	`expected_problem` varchar(255),
	PRIMARY KEY (`environmentthreat_id`)
)DEFAULT CHARSET=utf8;


CREATE TABLE `has_environment_environmentthreat` (
  `environment_id` int(11) NOT NULL,
  `environmentthreat_id` int(11) NOT NULL,
  PRIMARY KEY (`environmentthreat_id`,`environment_id`),
  CONSTRAINT `has_environment_environmentthreat_fk_environmentthreat` FOREIGN KEY (`environmentthreat_id`) REFERENCES `environmentthreats` (`environmentthreat_id`),
  CONSTRAINT `has_environment_environmentthreat_fk_environments` FOREIGN KEY (`environment_id`) REFERENCES `environments` (`environment_id`)
)DEFAULT CHARSET=utf8;


CREATE TABLE `rscmclient_connections` (
	`connection_id` int(11) NOT NULL AUTO_INCREMENT,
	`rscmclient_fs` int(11),
	`environment_fs` int(11),
	`connection_start` datetime,
	`connection_end` datetime,
	`connection_description` varchar(255),
	`connection_exitcode` varchar(255),
	`connection_supplement` varchar(255),
	PRIMARY KEY (`connection_id`),
	CONSTRAINT `rscmclient_connections_fk_rscmclient` FOREIGN KEY (`rscmclient_fs`) REFERENCES `rscmclients` (`rscmclient_id`),
	CONSTRAINT `rscmclient_connections_fk_environment` FOREIGN KEY (`environment_fs`) REFERENCES `environments` (`environment_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `tasks` (
	`task_id` int(11) NOT NULL AUTO_INCREMENT,
	`task_name` varchar(255),
	`task_creationdate` datetime,
	`task_plan_begindate` datetime,
	`task_plan_enddate` datetime,
	`task_actual_begindate` datetime,
	`task_actual_enddate` datetime,
	`task_description` varchar(255),
	`task_outcome` varchar(255),
	PRIMARY KEY (`task_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `has_applicant_task` (
	`task_id` int(11) NOT NULL,
	`applicant_id` int(11) NOT NULL,
	PRIMARY KEY (`task_id`,`applicant_id`),
	CONSTRAINT `has_applicant_task_fk_task` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`),
	CONSTRAINT `has_applicant_task_fk_applicant` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`applicant_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `scriptexecution` (
	`scriptexecution_id` int(11) NOT NULL AUTO_INCREMENT,
	`script_Name` varchar(255),
	`scriptexecution_executiondate` datetime,
	`rscmclient_fs` int(11) NOT NULL,
	`rscmclient_fs` int(11) NOT NULL,
	`task_fs` int(11),
	`environment_fs` int(11),
	`environmentthreat_fs` int(11),
	`job_fs` int(11),
	`applicantgroup_fs` int(11),
	`applicant_fs` int(11),
	`role_fs` int(11),
	PRIMARY KEY (`scriptexecution_id`),
	CONSTRAINT `scriptexecution_fk_rscmclient` FOREIGN KEY (`rscmclient_fs`) REFERENCES `rscmclients` (`rscmclient_id`),
	CONSTRAINT `scriptexecution_fk_task` FOREIGN KEY (`task_fs`) REFERENCES `tasks` (`task_id`),
	CONSTRAINT `scriptexecution_fk_environment` FOREIGN KEY (`environment_fs`) REFERENCES `environments` (`environment_id`),
	CONSTRAINT `scriptexecution_fk_environmentthreat` FOREIGN KEY (`environmentthreat_fs`) REFERENCES `environmentthreats` (`environmentthreat_id`),
	CONSTRAINT `scriptexecution_fk_job` FOREIGN KEY (`job_fs`) REFERENCES `jobs` (`job_id`),
	CONSTRAINT `scriptexecution_fk_applicantgroup` FOREIGN KEY (`applicantgroup_fs`) REFERENCES `applicantgroups` (`applicantgroup_id`),
	CONSTRAINT `scriptexecution_fk_applicant` FOREIGN KEY (`applicant_fs`) REFERENCES `applicants` (`applicant_id`),
	CONSTRAINT `scriptexecution_fk_role` FOREIGN KEY (`role_fs`) REFERENCES `roles` (`role_id`)
)DEFAULT CHARSET=utf8;

