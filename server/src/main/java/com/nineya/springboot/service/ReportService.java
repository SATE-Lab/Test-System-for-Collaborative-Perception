package com.nineya.springboot.service;

import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cw
 * @since 2023-05-07
 */
public interface ReportService extends IService<Report> {

    R getAll();

    R insertReport(Report report);

    R getById(int id);
}
