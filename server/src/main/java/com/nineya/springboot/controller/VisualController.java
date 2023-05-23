package com.nineya.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Pointcloud;
import com.nineya.springboot.entity.Report;
import com.nineya.springboot.entity.Visual;
import com.nineya.springboot.service.AugmenttaskService;
import com.nineya.springboot.service.VisualService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VisualController {
    @Autowired
    AugmenttaskService augmenttaskService;
    @Autowired
    VisualService visualService;

    @ResponseBody
    @RequestMapping(value = "/visual/getset",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getcar(HttpServletRequest req, HttpSession session) {
        System.out.println("获取可视化信息");
        int road;
        int type;
        if(req.getParameter("road").equals("")){
            return null;
        }else {
            road = Integer.parseInt(req.getParameter("road"));
            type = Integer.parseInt(req.getParameter("type"));
        }

        List<Augmenttask> AugList = (List<Augmenttask>) augmenttaskService.getByRoad(road).getData();

        Map<Integer, String> originMap =new HashMap<Integer, String>();
        for (Augmenttask Augmenttask :AugList){
            if(Augmenttask.getOriginsetid()!=null){
               originMap.put(Augmenttask.getId(),Augmenttask.getTaskname());
            }
        }

        Map<Integer, String> AugMap =new HashMap<Integer, String>();
        for (Augmenttask Augmenttask :AugList){
            if(Augmenttask.getAugsetid()!=null){
                AugMap.put(Augmenttask.getId(),Augmenttask.getTaskname());
            }
        }

        JSONObject data = new JSONObject();

        JSONArray originOption =new JSONArray(originMap.size());
        int i=0;
        for (Map.Entry<Integer, String> entry : originMap.entrySet()){
            JSONObject item = new JSONObject();
            item.put("label",entry.getValue());
            item.put("value",entry.getKey());
            originOption.add(i,item);
            i++;
        }

        JSONArray augOption =new JSONArray(AugMap.size());
        i=0;
        for (Map.Entry<Integer, String> entry : AugMap.entrySet()){
            JSONObject item = new JSONObject();
            item.put("label",entry.getValue() + "（扩增后）");
            item.put("value",entry.getKey());
            augOption.add(i,item);
            i++;
        }
        JSONObject item = new JSONObject();
        item.put("label", "空");
        item.put("value",-1);
        augOption.add(0,item);
        originOption.add(0,item);

        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");
        if(type==1){
            testresult.put("data",originOption);
        }else {
            testresult.put("data",augOption);
        }
        return testresult.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/visual/visual",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String visual(HttpServletRequest req, HttpSession session) throws IOException, InterruptedException {
        System.out.println("开始可视化");
        int road =-1;
        int set1 =-1;
        int set2 =-1;
        if(!req.getParameter("road").equals("")){
            road = Integer.parseInt(req.getParameter("road"));
        }

        if(!req.getParameter("set1").equals("")){
            set1 = Integer.parseInt(req.getParameter("set1"));
        }

        if(!req.getParameter("set2").equals("")){
            set2 = Integer.parseInt(req.getParameter("set2"));
        }
        String[] roadfiledir =new String[4];
        roadfiledir[0]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/TwoCars_1/";
        roadfiledir[1]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/TwoCars_2";
        roadfiledir[2]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/ThreeCars_1";
        roadfiledir[3]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/ThreeCars_2";
        List<Visual> originvisuals = (List<Visual>) visualService.getVisual(set1,0).getData();
        List<Visual> augvisuals = (List<Visual>) visualService.getVisual(set2,1).getData();
        System.out.println(originvisuals.size());
        System.out.println(set1);
        if(set1!=-1 && originvisuals.size()==0){
            System.out.println("生成原始可视化gif");
            List<Augmenttask> AugList = (List<Augmenttask>) augmenttaskService.getById(set1).getData();
            Augmenttask augmenttask =AugList.get(0);
            String[] dirs =augmenttask.getOriginsetid().split("!!");
            int flag =0;
            for (int i=0;i<dirs.length;i++){
                String[] dir =dirs[i].split("/");
                String dirroad = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/";
                String dircar = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/"+dir[6]+"/";
                if(flag==0){
                    File directory1 = new File("/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/");
                    FileUtils.cleanDirectory(directory1);
                    File directory2 = new File("/home/chenwei/test_pcl/visual/");
                    FileUtils.cleanDirectory(directory2);
                }
                File directoryroad = new File(dirroad);
                File directorycar = new File(dircar);
                if(!directoryroad.exists()){
                    boolean hasSucceeded = directoryroad.mkdir();
                }
                if(!directorycar.exists()){
                    boolean hasSucceeded = directorycar.mkdir();
                }
                System.out.println(dirs[i]);
                File file= new File(dirs[i]);
                //更改目标路径
                File out1 = new File(dircar, file.getName());
                FileCopyUtils.copy(file, out1);

                String diryaml = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/"+dir[5]+"/"+dir[6]+"/"+dir[7].substring(0,dir[7].length()-4)+".yaml";
                String trueyaml = dircar+dir[7].substring(0,dir[7].length()-4)+".yaml";
                File confile= new File(diryaml);
                //更改目标路径
                File confout = new File(trueyaml);
                FileCopyUtils.copy(confile, confout);

                if(flag==0){
                    File roadfile= new File(roadfiledir[augmenttask.getRoad()]+"/data_protocol.yaml");
                    //更改目标路径
                    File out2 = new File(dirroad, roadfile.getName());
                    FileCopyUtils.copy(roadfile, out2);
                    flag=1;
                }
            }
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," pwd && bash start.sh"},null,new File ("/home/chenwei/opencood/OpenCOOD"));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()) );
            String gifname=null;
            String line;
            while ((line = in.readLine()) != null) {
                gifname =line;
                System.out.println(line);
            }
            System.out.println(gifname);
            in.close();
            Visual visual=new Visual();
            visual.setType(0);
            visual.setAddress(gifname);
            visual.setAugid(set1);
            visualService.insertVisual(visual);
