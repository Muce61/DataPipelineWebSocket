package com.mc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mc.entity.SecurityCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author muse
 * @Date 2024/07/16
 **/
@Mapper
public interface TabSecurityCompanyMapper extends BaseMapper<SecurityCompany> {

}
