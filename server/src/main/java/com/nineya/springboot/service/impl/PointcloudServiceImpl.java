package com.nineya.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Pointcloud;
import com.nineya.springboot.mapper.PointcloudMapper;
import com.nineya.springboot.service.PointcloudService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cw
 * @since 2023-04-05
 */
@Service
public class PointcloudServiceImpl extends ServiceImpl<PointcloudMapper, Pointcloud> implements PointcloudService {
    @Autowired
    private PointcloudMapper pointcloudMapper;

    @Override
    public R insertPointCloud(Pointcloud pointcloud) {
        try {
            if (pointcloudMapper.insert(pointcloud) > 0) {
                return R.success("插入成功");
            } else {
                return R.error("插入失败");
            }
        } catch (DuplicateKeyException e) {
            return R.fatal(e.getMessage());
        }

    }

    @Override
    public R getCar(int sence, int car) {
        QueryWrapper<Pointcloud> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("sence", sence);
        queryWrapper.like("car", car);
        List<Pointcloud> CarList = pointcloudMapper.selectList(queryWrapper);
        return R.success(null, CarList);
    }
}
