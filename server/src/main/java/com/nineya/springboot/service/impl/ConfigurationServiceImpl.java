package com.nineya.springboot.service.impl;

import com.nineya.springboot.entity.Configuration;
import com.nineya.springboot.mapper.ConfigurationMapper;
import com.nineya.springboot.service.ConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ylq
 * @since 2023-04-02
 */
@Service
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, Configuration> implements ConfigurationService {

}
