package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * mybatis entity 的 基础bean
 *
 * @author wr
 */
@Data
@ApiModel
@Accessors(chain = true)
@ToString(callSuper = true)
public class MybatisEntity<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableField(exist = false)
  @ApiModelProperty(value = "页码", example = "1")
  private Long current = 1L;

  @TableField(exist = false)
  @ApiModelProperty(value = "页容量", example = "10")
  private Long pageSize = 10L;


  public Page<T> page() {
    return new Page<>(current, pageSize);
  }

  public static final String COLUMN_ID = "id";
  public static final String LIMIT_ONE = "LIMIT 1 ";
  public static final String LIMIT_HUNDRED = "LIMIT 100 ";
  public static final String FIELD_CREATE_TIME = "createTime";
  public static final String FIELD_UPDATE_TIME = "updateTime";
}
