package com.nineya.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Testtask;
import com.nineya.springboot.service.AugmenttaskService;
import com.nineya.springboot.service.ReportService;
import com.nineya.springboot.service.TesttaskService;
import com.nineya.springboot.thread.AugThread;
import com.nineya.springboot.thread.ModelTestThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cw
 * @since 2023-04-06
 */
@RestController
public class TesttaskController {
    @Autowired
    AugmenttaskService augmenttaskService;

    @Autowired
    TesttaskService testtaskService;

    @Autowired
    ReportService reportService;

    @ResponseBody
    @RequestMapping(value = "/testtask/operate",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public R operate( HttpServletRequest req, HttpSession session) {
        int id = Integer.parseInt(req.getParameter("id"));
        List<Testtask> TestTaskList = (List<Testtask>) testtaskService.getById(id).getData();
        if(TestTaskList.get(0).getStatus()!=1){
            return null;
        }
        ModelTestThread modelTestThread= new ModelTestThread(TestTaskList.get(0),testtaskService,reportService,augmenttaskService);
        modelTestThread.start();

        System.out.println("继续执行3秒钟");
        TestTaskList.get(0).setStatus(3);
        return testtaskService.modifytask( TestTaskList.get(0).getId(), TestTaskList.get(0));
    }

    @ResponseBody
    @RequestMapping(value = "/testtask/create",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public R create( HttpServletRequest req, HttpSession session) {
        int id = Integer.parseInt(req.getParameter("id"));
        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.getById(id).getData();
        if(augTaskList.get(0).getStatus()!=1){
            return null;
        }
        Testtask testtask=new Testtask();
        testtask.setAugtaskid(id);
        testtask.setStatus(1);
        testtask.getCreatetime();
        Augmenttask augmenttask =augTaskList.get(0);
        testtask.setTestsetid(augmenttask.getAugsetid());
        testtask.setName(augmenttask.getTaskname());
        augmenttask.setStatus(3);
        augmenttaskService.modifytask(augmenttask.getId(),augmenttask);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        testtask.setCreatetime(timestamp);

        AugThread augThread=new AugThread(augmenttask,augmenttaskService,testtask,testtaskService);
        augThread.start();
        return augmenttaskService.modifytask(augmenttask.getId(),augmenttask);
    }

    @RequestMapping(value = "/testtask/getAll",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getAll(HttpServletRequest req, HttpSession session) {

        List<Testtask> TestTaskList = (List<Testtask>) testtaskService.getAll().getData();
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray(3);
        JSONObject data = new JSONObject();
        int i=0;
        for (Testtask testTask:TestTaskList){
            String time =testTask.getCreatetime().toString();
            JSONObject item = new JSONObject();
            item.put("id",testTask.getId());
            item.put("a",testTask.getName());
            item.put("b",time);
            item.put("d",testTask.getAugtaskid());
            item.put("e",testTask.getStatus());
            array.add(i,item);
            i++;
        }
        data.put("rows",array);
        result.put("status",0);
        result.put("msg", "");
        result.put("data", data);

        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    @RequestMapping(value = "/testtask/deleteById",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String deleteById(HttpServletRequest req, HttpSession session) {

        System.out.println("删除测试任务");
        int id = Integer.parseInt(req.getParameter("id"));

        List<Testtask> testtaskList = (List<Testtask>) testtaskService.deleteById(id).getData();
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray(3);
        JSONObject data = new JSONObject();
        int i=0;
        for (Testtask testTask:testtaskList){
            JSONObject item = new JSONObject();
            item.put("id",testTask.getId());
            item.put("a",testTask.getName());
            item.put("b",testTask.getCreatetime());
            item.put("d",testTask.getAugtaskid());
            item.put("e",testTask.getStatus());
            array.add(i,item);
            i++;
        }
        data.put("rows",array);
        result.put("status",0);
        result.put("msg", "");
        result.put("data", data);

        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

}

