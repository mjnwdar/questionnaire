package com.zzq.system.service;

import com.github.pagehelper.PageHelper;
import com.zzq.system.mapper.SysLogMapper;
import com.zzq.system.model.SysLog;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    public List<SysLog> selectAll(int page, int rows) {
        PageHelper.startPage(page, rows);
        return sysLogMapper.selectAll();
    }

    public int count() {
        return sysLogMapper.count();
    }
}
