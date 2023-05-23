
package com.nineya.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.mapper.AugmenttaskMapper;
import com.nineya.springboot.service.AugmenttaskService;
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
public class AugmenttaskServiceImpl extends ServiceImpl<AugmenttaskMapper, Augmenttask> implements AugmenttaskService {
    @Autowired
    private AugmenttaskMapper augmenttaskMapper;

    @Override
    public R insertAugmentTask(Augmenttask augmenttask) {
        try {
            if (augmenttaskMapper.insert(augmenttask) > 0) {
                return R.success("插入成功");
            } else {
                return R.error("插入失败");
            }
        } catch (DuplicateKeyException e) {
            return R.fatal(e.getMessage());
        }
    }

    @Override
    public R getAugTask() {
        QueryWrapper<Augmenttask> queryWrapper = new QueryWrapper<>();
        List<Augmenttask> AugTasklList = augmenttaskMapper.selectList(queryWrapper);
        return R.success(null, AugTasklList);
    }

    @Override
    public R getById(int id) {
        QueryWrapper<Augmenttask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("ID", id);
        List<Augmenttask> AugTasklList = augmenttaskMapper.selectList(queryWrapper);
        return R.success(null, AugTasklList);
    }

    @Override
    public R getByRoad(int road) {
        QueryWrapper<Augmenttask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("road", road);
        List<Augmenttask> AugTasklList = augmenttaskMapper.selectList(queryWrapper);
        return R.success(null, AugTasklList);
    }

    @Override
    public R deleteById(Integer id) {
        augmenttaskMapper.deleteById(id);
        return getAugTask();
    }

    @Override
    public R modifytask(Integer id, Augmenttask augmenttask) {
        augmenttaskMapper.updateById(augmenttask);
//        System.out.println(modelMapper.selectById(id));
        return R.success(null, augmenttask);
    }
}


