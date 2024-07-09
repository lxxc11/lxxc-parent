package com.lvxc.doc.service;

import com.lvxc.doc.domain.SystemBaseDoc;
import com.lvxc.doc.domain.SystemModuleBaseDoc;
import com.lvxc.doc.domain.DetailDocBaseDoc;
import com.lvxc.doc.domain.dto.DsDocDTO;

import javax.servlet.http.HttpServletResponse;


public interface DsDocService {
    /**
     * 数据库设置由数据提供，开发不提供
     * @param dto
     * @param response
     */
    @Deprecated
    void buildDoc(DsDocDTO dto, HttpServletResponse response);

    <A extends SystemModuleBaseDoc,S extends SystemBaseDoc<A>,T extends DetailDocBaseDoc<S>> void buildDetail(T detailDoc);
}
