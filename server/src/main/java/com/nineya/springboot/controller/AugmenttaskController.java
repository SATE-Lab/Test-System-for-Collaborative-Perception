
package com.nineya.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nineya.springboot.common.R;
import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Pointcloud;
import com.nineya.springboot.service.AugmenttaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cw
 * @since 2023-04-05
 */
@RestController
public class AugmenttaskController {
    @Autowired
    AugmenttaskService augmenttaskService;

    @ResponseBody
    @RequestMapping(value = "/augmenttask/create",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public R create(@RequestBody Map<String, String> map, HttpServletRequest req, HttpSession session) {
        Augmenttask augmenttask=new Augmenttask();

        System.out.println("配置方案提交");
        int weather=0;
        double intensity=0.0;
        double para=0.0;
        String car1="";
        String car2="";
        String car3="";
        String taskname="";

        if(map.get("weather_random_select")!=null){
            weather = Integer.parseInt(map.get("weather_random_select"));
            augmenttask.setWeathertype(weather);

        }else {
            weather = Integer.parseInt(map.get("weather_custom_select2"));
            intensity= Double.parseDouble(map.get("intensity"));
            para = Double.parseDouble(map.get("para"));

            augmenttask.setWeathertype(weather);
            augmenttask.setWeatherintensity(intensity);
            augmenttask.setParameter(para);
        }

        String car="";
        if(map.get("towcar_car1")!=null){
            car1=map.get("towcar_car1");
            car2=map.get("towcar_car2");
            car = car1+"!!"+car2;
            car =car.replace(",","!!");
        }else {
            car1=map.get("threecar_car1");
            car2=map.get("threecar_car2");
            car3=map.get("threecar_car3");
            car = car1+"!!"+car2+"!!"+car3;
            car=car.replace(",","!!");
        }
        taskname=map.get("taskname");
        augmenttask.setTaskname(taskname);
        augmenttask.setStatus(1);
        augmenttask.setOriginsetid(car);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        augmenttask.setBegintime(timestamp);
        int road = Integer.parseInt(req.getParameter("road"));
        augmenttask.setRoad(road);

        System.out.println(augmenttask.toString());
        return augmenttaskService.insertAugmentTask(augmenttask);
    }

    @ResponseBody
    @RequestMapping(value = "/augmenttask/getAll",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getTask(HttpServletRequest req, HttpSession session) {
        System.out.println("扩增任务初始化");
        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.getAugTask().getData();
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray(3);
        JSONObject data = new JSONObject();
        int i=0;
        for (Augmenttask augmenttask:augTaskList){
            String time =augmenttask.getBegintime().toString();
            JSONObject item = new JSONObject();
            item.put("id",augmenttask.getId());
            item.put("a",augmenttask.getTaskname());
            item.put("b",time);
            item.put("e",augmenttask.getStatus());
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

    @ResponseBody
    @RequestMapping(value = "/augmenttask/getById",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getById(HttpServletRequest req, HttpSession session) {

        System.out.println("扩增任务详情");
        int id = Integer.parseInt(req.getParameter("id"));
        int type = Integer.parseInt(req.getParameter("type"));
        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.getById(id).getData();
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray(3);
        JSONObject data = new JSONObject();

        for (Augmenttask augmenttask:augTaskList){
            JSONObject item = new JSONObject();
            String car =augmenttask.getOriginsetid();
            String sence[] = car.split("!!");
            int i=0;
            int count=0;
            String road ="";
            for (;i<sence.length;i++){
                String cars[] =sence[i].split(",");
                count=count+cars.length;
                String dir[] =cars[0].split("/");
                if(dir[dir.length-3].equals("TwoCars_1")){
                    road="双车场景1";
                }else if (dir[dir.length -3].equals("TwoCars_2")){
                    road="双车场景2";
                }else if (dir[dir.length -3].equals("ThreeCars_1")){
                    road="三车场景1";
                }else {
                    road="三车场景2";
                }

            }
            item.put("a",augmenttask.getTaskname());
            item.put("b",count);
            item.put("c",augmenttask.getBegintime().toString());
            String WeatherType ="";
            if(augmenttask.getWeathertype()==0){
                WeatherType ="雨";
            }else if (augmenttask.getWeathertype()==1){
                WeatherType ="雪";
            }else {
                WeatherType ="雾";
            }
            item.put("d",WeatherType);


            JSONObject item2 = new JSONObject();
            if(augmenttask.getWeatherintensity()==null){
                item2.put("e","未生成");
                item2.put("n","未生成");
                item2.put("z","随机");
            }else {
                item2.put("e",augmenttask.getWeatherintensity());
                item2.put("n",augmenttask.getParameter());
                item2.put("z","自定义");
            }


            item2.put("v",road);

            if (type==1){
                array.add(0,item);
            }else {
                array.add(0,item2);
            }
        }

        result.put("status",0);
        result.put("msg", "");
        result.put("data", array);
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/augmenttask/deleteById",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String deleteById(HttpServletRequest req, HttpSession session) {

        System.out.println("删除扩增任务");
        int id = Integer.parseInt(req.getParameter("id"));

        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.deleteById(id).getData();
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray(3);
        JSONObject data = new JSONObject();
        int i=0;
        for (Augmenttask augmenttask:augTaskList){
            JSONObject item = new JSONObject();
            item.put("id",augmenttask.getId());
            item.put("a",augmenttask.getTaskname());
            item.put("b",augmenttask.getBegintime());
            item.put("e",augmenttask.getStatus());
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

