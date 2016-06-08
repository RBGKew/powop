DROP TABLE IF EXISTS `acl_class`;


CREATE TABLE `acl_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_class_idx` (`class`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `acl_entry`;


CREATE TABLE `acl_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `acl_object_identity` bigint(20) NOT NULL,
  `ace_order` int(11) NOT NULL,
  `sid` bigint(20) NOT NULL,
  `mask` int(11) NOT NULL,
  `granting` tinyint(4) NOT NULL,
  `audit_success` tinyint(4) NOT NULL,
  `audit_failure` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_entry_idx` (`acl_object_identity`,`ace_order`),
  KEY `acl_entry_sid_fk` (`sid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `acl_object_identity`;


CREATE TABLE `acl_object_identity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `object_id_class` bigint(20) NOT NULL,
  `object_id_identity` bigint(20) NOT NULL,
  `parent_object` bigint(20) DEFAULT NULL,
  `owner_sid` bigint(20) NOT NULL,
  `entries_inheriting` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_object_identity_idx` (`object_id_class`,`object_id_identity`),
  KEY `acl_object_identity_object_identity_fk` (`parent_object`),
  KEY `acl_object_identity_sid_fk` (`owner_sid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `acl_sid`;


CREATE TABLE `acl_sid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` tinyint(4) NOT NULL,
  `sid` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_sid_idx` (`sid`,`principal`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `annotation`;


CREATE TABLE `annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `annotatedObjId` bigint(20) DEFAULT NULL,
  `annotatedObjType` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  `text` longtext,
  `dateTime` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `recordType` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Annotation_annotatedObjId_IDX` (`annotatedObjId`),
  KEY `Annotation_annotatedObjType_IDX` (`annotatedObjType`),
  KEY `Annotation_jobId_IDX` (`jobId`),
  KEY `FK1A21C74F92954226` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_execution`;


CREATE TABLE `batch_job_execution` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  UNIQUE KEY `JOB_EXECUTION_IDX` (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_IDX` (`JOB_INSTANCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_execution_context`;


CREATE TABLE `batch_job_execution_context` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` longtext,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  UNIQUE KEY `JOB_EXECUTION_CONTEXT_IDX` (`JOB_EXECUTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_execution_params`;


CREATE TABLE `batch_job_execution_params` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_execution_seq`;


CREATE TABLE `batch_job_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_instance`;


CREATE TABLE `batch_job_instance` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INSTANCE_IDX` (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_IDX` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_params`;


CREATE TABLE `batch_job_params` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` longtext,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  KEY `JOB_INST_PARAMS_IDX` (`JOB_INSTANCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_job_seq`;


CREATE TABLE `batch_job_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_step_execution`;


CREATE TABLE `batch_step_execution` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  UNIQUE KEY `STEP_EXECUTION_IDX` (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_IDX` (`JOB_EXECUTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_step_execution_context`;


CREATE TABLE `batch_step_execution_context` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` longtext,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  UNIQUE KEY `STEP_EXECUTION_CONTEXT_IDX` (`STEP_EXECUTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `batch_step_execution_seq`;


CREATE TABLE `batch_step_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `comment`;


CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL,
  `identifier` varchar(255) NOT NULL,
  `aboutData_id` bigint(20) DEFAULT NULL,
  `aboutData_type` varchar(255) DEFAULT NULL,
  `comment` longtext,
  `created` datetime DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `commentPage_id` bigint(20) DEFAULT NULL,
  `commentPage_type` varchar(255) DEFAULT NULL,
  `inResponseTo_id` bigint(20) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `Comment_User_FK` (`user_id`),
  KEY `Comment_aboutData_id_IDX` (`aboutData_id`),
  KEY `Comment_aboutData_type_IDX` (`aboutData_type`),
  KEY `Comment_commentPage_id_IDX` (`commentPage_id`),
  KEY `Comment_commentPage_type_IDX` (`commentPage_type`),
  KEY `Comment_inResponseTo_id_IDX` (`inResponseTo_id`),
  KEY `FKC35AE4F16C64D29C` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `comment_alternativeidentifiers`;


CREATE TABLE `comment_alternativeidentifiers` (
  `Comment_id` bigint(20) NOT NULL,
  `alternativeIdentifiers` varchar(255) NOT NULL,
  `alternativeIdentifiers_KEY` varchar(255) NOT NULL,
  PRIMARY KEY (`Comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `concept`;


CREATE TABLE `concept` (
  `id` bigint(20) NOT NULL,
  `identifier` varchar(255) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source_id` bigint(20) DEFAULT NULL,
  `definition` longtext,
  `prefLabel` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `altLabel` varchar(255) DEFAULT NULL,
  `prefSymbol_id` bigint(20) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `Concept_authority_FK` (`authority_id`),
  KEY `Concept_prefSymbol_FK` (`prefSymbol_id`),
  KEY `Concept_source_FK` (`source_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `databasechangelog`;


CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `databasechangeloglock`;


CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `description`;


CREATE TABLE `description` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `description` longtext,
  `taxon_id` bigint(20) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `language` varchar(32) DEFAULT NULL,
  `contributor` varchar(255) DEFAULT NULL,
  `audience` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Description_identifier_IDX` (`identifier`),
  KEY `Content_taxon_id_IDX` (`taxon_id`),
  KEY `FK9BEFCC5992954226` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `description_reference`;


CREATE TABLE `description_reference` (
  `Description_id` bigint(20) NOT NULL DEFAULT '0',
  `references_id` bigint(20) NOT NULL,
  PRIMARY KEY (`Description_id`,`references_id`),
  KEY `Description_Reference_Description_id_IDX` (`Description_id`),
  KEY `Description_Reference_references_id_IDX` (`references_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `description_types`;

CREATE TABLE `description_types` (
  `Description_id` bigint(20) NOT NULL DEFAULT '0',
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`Description_id`),
  KEY `Description_Reference_Description_id_IDX` (`Description_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `distribution`; 

CREATE TABLE `distribution` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `locality` varchar(255) DEFAULT NULL,
  `occurrenceRemarks` varchar(255) DEFAULT NULL,
  `occurrenceStatus` varchar(255) DEFAULT NULL,
  `establishmentMeans` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Distribution_identifier_IDX` (`identifier`),
  KEY `Distribution_taxon_id_IDX` (`taxon_id`),
  KEY `FKAB93A2A492954226` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `distribution_reference`;


CREATE TABLE `distribution_reference` (
  `Distribution_id` bigint(20) NOT NULL,
  `references_id` bigint(20) NOT NULL,
  PRIMARY KEY (`Distribution_id`,`references_id`),
  KEY `Distribution_Reference_Distribution_id_IDX` (`Distribution_id`),
  KEY `Distribution_Reference_references_id_IDX` (`references_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_permissions`;


CREATE TABLE `group_permissions` (
  `Group_id` bigint(20) NOT NULL,
  `permissions` int(11) DEFAULT NULL,
  KEY `FK7A63C2A45090CB20` (`Group_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequences`;


CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `sequence_next_hi_value` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `identification`;


CREATE TABLE `identification` (
  `id` bigint(20) NOT NULL,
  `dateIdentified` datetime DEFAULT NULL,
  `identificationQualifier` varchar(255) DEFAULT NULL,
  `identificationReferences` varchar(255) DEFAULT NULL,
  `identificationRemarks` varchar(255) DEFAULT NULL,
  `identificationVerificationStatus` varchar(255) DEFAULT NULL,
  `identifiedBy` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `typeStatus` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `taxon_id_idx` (`taxon_id`),
  KEY `authority_id_idx` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `identificationkey`;


CREATE TABLE `identificationkey` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) NOT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `description` longtext,
  `title` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `matrix` longtext,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `audience` varchar(255) DEFAULT NULL,
  `contributor` varchar(255) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `references` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `IdentificationKey_Source_IDX` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `identifier`;


CREATE TABLE `identifier` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK165A88C9351368A7` (`taxon_id`),
  KEY `FK165A88C96B53D29C` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `image`;


CREATE TABLE `image` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `description` longtext,
  `format` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `locality` varchar(255) DEFAULT NULL,
  `location` geometry DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `contributor` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `audience` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `references` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `accessUri` varchar(255) DEFAULT NULL,
  `associatedObservationReference` varchar(255) DEFAULT NULL,
  `associatedSpecimenReference` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `countryCode` varchar(255) DEFAULT NULL,
  `countryName` varchar(255) DEFAULT NULL,
  `pixelXDimension` int(11) DEFAULT NULL,
  `pixelYDimension` int(11) DEFAULT NULL,
  `providerManagedId` varchar(255) DEFAULT NULL,
  `provinceState` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `subType` varchar(255) DEFAULT NULL,
  `subjectCategoryVocabulary` varchar(255) DEFAULT NULL,
  `sublocation` varchar(255) DEFAULT NULL,
  `taxonCoverage_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `worldRegion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Image_identifier_IDX` (`identifier`),
  KEY `FK437B93B92954226` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `image_subjectpart`;


CREATE TABLE `image_subjectpart` (
  `image_id` bigint(20) DEFAULT NULL,
  `subjectPart` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `logging_event`;


CREATE TABLE `logging_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` longtext NOT NULL,
  `logger_name` varchar(254) NOT NULL,
  `level_string` varchar(254) NOT NULL,
  `thread_name` varchar(254) DEFAULT NULL,
  `reference_flag` smallint(6) DEFAULT NULL,
  `arg0` varchar(254) DEFAULT NULL,
  `arg1` varchar(254) DEFAULT NULL,
  `arg2` varchar(254) DEFAULT NULL,
  `arg3` varchar(254) DEFAULT NULL,
  `caller_filename` varchar(254) NOT NULL,
  `caller_class` varchar(254) NOT NULL,
  `caller_method` varchar(254) NOT NULL,
  `caller_line` char(4) NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `logging_event_exception`;


CREATE TABLE `logging_event_exception` (
  `event_id` bigint(20) NOT NULL,
  `i` smallint(6) NOT NULL,
  `trace_line` varchar(254) NOT NULL,
  PRIMARY KEY (`event_id`,`i`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `logging_event_property`;


CREATE TABLE `logging_event_property` (
  `event_id` bigint(20) NOT NULL,
  `mapped_key` varchar(254) NOT NULL,
  `mapped_value` longtext,
  PRIMARY KEY (`event_id`,`mapped_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `measurementorfact`;


CREATE TABLE `measurementorfact` (
  `id` bigint(20) NOT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `measurementAccuracy` varchar(255) DEFAULT NULL,
  `measurementDeterminedBy` varchar(255) DEFAULT NULL,
  `measurementDeterminedDate` datetime DEFAULT NULL,
  `measurementMethod` varchar(255) DEFAULT NULL,
  `measurementRemarks` varchar(255) DEFAULT NULL,
  `measurementType` varchar(255) DEFAULT NULL,
  `measurementValue` varchar(255) DEFAULT NULL,
  `measurementUnit` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `bibliographicCitation` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `MeasurementOrFact_authority_FK` (`authority_id`),
  KEY `MeasurementOrFact_taxon_FK` (`taxon_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `organisation`;


CREATE TABLE `organisation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `bibliographicCitation` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `creatorEmail` varchar(255) DEFAULT NULL,
  `description` longtext,
  `logoUrl` varchar(255) DEFAULT NULL,
  `publisherName` varchar(255) DEFAULT NULL,
  `publisherEmail` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `commentsEmailedTo` varchar(255) DEFAULT NULL,
  `insertCommentsIntoScratchpad` bit(1) DEFAULT b'0',
  `footerLogoPosition` int(11) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Source_identifier_IDX` (`identifier`),
  KEY `FK1A21C74F92954237` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `phylogenetictree`;


CREATE TABLE `phylogenetictree` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) NOT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `description` longtext,
  `title` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `phylogeny` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `numberOfExternalNodes` bigint(20) DEFAULT NULL,
  `hasBranchLengths` bit(1) DEFAULT b'0',
  `bibliographicReference_id` bigint(20) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `references` varchar(255) DEFAULT NULL,
  `contributor` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `audience` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `PhylogeneticTree_Reference_FK` (`bibliographicReference_id`),
  KEY `PhylogeneticTree_authority_FK` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `phylogenetictree_taxon`;


CREATE TABLE `phylogenetictree_taxon` (
  `PhylogeneticTree_id` bigint(20) NOT NULL,
  `leaves_id` bigint(20) NOT NULL,
  KEY `PhylogeneticTree_Taxon_PhylogeneticTree_FK` (`PhylogeneticTree_id`),
  KEY `PhylogeneticTree_Taxon_leaves_FK` (`leaves_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `place`;


CREATE TABLE `place` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `fipsCode` varchar(5) DEFAULT NULL,
  `shape` geometry DEFAULT NULL,
  `point` geometry DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `mapFeatureId` bigint(20) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `principal`;


CREATE TABLE `principal` (
  `id` bigint(20) NOT NULL,
  `DTYPE` varchar(31) NOT NULL,
  `created` datetime DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `accountNonExpired` bit(1) DEFAULT NULL,
  `accountNonLocked` bit(1) DEFAULT NULL,
  `credentialsNonExpired` bit(1) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nonce` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `familyName` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `accountName` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `topicInterest` varchar(255) DEFAULT NULL,
  `homepage` varchar(255) DEFAULT NULL,
  `notifyByEmail` boolean NOT NULL DEFAULT 0,
  `apiKey` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  UNIQUE KEY `principal_accountname_uk` (`accountName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `qrtz_blob_triggers`;


CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_calendars`;


CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_cron_triggers`;


CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_fired_triggers`;


CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(20) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_job_details`;


CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_locks`;


CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;


CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_scheduler_state`;


CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(20) NOT NULL,
  `CHECKIN_INTERVAL` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_simple_triggers`;


CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(20) NOT NULL,
  `REPEAT_INTERVAL` bigint(20) NOT NULL,
  `TIMES_TRIGGERED` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;


CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `qrtz_triggers`;


CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(20) NOT NULL,
  `END_TIME` bigint(20) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(6) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `QRTZ_TRIGGERS_JOB_DETAILS_FK` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

DROP TABLE IF EXISTS `reference`;


CREATE TABLE `reference` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `bibliographicCitation` longtext,
  `authority_id` bigint(20) DEFAULT NULL,
  `description` longtext,
  `subject` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `language` varchar(32) DEFAULT NULL,
  `taxonRemarks` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Reference_identifier_IDX` (`identifier`),
  KEY `FK404D5F2B92954226` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `resource`;


CREATE TABLE `resource` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `exitDescription` longtext,
  `exitCode` varchar(255) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  `jobInstance` varchar(255) DEFAULT NULL,
  `resourceType` varchar(255) DEFAULT NULL,
  `lastHarvested` datetime DEFAULT NULL,
  `resource` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `organisation_id` bigint(20) DEFAULT NULL,
  `recordsRead` int(11) DEFAULT NULL,
  `processSkip` int(11) DEFAULT NULL,
  `written` int(11) DEFAULT NULL,
  `readSkip` int(11) DEFAULT NULL,
  `writeSkip` int(11) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `baseUrl` varchar(255) DEFAULT NULL,
  `scheduled` bit(1) DEFAULT NULL,
  `schedulingPeriod` varchar(255) DEFAULT NULL,
  `nextAvailableDate` datetime DEFAULT NULL,
  `lastHarvestedJobId` bigint(20) DEFAULT NULL,
  `lastAttempt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1239DCF3DA2C4` (`organisation_id`),
  KEY `Resource_jobId_IDX` (`jobId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `resource_parameters`;


CREATE TABLE `resource_parameters` (
  `Resource_id` bigint(20) NOT NULL DEFAULT '0',
  `parameters_KEY` varchar(255) NOT NULL,
  `parameters` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Resource_id`,`parameters_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon`;


CREATE TABLE `taxon` (
  `id` bigint(20) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `scientificName` varchar(255) DEFAULT NULL,
  `acceptedNameUsage_id` bigint(20) DEFAULT NULL,
  `parentNameUsage_id` bigint(20) DEFAULT NULL,
  `scientificNameAuthorship` varchar(255) DEFAULT NULL,
  `genus` varchar(255) DEFAULT NULL,
  `specificEpithet` varchar(255) DEFAULT NULL,
  `infraspecificEpithet` varchar(255) DEFAULT NULL,
  `taxonRank` varchar(255) DEFAULT NULL,
  `family` varchar(255) DEFAULT NULL,
  `subgenus` varchar(255) DEFAULT NULL,
  `kingdom` varchar(255) DEFAULT NULL,
  `nomenclaturalCode` varchar(255) DEFAULT NULL,
  `ordr` varchar(255) DEFAULT NULL,
  `phylum` varchar(255) DEFAULT NULL,
  `taxonomicStatus` varchar(255) DEFAULT NULL,
  `namePublishedIn_id` bigint(20) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `scientificNameID` varchar(255) DEFAULT NULL,
  `class` varchar(128) DEFAULT NULL,
  `subfamily` varchar(255) DEFAULT NULL,
  `tribe` varchar(255) DEFAULT NULL,
  `subtribe` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `bibliographicCitation` varchar(255) DEFAULT NULL,
  `namePublishedInString` varchar(255) DEFAULT NULL,
  `nameAccordingTo_id` bigint(20) DEFAULT NULL,
  `namePublishedInYear` int(11) DEFAULT NULL,
  `nomenclaturalStatus` varchar(255) DEFAULT NULL,
  `originalNameUsage_id` bigint(20) DEFAULT NULL,
  `taxonRemarks` varchar(255) DEFAULT NULL,
  `verbatimTaxonRank` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Taxon_identifier_IDX` (`identifier`),
  KEY `FK4CD9EAA92954226` (`authority_id`),
  KEY `Taxon_Reference_nameAccordingTo_FK` (`nameAccordingTo_id`),
  KEY `Taxon_Taxon_originalNameUsage_FK` (`originalNameUsage_id`),
  KEY `Taxon_accepted_id_IDX` (`acceptedNameUsage_id`),
  KEY `Taxon_parent_id_IDX` (`parentNameUsage_id`),
  KEY `Taxon_protologue_IDX` (`namePublishedIn_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_concept`;


CREATE TABLE `taxon_concept` (
  `Taxon_id` bigint(20) NOT NULL,
  `concepts_id` bigint(20) NOT NULL,
  KEY `Taxon_Concept_Taxon_FK` (`Taxon_id`),
  KEY `Taxon_Concept_concepts_FK` (`concepts_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_identificationkey`;


CREATE TABLE `taxon_identificationkey` (
  `Taxon_id` bigint(20) NOT NULL,
  `keys_id` bigint(20) NOT NULL,
  KEY `FK56D693661EDCD08E` (`Taxon_id`),
  KEY `FK56D69466437564A` (`keys_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_image`;


CREATE TABLE `taxon_image` (
  `Taxon_id` bigint(20) NOT NULL,
  `images_id` bigint(20) NOT NULL,
  KEY `Taxon_Image_Taxon_id_IDX` (`Taxon_id`),
  KEY `Taxon_Image_images_id_IDX` (`images_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_phylogenetictree`;


CREATE TABLE `taxon_phylogenetictree` (
  `Taxon_id` bigint(20) NOT NULL,
  `trees_id` bigint(20) NOT NULL,
  KEY `Taxon_PhylogeneticTree_Taxon_FK` (`Taxon_id`),
  KEY `Taxon_PhylogeneticTree_trees_FK` (`trees_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_reference`;


CREATE TABLE `taxon_reference` (
  `Taxon_id` bigint(20) NOT NULL,
  `references_id` bigint(20) NOT NULL,
  PRIMARY KEY (`Taxon_id`,`references_id`),
  KEY `Taxon_Reference_Taxon_id_IDX` (`Taxon_id`),
  KEY `Taxon_Reference_references_id_IDX` (`references_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taxon_typeandspecimen`;


CREATE TABLE `taxon_typeandspecimen` (
  `Taxon_id` bigint(20) NOT NULL,
  `typesAndSpecimens_id` bigint(20) NOT NULL,
  PRIMARY KEY (`Taxon_id`,`typesAndSpecimens_id`),
  KEY `Taxon_TypeAndSpecimen_Taxon_id_IDX` (`Taxon_id`),
  KEY `Taxon_TypeAndSpecimen_typesAndSpecimens_id_IDX` (`typesAndSpecimens_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `typeandspecimen`;


CREATE TABLE `typeandspecimen` (
  `id` bigint(20) NOT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `bibliographicCitation` varchar(255) DEFAULT NULL,
  `catalogNumber` varchar(255) DEFAULT NULL,
  `collectionCode` varchar(255) DEFAULT NULL,
  `institutionCode` varchar(255) DEFAULT NULL,
  `locality` longtext,
  `recordedBy` varchar(255) DEFAULT NULL,
  `scientificName` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `taxonRank` varchar(255) DEFAULT NULL,
  `typeDesignatedBy` varchar(255) DEFAULT NULL,
  `typeDesignationType` varchar(255) DEFAULT NULL,
  `typeStatus` varchar(255) DEFAULT NULL,
  `verbatimEventDate` varchar(255) DEFAULT NULL,
  `verbatimLabel` varchar(255) DEFAULT NULL,
  `verbatimLatitude` varchar(255) DEFAULT NULL,
  `verbatimLongitude` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `decimalLongitude` double DEFAULT NULL,
  `decimalLatitude` double DEFAULT NULL,
  `location` geometry DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `TypeAndSpecimen_authority_FK` (`authority_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_group`;


CREATE TABLE `user_group` (
  `User_id` bigint(20) NOT NULL,
  `groups_id` bigint(20) NOT NULL,
  PRIMARY KEY (`User_id`,`groups_id`),
  KEY `FKE7B7ED0BDA0BABAB` (`groups_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_permissions`;


CREATE TABLE `user_permissions` (
  `User_id` bigint(20) NOT NULL,
  `permissions` int(11) DEFAULT NULL,
  KEY `FKB4582A309E0AAB54` (`User_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `vernacularname`;


CREATE TABLE `vernacularname` (
  `id` bigint(20) NOT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `accessRights` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `rights` longtext,
  `rightsHolder` varchar(255) DEFAULT NULL,
  `countryCode` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `lifeStage` varchar(255) DEFAULT NULL,
  `locality` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `organismPart` varchar(255) DEFAULT NULL,
  `plural` bit(1) NOT NULL DEFAULT b'0',
  `preferredName` bit(1) NOT NULL DEFAULT b'0',
  `sex` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `taxonRemarks` varchar(255) DEFAULT NULL,
  `temporal` varchar(255) DEFAULT NULL,
  `vernacularName` varchar(255) DEFAULT NULL,
  `authority_id` bigint(20) DEFAULT NULL,
  `taxon_id` bigint(20) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifier` (`identifier`),
  KEY `VernacularName_authority_FK` (`authority_id`),
  KEY `VernacularName_taxon_FK` (`taxon_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;








