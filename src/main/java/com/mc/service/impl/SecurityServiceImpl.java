package com.mc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mc.dao.TabSecurityCompanyMapper;
import com.mc.entity.SecurityCompany;
import com.mc.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author muse
 * @Date 2024/07/16
 **/
@Service("SecurityService")
public class SecurityServiceImpl extends ServiceImpl<TabSecurityCompanyMapper, SecurityCompany>  implements SecurityService {
    @Override
    public Integer isPass(String key) {
        if (StringUtils.isBlank(key)) {
            return 0;
        }
        QueryWrapper<SecurityCompany> query = new QueryWrapper<>();
        query.eq("uuid", key);
        SecurityCompany queryOne = this.getOne(query);
        return queryOne == null ? 0 : 1;
    }
}
