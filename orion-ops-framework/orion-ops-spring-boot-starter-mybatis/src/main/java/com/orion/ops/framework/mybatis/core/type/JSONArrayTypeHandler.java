package com.orion.ops.framework.mybatis.core.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO TEST
 * varchar -> JSONArray
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:33
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(JSONArray.class)
public class JSONArrayTypeHandler implements ITypeHandler<String, JSONArray> {

    @Override
    public void setParameter(PreparedStatement ps, int i, JSONArray res, JdbcType jdbcType) throws SQLException {
        // todo TEST NULL
        // 设置占位符
        ps.setString(i, res.toString());
    }

    @Override
    public JSONArray getResult(ResultSet rs, String columnName) throws SQLException {
        return this.getResult(rs.getString(columnName));
    }

    @Override
    public JSONArray getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public JSONArray getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    @Override
    public JSONArray getResult(String value) {
        if (value == null) {
            return null;
        }
        try {
            return JSON.parseArray(value);
        } catch (Exception e) {
            return null;
        }
    }

}
