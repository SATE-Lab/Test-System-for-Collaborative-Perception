package com.nineya.springboot.service;

import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Testtask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cw
 * @since 2023-04-06
 */
public interface TesttaskService extends IService<Testtask> {

    R insertTestTask(Testtask testtask);

    R getAll();

    R deleteById(Integer id);

    R getById(int id);

    R getByAugId(int id);

    R modifytask(Integer id, Testtask testtask);
}
