package com.lvxc.doc.domain;

import java.util.List;

public interface DetailDocBaseDoc<T extends SystemBaseDoc> extends BaseDoc{
    List<T> getSystemDocList();
    DetailDocBaseDoc<T> addSystemDoc(T doc);
    DetailDocBaseDoc<T> addSystemDocs(T... doc);
    void setAuthor(String author);
    String getAuthor();
    void setCreateTime(String createTime);
    String getCreateTime();
}
