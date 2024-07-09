package com.lvxc.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author 周锐
 * @Date 2023 2023/9/6 15:25
 * @Version 1.0
 * @Description
 */
@Data
public class TreeDestNode {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 层级
     */
    private int level;

    /**
     * 子节点
     */
    private List<TreeDestNode> children;
}