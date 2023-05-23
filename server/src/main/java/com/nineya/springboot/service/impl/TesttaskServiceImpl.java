package com.nineya.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Testtask;
import com.nineya.springboot.mapper.TesttaskMapper;
import com.nineya.springboot.service.TesttaskService;
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
 * @since 2023-04-06
 */
@Service
public class TesttaskServiceImpl extends ServiceImpl<TesttaskMapper, Testtask> implements TesttaskService {
    @Autowired
    private TesttaskMapper testtaskMapper;

    @Override
    public R insertTestTask(Testtask testtask) {
        try {
            if (testtaskMapper.insert(testtask) > 0) {
                return R.success("插入成功");
            } else {
                return R.error("插入失败");
            }
        } catch (DuplicateKeyException e) {
            return R.fatal(e.getMessage());
        }
    }

    @Override
    public R getAll() {
        QueryWrapper<Testtask> queryWrapper = new QueryWrapper<>();
        List<Testtask> TestTaskList = testtaskMapper.selectList(queryWrapper);
        return R.success(null, TestTaskList);
    }

    @Override
    public R deleteById(Integer id) {
        testtaskMapper.deleteById(id);
        return getAll();
    }

    @Override
    public R getById(int id) {
        QueryWrapper<Testtask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("ID", id);
        List<Testtask> TestTasklList = testtaskMapper.selectList(queryWrapper);
        return R.success(null, TestTasklList);
    }

    @Override
    public R getByAugId(int id) {
        QueryWrapper<Testtask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("augTaskId", id);
        List<Testtask> TestTasklList = testtaskMapper.selectList(queryWrapper);
        return R.success(null, TestTasklList);
    }

    @Override
    public R modifytask(Integer id, Testtask testtask) {
        testtaskMapper.updateById(testtask);
        return R.success(null, testtask);
    }
}
