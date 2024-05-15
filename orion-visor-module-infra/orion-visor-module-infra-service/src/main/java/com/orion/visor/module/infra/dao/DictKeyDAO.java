package com.orion.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.DictKeyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典配置项 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface DictKeyDAO extends IMapper<DictKeyDO> {

    /**
     * 通过 key 查询
     *
     * @param key key
     * @return dictKey
     */
    default DictKeyDO selectByKey(String key) {
        LambdaQueryWrapper<DictKeyDO> wrapper = this.lambda()
                .eq(DictKeyDO::getKeyName, key)
                .last(Const.LIMIT_1);
        return this.selectOne(wrapper);
    }

}
