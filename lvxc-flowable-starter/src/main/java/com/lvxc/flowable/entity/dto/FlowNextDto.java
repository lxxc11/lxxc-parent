package com.lvxc.flowable.entity.dto;

import com.lvxc.user.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 动态人员、组
 *
 * @author
 * @date
 */
@Data
public class FlowNextDto implements Serializable {

  private String type;

  private String vars;

  private List<SysUser> userList;

//  private List<SysRole> roleList;
}
