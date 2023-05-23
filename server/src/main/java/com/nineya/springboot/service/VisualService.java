package com.nineya.springboot.service;

import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Visual;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cw
 * @since 2023-05-13
 */
public interface VisualService extends IService<Visual> {

    R insertVisual(Visual visual);

    R getVisual(int id, int type);
}
