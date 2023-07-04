package com.orion.ops.framework.mybatis.type;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO TEST
 * varchar -> List<Long>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:33
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class LongListTypeHandler implements ITypeHandler<String, List<Long>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Long> res, JdbcType jdbcType) throws SQLException {
        // 设置占位符
        ps.setString(i, Lists.join(res));
    }

    @Override
    public List<Long> getResult(ResultSet rs, String columnName) throws SQLException {
        return this.getResult(rs.getString(columnName));
    }

    @Override
    public List<Long> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    @Override
    public List<Long> getResult(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(value.split(Const.COMMA))
                .filter(Strings::isNumber)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

}
