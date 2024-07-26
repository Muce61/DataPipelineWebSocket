package com.mc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mc.entity.PermissionDetails;
import com.mc.mapper.TabSecurityCompanyMapper;
import com.mc.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author muse
 * @Date 2024/07/16
 **/
@Service("SecurityService")
public class SecurityServiceImpl extends ServiceImpl<TabSecurityCompanyMapper, PermissionDetails>  implements SecurityService {
    @Override
    public Boolean isPass(String key, String[] topics) {
        if (StringUtils.isBlank(key)) {
            return false;
        }

        boolean flag = true;
        for (String topic : topics) {
            QueryWrapper<PermissionDetails> query = new QueryWrapper<>();
            query.eq("uuid", key);
            query.like("permission_name", topic);
            PermissionDetails queryOne = this.getOne(query);
            if (queryOne == null) {
                flag = false;
            }
        }

        return flag;
    }
}
