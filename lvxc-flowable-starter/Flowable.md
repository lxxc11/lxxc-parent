1. Flowable 6.5.0项目启动之后,会生成46张数据表。

ACT_EVT_*  
ACT_EVT_LOG：事件日志相关  
ACT_GE_*  
ACT_GE_BYTEARRAY：保存流程的bpmn的xml以及流程的Image缩略图等信息。  
ACT_GE_PROPERTY：Flowable相关的基本信息。比如各个module使用的版本信息。  
ACT_HI_*  
ACT_HI_ACTINST：流程实例历史。  
ACT_HI_ATTACHMENT：实例的历史附件(几乎不会使用，会加大数据库很大的一个loading)。  
ACT_HI_COMMENT：实例的历史备注。  
ACT_HI_DETAIL：实例流程详细信息。  
ACT_HI_ENTITYLINK：  
ACT_HI_IDENTITYLINK：实例节点中，如果指定了目标人，产生的历史。  
ACT_HI_PROCINST：流程实例历史。  
ACT_HI_TASKINST：流程实例的任务历史。  
ACT_HI_TSK_LOG：  
ACT_HI_VARINST：流程实例的变量历史。  
ACT_ID_*  
IDM模块，用户相关信息，似乎在百分之大多数引入Flowable流程引擎的系统中都不太会使用，而是直接只用业务系统的用户信息。  

ACT_ID_BYTEARRAY：  
ACT_ID_GROUP：用户组信息。  
ACT_ID_INFO：用户详情。  
ACT_ID_MEMBERSHIP：用户组和用户的关系。  
ACT_ID_PRIV：权限。  
ACT_ID_PRIV_MAPPING：用户组和权限之间的关系。  
ACT_ID_PROPERTY：用户或者用户组属性拓展表。  
ACT_ID_TOKEN：登录相关日志。  
ACT_ID_USER：用户。  
ACT_PROCDEF_*  
ACT_PROCDEF_INFO：当通过缓存保存的流程信息。  
ACT_RE_*  
ACT_RE_DEPLOYMENT：部署对象，存储流程名称 租户相关。  
ACT_RE_MODEL：基于流程的模型信息。  
ACT_RE_PROCDEF：流程定义表  
ACT_RU_*  
ACT_RU_ACTINST：运行中实例的活动表。  
ACT_RU_DEADLETTER_JOB：当JOB执行很多次都无法执行，就会被记录在此表。  
ACT_RU_ENTITYLINK：  
ACT_RU_EVENT_SUBSCR：运行时的事件。  
ACT_RU_EXECUTION：运行的实例表。  
ACT_RU_HISTORY_JOB：运行中的定时任务历史表。  
ACT_RU_IDENTITYLINK：当前任务执行人的信息。  
ACT_RU_JOB：运行中的异步任务。  
ACT_RU_SUSPENDED_JOB：暂停的任务表。如果一个异步任务在运行中，被暂停。就会记录在词表。  
ACT_RU_TASK：运行中的正常节点任务。  
ACT_RU_TIMER_JOB：定时作业表。  
ACT_RU_VARIABLE：运行中的流程实例变量。  
FLW_*  
FLW_CHANNEL_DEFINITION：泳池管道定义表。  
FLW_EVENT_DEFINITION：已部署事件定义的元数据。  
FLW_EVENT_DEPLOYMENT：已部署事件部署元数据。  
FLW_EVENT_RESOURCE：事件所需资源。  
FLW_EV_DATABASECHANGELOG：Liquibase执行的记录。  
FLW_EV_DATABASECHANGELOGLOCK：Liquibase执行锁。  
FLW_RU_BATCH：  
FLW_RU_BATCH_PART：  


2. Flowable 知识文档：
   https://tkjohn.github.io/flowable-userguide/#_introduction