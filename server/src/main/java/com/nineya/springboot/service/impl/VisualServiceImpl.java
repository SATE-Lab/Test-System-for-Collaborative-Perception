package com.nineya.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Visual;
import com.nineya.springboot.mapper.VisualMapper;
import com.nineya.springboot.service.VisualService;
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
 * @since 2023-05-13
 */
@Service
public class VisualServiceImpl extends ServiceImpl<VisualMapper, Visual> implements VisualService {
    @Autowired
    private VisualMapper visualMapper;
    @Override
    public R insertVisual(Visual visual) {
        try {
            if (visualMapper.insert(visual) > 0) {
                return R.success("插入成功");
            } else {
                return R.error("插入失败");
            }
        } catch (DuplicateKeyException e) {
            return R.fatal(e.getMessage());
        }
    }

    @Override
    public R getVisual(int id, int type) {
        QueryWrapper<Visual> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("augId", id);
        queryWrapper.like("type", type);
        List<Visual> visualList = visualMapper.selectList(queryWrapper);
        return R.success(null, visualList);
    }
}
