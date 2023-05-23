package com.nineya.springboot.service;

import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Pointcloud;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cw
 * @since 2023-04-05
 */
public interface PointcloudService extends IService<Pointcloud> {

    R insertPointCloud(Pointcloud pointcloud);

    R getCar(int sence, int car);
}
