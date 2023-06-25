package com.orion.ops.framework.mybatis.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO TEST
 * varchar -> JSONObject
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:33
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(JSONObject.class)
public class JSONObjectTypeHandler implements ITypeHandler<String, JSONObject> {

    @Override
    public void setParameter(PreparedStatement ps, int i, JSONObject res, JdbcType jdbcType) throws SQLException {
        // todo TEST NULL
        // 设置占位符
        ps.setString(i, res.toString());
    }

    @Override
    public JSONObject getResult(ResultSet rs, String columnName) throws SQLException {
        return this.getResult(rs.getString(columnName));
    }

    @Override
    public JSONObject getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public JSONObject getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    @Override
    public JSONObject getResult(String value) {
        if (value == null) {
            return null;
        }
        try {
            return JSON.parseObject(value);
        } catch (Exception e) {
            return null;
        }
    }

}
