package com.lvxc.common.dto;

/**
 * @Author 周锐
 * @Date 2023 2023/9/6 15:26
 * @Version 1.0
 * @Description
 */
public abstract class TreeSourceNode {

    /**
     * 获取父节点id
     *
     * @return
     */
    public abstract String getParentId();

    /**
     * 获取节点id
     *
     * @return
     */
    public abstract String getId();

    /**
     * 获取节点名称
     *
     * @return
     */
    public abstract String getName();
}