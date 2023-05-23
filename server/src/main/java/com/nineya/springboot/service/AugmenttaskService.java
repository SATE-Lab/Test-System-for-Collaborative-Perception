package com.nineya.springboot.service;

import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Augmenttask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cw
 * @since 2023-05-07
 */
public interface AugmenttaskService extends IService<Augmenttask> {

    R insertAugmentTask(Augmenttask augmenttask);

    R getAugTask();

    R getById(int id);

    R getByRoad(int road);

    R deleteById(Integer id);

    R modifytask(Integer id, Augmenttask augmenttask);
}
