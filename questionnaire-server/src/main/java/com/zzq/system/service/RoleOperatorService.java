package com.zzq.system.service;

import com.zzq.system.mapper.RoleOperatorMapper;
import com.zzq.system.model.RoleOperator;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RoleOperatorService {

    @Resource
    private RoleOperatorMapper roleOperatorMapper;

    public int insert(RoleOperator roleOperator) {
        return roleOperatorMapper.insert(roleOperator);
    }

}
