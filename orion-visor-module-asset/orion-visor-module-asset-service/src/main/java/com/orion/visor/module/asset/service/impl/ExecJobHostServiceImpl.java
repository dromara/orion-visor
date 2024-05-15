package com.orion.visor.module.asset.service.impl;

import com.orion.visor.module.asset.dao.ExecJobHostDAO;
import com.orion.visor.module.asset.entity.domain.ExecJobHostDO;
import com.orion.visor.module.asset.service.ExecJobHostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划任务主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Slf4j
@Service
public class ExecJobHostServiceImpl implements ExecJobHostService {

    @Resource
    private ExecJobHostDAO execJobHostDAO;

    @Override
    public void setHostIdByJobId(Long jobId, List<Long> hostIdList) {
        log.info("ExecJobHostService.setHostIdByJobId jobId: {}, hostIdList: {}", jobId, hostIdList);
        // 删除
        execJobHostDAO.deleteByJobId(jobId);
        // 重新插入
        List<ExecJobHostDO> rows = hostIdList.stream()
                .map(s -> ExecJobHostDO.builder()
                        .hostId(s)
                        .jobId(jobId)
                        .build())
                .collect(Collectors.toList());
        execJobHostDAO.insertBatch(rows);
    }

    @Override
    public List<Long> getHostIdByJobId(Long jobId) {
        return execJobHostDAO.selectHostIdByJobId(jobId);
    }

    @Override
    public Integer deleteByJobId(Long jobId) {
        return execJobHostDAO.deleteByJobId(jobId);
    }

    @Override
    public Integer deleteByHostId(Long hostId) {
        return execJobHostDAO.deleteByHostId(hostId);
    }

}
