package com.lvxc.doc.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文档对象，systemDocList数量代表系统个数
 * @param <T> 系统文档对象
 */
public class DetailDoc<T extends SystemBaseDoc> implements DetailDocBaseDoc<T> {
    //一份文档中可能多个系统系统
    private List<T> systemDocList;
    //文档名称
    private String name;
    //文档作者
    private String author;
    //文档创建时间（yyyy-MM-dd）
    private String createTime;

    @Override
    public List<T> getSystemDocList() {
        return this.systemDocList;
    }

    public DetailDoc<T> addSystemDoc(T doc) {
        if (CollectionUtils.isEmpty(systemDocList)){
            systemDocList = new ArrayList<>();
        }
        if (doc != null){
            systemDocList.add(doc);
        }
        return this;
    }

    public DetailDoc<T> addSystemDocs(T... doc) {
        if (CollectionUtils.isEmpty(systemDocList)){
            systemDocList = new ArrayList<>();
        }
        if (doc != null && doc.length > 0){
            systemDocList.addAll(Arrays.asList(doc));
        }
        return this;
    }

    @Override
    public DetailDoc<T> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
