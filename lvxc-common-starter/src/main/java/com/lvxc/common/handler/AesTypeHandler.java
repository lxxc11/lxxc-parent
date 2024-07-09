package com.lvxc.common.handler;

import com.lvxc.common.utils.AESUtil;
import lombok.SneakyThrows;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 使用：
 * @Table(autoResultMap = true) 不设置为true查询结果依旧是密文
 *
 * @TableField(typeHandler = AesTypeHandler.class)
 */

public class AesTypeHandler implements TypeHandler<String> {


    @SneakyThrows
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {
        ps.setString(i, AESUtil.encryption(parameter));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) {
        try {
            return AESUtil.decryption(rs.getString(columnName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) {
        try {
            return AESUtil.decryption(rs.getString(columnIndex));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return AESUtil.decryption(cs.getString(columnIndex));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

