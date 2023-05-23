package com.nineya.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Pointcloud;
import com.nineya.springboot.service.PointcloudService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cw
 * @since 2023-04-05
 */
@RestController
public class PointcloudController {
    @Autowired
    PointcloudService pointcloudService;

    @ResponseBody
    @RequestMapping(value = "/pointcloud/upload",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public R upload(HttpServletRequest req, HttpSession session) {
        Pointcloud pointcloud=new Pointcloud();
        pointcloud.setFiletype(0);
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) req;
        String scene="";
        String car="";
        InputStream in = null;
        OutputStream fileout = null;


        int id =Integer.parseInt(req.getParameter("id"));
        String road =req.getParameter("road");
        System.out.println(road);
        MultipartFile multipartFile = multipartRequest.getFile("file");

        try {
            String fileName = multipartFile.getOriginalFilename();
            pointcloud.setName(fileName);
            pointcloud.setSize((int) multipartFile.getSize());
            System.out.println(fileName);
            if(road.equals("A")){
                pointcloud.setSence(0);
                scene="TwoCars_1/";
            }else if (road.equals("B")){
                pointcloud.setSence(1);
                scene="TwoCars_2/";
            }else if (road.equals("C")){
                pointcloud.setSence(2);
                scene="ThreeCars_1/";
            }else {
                pointcloud.setSence(3);
                scene="ThreeCars_2/";
            }
            if(id == 1 || id==3){
                pointcloud.setCar(0);
                car="car1/";
            }else if(id ==2 || id==4){
                pointcloud.setCar(1);
                car="car2/";
            }else {
                pointcloud.setCar(2);
                car="car3/";
            }
            String fielDir="/home/chenwei/test_pcl/upload/"+scene+car+ fileName.substring(0, fileName.lastIndexOf("."))  + fileName.substring(fileName.lastIndexOf("."));
            pointcloud.setAddress(fielDir);

            File file = new File(fielDir);
            in = multipartFile.getInputStream();
            ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            Streams.copy(in, bstream, true);
            fileout = new FileOutputStream(file);
            bstream.writeTo(fileout);
        } catch (IOException e) {
            throw new RuntimeException("file copy error!",e);
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileout != null){
                try {
                    fileout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        pointcloud.setUploadtime(timestamp);
        System.out.println("上传功能");

        return pointcloudService.insertPointCloud(pointcloud);
    }

    @ResponseBody
    @RequestMapping(value = "/pointcloud/getcar",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getcar(HttpServletRequest req, HttpSession session) {
        System.out.println("获取车辆信息");
        int id =Integer.parseInt(req.getParameter("id"));
        int road =Integer.parseInt(req.getParameter("road"));
        int sence =road;
        int car =0;
        if(id == 1 || id==3){
            car=0;
        }else if(id ==2 || id==4){
            car=1;
        }else {
            car=2;
        }

        List<Pointcloud> CarList = (List<Pointcloud>) pointcloudService.getCar(sence,car).getData();

        JSONObject data = new JSONObject();
        JSONArray option =new JSONArray(CarList.size());
        int i=0;
        for (Pointcloud pointcloud:CarList){
            System.out.println(pointcloud.getName());
            JSONObject item = new JSONObject();
            item.put("label",pointcloud.getName());
            item.put("value",pointcloud.getAddress());
            option.add(i,item);
            i++;
        }
        data.put("option",option);
        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");
        testresult.put("data",option);
        if(id<=2 && road>=2){
           return null;
        }
        if(id>2 && road<2){
            return  null;
        }
        return testresult.toJSONString();


    }



}
