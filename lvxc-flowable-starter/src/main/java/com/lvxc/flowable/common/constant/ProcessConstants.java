package com.lvxc.flowable.common.constant;


public class ProcessConstants {

  private ProcessConstants(){
    throw new IllegalStateException("Utility class");
  }

  /**
   * 动态数据
   */
  public static final String DATA_TYPE = "dynamic";

  /**
   * 单个审批人
   */
  public static final String USER_TYPE_ASSIGNEE = "flowable:assignee";


  /**
   * 候选人
   */
  public static final String USER_TYPE_USERS = "candidateUsers";


  /**
   * 审批组
   */
  public static final String USER_TYPE_ROUPS = "candidateGroups";

  /**
   * 单个审批人
   */
  public static final String PROCESS_APPROVAL = "approval";

  /**
   * 会签人员
   */
  public static final String PROCESS_MULTI_INSTANCE_USER = "userList";

  /**
   * nameapace
   */
  public static final String NAMASPASE = "http://flowable.org/bpmn";

  /**
   * 会签节点
   */
  public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

  /**
   * 自定义属性 dataType
   */
  public static final String PROCESS_CUSTOM_DATA_TYPE = "dataType";

  /**
   * 自定义属性 userType
   */
  public static final String PROCESS_CUSTOM_USER_TYPE = "userType";

  /**
   * 初始化人员
   */
  public static final String PROCESS_INITIATOR = "INITIATOR";


  /**
   * 流程跳过
   */
  public static final String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";


}
