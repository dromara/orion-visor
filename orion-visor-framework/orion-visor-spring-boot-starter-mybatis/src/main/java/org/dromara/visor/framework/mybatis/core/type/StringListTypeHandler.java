/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.mybatis.core.type;

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.dromara.visor.common.constant.Const;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * varchar -> List<String>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:33
 */
@MappedJdbcTypes({JdbcType.CHAR, JdbcType.VARCHAR})
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
