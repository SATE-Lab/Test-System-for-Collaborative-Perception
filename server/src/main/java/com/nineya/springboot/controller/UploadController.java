package com.nineya.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    JSONObject result = new JSONObject();
    JSONObject testresult = new JSONObject();
    int flag =0;
    int testflag =0;
    private HttpServletRequest req;


    // 判断是否登录成功
    @ResponseBody
    @RequestMapping(value = "/upload/car",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public Object uploadCar(HttpServletRequest req, HttpSession session) {
        System.out.println("点云文件上传");
        int id =Integer.parseInt(req.getParameter("id"));

        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) req;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        InputStream in = null;
        OutputStream fileout = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            System.out.println(fileName);
            String scene="";
            String car="";
            if(id<=2){
                scene="Two_car_scenario/";
            }else {
                scene="Three_car_scenario/";
            }
            if(id == 1 || id==3){
                car="car1/";
            }else if(id ==2 || id==4){
                car="car2/";
            }else {
                car="car3/";
            }
            String fielDir="D:/upload/"+scene+car+ fileName.substring(0, fileName.lastIndexOf(".")) + "_"  + fileName.substring(fileName.lastIndexOf("."));
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

        JSONObject data = new JSONObject();
        data.put("value","haha");
        testresult.put("status",0);
        testresult.put("msg","");
        testresult.put("data",data);
        return testresult.toJSONString();
    }

    @RequestMapping(value = "/getall/car",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public Object getAll(HttpServletRequest req, HttpSession session) {
        System.out.println("点云文件查询");
        int id =Integer.parseInt(req.getParameter("id"));

        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) req;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        InputStream in = null;
        OutputStream fileout = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            System.out.println(fileName);
            String scene="";
            String car="";
            if(id<=2){
                scene="Two_car_scenario/";
            }else {
                scene="Three_car_scenario/";
            }
            if(id == 1 || id==3){
                car="car1/";
            }else if(id ==2 || id==4){
                car="car2/";
            }else {
                car="car3/";
            }
            String fielDir="D:/upload/"+scene+car+ fileName.substring(0, fileName.lastIndexOf(".")) + "_"  + fileName.substring(fileName.lastIndexOf("."));
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

        JSONObject data = new JSONObject();
        data.put("value","haha");
        testresult.put("status",0);
        testresult.put("msg","");
        testresult.put("data",data);
        return testresult.toJSONString();
    }
}
