package com.lvxc.doc.service.impl;

import com.lvxc.doc.config.parser.SystemParser;
import com.lvxc.doc.config.util.DocWordUtil;
import com.lvxc.doc.domain.dto.DsDocDTO;
import com.lvxc.doc.domain.vo.DsTableVO;
import com.lvxc.doc.service.DsDocService;
import com.lvxc.doc.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DsDocServiceImpl implements DsDocService {

    @Override
    public void buildDoc(DsDocDTO dto, HttpServletResponse response) {
        String path = "./industry/";
        String fileName = "《"+dto.getDocName()+"》数据库设计";
        Map<String, Object> dataMap = new HashMap<>();
//        List<DsTableVO> dsTableVOS = dsDocMapper.queryDataSourceInfos(Arrays.asList(dto.getSchemas().split(",")));
        List<DsTableVO> dsTableVOS = executeJdbc(dto);
        if (CollectionUtils.isEmpty(dsTableVOS)){
            throw new RuntimeException("请确认数据库链接及Schema是否正确");
        }
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, List<DsTableVO>> collect = dsTableVOS.stream().collect(Collectors.groupingBy(DsTableVO::getName2));
        Map<String,Object> item = null;
        for (Map.Entry<String, List<DsTableVO>> entry : collect.entrySet()) {
            item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("size", entry.getValue().size());
            item.put("list", entry.getValue());
            list.add(item);
        }
        dataMap.put("tableList", list);
        dataMap.put("projectName", dto.getDocName());
        File word = DocWordUtil.createWord("datasource.ftl", path + fileName, dataMap, false, false);
        //直接看本地生成的把，文件大下面的太慢
        //写入输出流
//        buildExcelDocument(response,word);
    }

    @Override
    public <A extends SystemModuleBaseDoc,S extends SystemBaseDoc<A>,T extends DetailDocBaseDoc<S>> void buildDetail(T detailDoc) {
        String path1 = "./doc/";
        Map<String, Object> result = doParser(detailDoc);
        result.put("projectName", detailDoc.getName());
        result.put("createTime", detailDoc.getCreateTime());
        result.put("author", detailDoc.getAuthor());
        File word = DocWordUtil.createWord("detailDoc.ftl", path1 + detailDoc.getName()+"详细设计说明书", result, false, false);
        File word1 = DocWordUtil.createWord("summaryDoc.ftl", path1 + detailDoc.getName()+"概要设计说明书", result, false, false);
    }

    private <A extends SystemModuleBaseDoc,S extends SystemBaseDoc<A>,T extends DetailDocBaseDoc<S>> Map<String, Object> doParser(T detailDoc) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> sysList = new ArrayList<>();
        for (S systemDoc : detailDoc.getSystemDocList()) {
            sysList.add(new SystemParser().doParser(systemDoc).getResult());
        }
        result.put("system", sysList);
        return result;
    }

    private static void buildExcelDocument(HttpServletResponse response, File file) {
        ServletOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            //设置文件ContentType类型、图片类型
            //自动判断下载文件类型
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            response.setStatus(200);
        } catch (Exception e) {
            log.info("文件输出异常");
        } finally {
            try {
                assert out != null;
                out.close();
                in.close();
            } catch (NullPointerException e) {
                log.error("close error:NullPointerException:" + e.toString());
            } catch (Exception e) {
                log.error("close error:Exception:" + e.toString());
            }
        }
    }


    private List<DsTableVO> executeJdbc(DsDocDTO dto){
        dto.setSchemas("'"+dto.getSchemas().replace(",","','")+"'");
        List<DsTableVO> result = new ArrayList<>();
        // 数据库连接URL，用户名和密码
        String url = dto.getUrl();
        String user = dto.getUsername();
        String password = dto.getPassword();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 加载MySQL JDBC驱动
            Class.forName("org.postgresql.Driver");
            // 建立连接
            conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象
            stmt = conn.createStatement();
            // 执行查询
            String sql = "select t.\"comment\" as name1,t.name as name2,k.attname as name3 ,k.name4 ,k.name5,k.name6,\n" +
                    "         k.nullable name7,k.comment as name8 from(select a.attrelid,\n" +
                    "\n" +
                    "        a.attname,\n" +
                    "\n" +
                    "        format_type(a.atttypid,a.atttypmod) as name4,\n" +
                    "\n" +
                    "        (case when atttypmod-4>0 then atttypmod-4 else 0 end) as name5,\n" +
                    "\n" +
                    "        (case when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='p')>0 then 'Y' else 'N' end) as name6,\n" +
                    "\n" +
                    "        (case when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='u')>0 then 'Y' else 'N' end) as 唯一约束,\n" +
                    "\n" +
                    "        (case when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='f')>0 then 'Y' else 'N' end) as 外键约束,\n" +
                    "\n" +
                    "        (case when a.attnotnull=true then 'Y' else 'N' end) as nullable,\n" +
                    "        col_description(a.attrelid,a.attnum) as comment\n" +
                    "        from pg_attribute a\n" +
                    "        ) k\n" +
                    "        left join (\n" +
                    "        SELECT a.oid,\n" +
                    "        a.relname AS name,\n" +
                    "        b.description AS comment\n" +
                    "        FROM pg_class a\n" +
                    "        LEFT OUTER JOIN pg_description b ON b.objsubid=0 AND a.oid = b.objoid\n" +
                    "        WHERE a.relnamespace in (SELECT oid FROM pg_namespace WHERE nspname in ("+dto.getSchemas()+")) " +
                    "        AND a.relkind='r'\n" +
                    "        ORDER BY a.relname\n" +
                    "        )t on t.oid=k.attrelid where t.name is not null AND k.comment is not null";
            rs = stmt.executeQuery(sql);
            DsTableVO vo = null;
            // 处理结果
            while (rs.next()) {
                vo = new DsTableVO();
                vo.setName1(rs.getString("name1"));
                vo.setName2(rs.getString("name2"));
                vo.setName3(rs.getString("name3"));
                vo.setName4(rs.getString("name4"));
                vo.setName5(rs.getString("name5"));
                vo.setName6(rs.getString("name6"));
                vo.setName7(rs.getString("name7"));
                vo.setName8(rs.getString("name8"));
                result.add(vo);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

}
