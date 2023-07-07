package com.orion.ops.framework.mybatis.core.type;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO TEST
 * varchar -> List<String>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:33
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class StringListTypeHandler implements ITypeHandler<String, List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> res, JdbcType jdbcType) throws SQLException {
        // 设置占位符
        ps.setString(i, Lists.join(res));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return this.getResult(rs.getString(columnName));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    @Override
    public List<String> getResult(String value) {
        if (value == null) {
            return null;
        }
        return Lists.of(value.split(Const.COMMA));
    }

}