//            BufferedReader error = new BufferedReader(
//                    new InputStreamReader(p.getErrorStream()) );
//            while ((line = error.readLine()) != null) {
//                System.out.println(line);
//            }
//            error.close();
            p.waitFor();

        }

        System.out.println(augvisuals.size());
        System.out.println(set2);
        if(set2!=-1 && augvisuals.size()==0){
            System.out.println("生成扩增可视化gif");
            List<Augmenttask> AugList = (List<Augmenttask>) augmenttaskService.getById(set2).getData();
            Augmenttask augmenttask =AugList.get(0);
            String[] dirs =augmenttask.getAugsetid().split("!!");
            int flag =0;
            for (int i=0;i<dirs.length;i++){
                String[] dir =dirs[i].split("/");
                String dirroad = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/";
                String dircar = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/"+dir[6]+"/";
                if(flag==0){
                    File directory1 = new File("/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/");
                    FileUtils.cleanDirectory(directory1);
                    File directory2 = new File("/home/chenwei/test_pcl/visual/");
                    FileUtils.cleanDirectory(directory2);
                }
                File directoryroad = new File(dirroad);
                File directorycar = new File(dircar);
                if(!directoryroad.exists()){
                    boolean hasSucceeded = directoryroad.mkdir();
                }
                if(!directorycar.exists()){
                    boolean hasSucceeded = directorycar.mkdir();
                }
                System.out.println(dirs[i]);
                File file= new File(dirs[i]);
                //更改目标路径
                File out1 = new File(dircar, file.getName());
                FileCopyUtils.copy(file, out1);

                String diryaml = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/"+dir[5]+"/"+dir[6].substring(3)+"/"+dir[7].substring(0,dir[7].length()-4)+".yaml";
                String trueyaml = dircar+dir[7].substring(0,dir[7].length()-4)+".yaml";
                File confile= new File(diryaml);
                //更改目标路径
                File confout = new File(trueyaml);
                FileCopyUtils.copy(confile, confout);

                if(flag==0){
                    File roadfile= new File(roadfiledir[augmenttask.getRoad()]+"/data_protocol.yaml");
                    //更改目标路径
                    File out2 = new File(dirroad, roadfile.getName());
                    FileCopyUtils.copy(roadfile, out2);
                    flag=1;
                }
            }
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," pwd && bash start.sh"},null,new File ("/home/chenwei/opencood/OpenCOOD"));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()) );
            String gifname=null;
            String line;
            while ((line = in.readLine()) != null) {
                gifname =line;
                System.out.println(line);
            }
            System.out.println(gifname);
            in.close();
            Visual visual=new Visual();
            visual.setType(1);
            visual.setAddress(gifname);
            visual.setAugid(set1);
            visualService.insertVisual(visual);
//            BufferedReader error = new BufferedReader(
//                    new InputStreamReader(p.getErrorStream()) );
//            while ((line = error.readLine()) != null) {
//                System.out.println(line);
//            }
//            error.close();
            p.waitFor();
        }

        if(set1!=-1){
            if(originvisuals.size()==0){
                originvisuals = (List<Visual>) visualService.getVisual(set1,0).getData();
            }
        }

        if(set1!=-1){
            if(augvisuals.size()==0){
                augvisuals = (List<Visual>) visualService.getVisual(set2,1).getData();
            }
        }

        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/visual/getimage",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getimage(HttpServletRequest req, HttpSession session) {
        int set1 =-1;
        int set2 =-1;
        if(!req.getParameter("set1").equals("")){
            set1 = Integer.parseInt(req.getParameter("set1"));
        }
        if(!req.getParameter("set2").equals("")){
            set2 = Integer.parseInt(req.getParameter("set2"));
        }

        JSONArray reportOption =new JSONArray(2);
        if(set1!=-1){
            List<Visual> visuals1 = (List<Visual>) visualService.getVisual(set1,0).getData();
            String[] address =visuals1.get(0).getAddress().split("/");
            reportOption.add("http://localhost:3000/pages/resource/visual/"+address[address.length-1]);
        }
        if(set2!=-1){
            List<Visual> visuals2 = (List<Visual>) visualService.getVisual(set2,1).getData();
            String[] address =visuals2.get(0).getAddress().split("/");
            reportOption.add("http://localhost:3000/pages/resource/visual/"+address[address.length-1]);
        }
        JSONObject image = new JSONObject();
        image.put("imageList",reportOption);
        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");
        testresult.put("data",image);
        System.out.println(testresult);
        return testresult.toJSONString();
    }

}
