package com.nineya.springboot.thread;

import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Report;
import com.nineya.springboot.entity.Testtask;
import com.nineya.springboot.service.AugmenttaskService;
import com.nineya.springboot.service.ReportService;
import com.nineya.springboot.service.TesttaskService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.Thread.sleep;

public class ModelTestThread extends Thread{


    private Testtask testtask;

    private TesttaskService testtaskService;

    private ReportService reportService;

    private AugmenttaskService augmenttaskService;

    public ModelTestThread(Testtask testtask, TesttaskService testtaskService, ReportService reportService ,AugmenttaskService augmenttaskService){
        this.testtask = testtask;
        this.testtaskService = testtaskService;
        this.reportService = reportService;
        this.augmenttaskService=augmenttaskService;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            String[] roadfiledir =new String[4];
            roadfiledir[0]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/TwoCars_1/";
            roadfiledir[1]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/TwoCars_2";
            roadfiledir[2]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/ThreeCars_1";
            roadfiledir[3]="/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver/ThreeCars_2";
            System.out.println("开始执行模型测试");
            int id =testtask.getAugtaskid();
            List<Augmenttask> AugList = (List<Augmenttask>) augmenttaskService.getById(id).getData();
            Augmenttask augmenttask =AugList.get(0);
            String[] origindirs =augmenttask.getOriginsetid().split("!!");
            String[] augdirs =augmenttask.getAugsetid().split("!!");
            String errorAugLidar =null;
            String errorImage=null;
            String testinfo=null;
            String falseAlarmStr=null;
            String missRateStr=null;
            String yawStr=null;
            String bevStr=null;
            double bevAp=0;
            int di=0;
            if(augmenttask.getRoad()<=1){
                di=2;
            }else {
                di=3;
            }
            for (int j=0;j<origindirs.length/di;j++){
                int flag=0;
                for (int i=j;i<origindirs.length;i=i+origindirs.length/di){
                    String[] dir =origindirs[i].split("/");
                    String dirroad = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/";
                    String dircar = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/"+dir[6]+"/";
                    if(flag==0){
                        File directory1 = new File("/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/");
                        FileUtils.cleanDirectory(directory1);
                        File directory2 = new File("/home/chenwei/opencood/OpenCOOD/opencood/models/v2vnet/vis/");
                        FileUtils.cleanDirectory(directory2);
                        File directory0 = new File("/home/chenwei/test_pcl/test/");
                        FileUtils.cleanDirectory(directory0);
                    }
                    File directoryroad = new File(dirroad);
                    File directorycar = new File(dircar);
                    if(!directoryroad.exists()){
                        boolean hasSucceeded = directoryroad.mkdir();
                    }
                    if(!directorycar.exists()){
                        boolean hasSucceeded = directorycar.mkdir();
                    }
                    File file= new File(origindirs[i]);
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
                Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," pwd && bash detect.sh"},null,new File ("/home/chenwei/opencood/OpenCOOD"));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()) );
                String gifname=null;
                String line;
                while ((line = in.readLine()) != null) {
                    gifname =line;
                }
                in.close();
                p.waitFor();

                File directory5 = new File("/home/chenwei/test_pcl/test/");
                File[] testRe =directory5.listFiles();
                File directory4 = new File("/home/chenwei/opencood/OpenCOOD/opencood/models/v2vnet/vis/");
                File[] testReNew =directory4.listFiles();
                File testout = new File("/home/chenwei/test_pcl/test/", String.valueOf(testRe.length)+".png");
                FileCopyUtils.copy(testReNew[0], testout);

                flag=0;
                String erroraug=null;
                for (int i=j;i<origindirs.length;i=i+origindirs.length/di){
                    String[] dir =augdirs[i].split("/");
                    String dirroad = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/";
                    String dircar = "/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/"+dir[5]+"/"+dir[6]+"/";
                    if(flag==0){
                        File directory1 = new File("/home/chenwei/opencood/OpenCOOD/opv2v_data_dumping/test_culver_city/");
                        FileUtils.cleanDirectory(directory1);
                    }
                    File directoryroad = new File(dirroad);
                    File directorycar = new File(dircar);
                    if(!directoryroad.exists()){
                        boolean hasSucceeded = directoryroad.mkdir();
                    }
                    if(!directorycar.exists()){
                        boolean hasSucceeded = directorycar.mkdir();
                    }
                    File file= new File(augdirs[i]);
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
                    if(erroraug!=null){
                        erroraug =erroraug+"!!"+augdirs[i];
                    }else {
                        erroraug =augdirs[i];
                    }
                }
                Process p1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," pwd && bash detect.sh"},
                        null,new File ("/home/chenwei/opencood/OpenCOOD"));
                BufferedReader in1 = new BufferedReader(
                        new InputStreamReader(p1.getInputStream()) );
                String[] gifname1=new String[6];
                int i =0;
                String line1;
                while ((line1 = in1.readLine()) != null) {
                    if(i!=0){
                        System.out.println(line1);
                        gifname1[i-1]=line1;
                        i++;
                    }
                    if(line1.equals("开始输出结果")){
                        i++;
                    }
                }
                BufferedReader error = new BufferedReader(
                        new InputStreamReader(p.getErrorStream()) );
                String errorline;
                while ((errorline = error.readLine()) != null) {
                    System.out.println(errorline);
                }
                error.close();
                in1.close();
                p1.waitFor();

                File directory6 = new File("/home/chenwei/test_pcl/test/");
                File[] testRe1 =directory6.listFiles();
                File directory7 = new File("/home/chenwei/opencood/OpenCOOD/opencood/models/v2vnet/vis/");
                File[] testReNew1 =directory7.listFiles();
                File testout1 = new File("/home/chenwei/test_pcl/test/", String.valueOf(testRe1.length)+".png");
                FileCopyUtils.copy(testReNew1[0], testout1);

                String originGrade =gifname.substring(gifname.length()-4);
                String augGrade =gifname1[4].substring(gifname1[4].length()-4);
                System.out.println(originGrade);
                System.out.println(augGrade);
                Double falseAlarm = Double.valueOf(gifname1[0]);
                Double missRate = Double.valueOf(gifname1[1]);
                Double yaw = Double.valueOf(gifname1[2]);
                String info =gifname1[3];
                bevAp= Double.parseDouble(gifname1[4].substring(gifname1[4].length()-4));
                System.out.println("falseAlarm "+Double.valueOf(gifname1[0]));
                System.out.println("missRate "+Double.valueOf(gifname1[1]));
                System.out.println("yaw "+Double.valueOf(gifname1[2]));
                System.out.println("info "+gifname1[3]);
                if(Double.parseDouble(originGrade)-Double.parseDouble(augGrade)>0.09 || yaw >0){
                    File[] testRe2 =directory6.listFiles();
                    File directory2 = new File("/home/chenwei/amis/amis-admin-master/pages/resource/test/origin/");
                    File[] preOriFiles =directory2.listFiles();
                    File directory3 = new File("/home/chenwei/amis/amis-admin-master/pages/resource/test/aug/");
                    File[] preAugFiles =directory3.listFiles();
                    File out1 = new File("/home/chenwei/amis/amis-admin-master/pages/resource/test/origin/", String.valueOf(preOriFiles.length)+".png");
                    File out2 = new File("/home/chenwei/amis/amis-admin-master/pages/resource/test/aug/", String.valueOf(preAugFiles.length)+".png");
                    if(testRe2[0].getName().startsWith("1")){
                        FileCopyUtils.copy(testRe2[0], out2);
                        FileCopyUtils.copy(testRe2[1], out1);
                    }else {
                        FileCopyUtils.copy(testRe2[0], out1);
                        FileCopyUtils.copy(testRe2[1], out2);
                    }
                    if(errorImage!=null){
                        errorImage=errorImage+"!!"+out1.getAbsolutePath()+"!!"+out2.getAbsolutePath();
                    }else {
                        errorImage=out1.getAbsolutePath()+"!!"+out2.getAbsolutePath();
                    }

                    if(testinfo==null){
                        testinfo=info;
                        falseAlarmStr= String.valueOf(falseAlarm);
                        missRateStr= String.valueOf(missRate);
                        yawStr= String.valueOf(yaw);
                        bevStr= String.valueOf(bevAp);
                    }else {
                        testinfo = testinfo+"!!"+info;
                        falseAlarmStr=falseAlarmStr+"!!"+ String.valueOf(falseAlarm);
                        missRateStr= missRateStr+"!!"+ String.valueOf(missRate);
                        yawStr= yawStr+"!!"+ String.valueOf(yaw);
                        bevStr= bevStr+"!!"+ String.valueOf(bevAp);
                    }

                    if(errorAugLidar!=null){
                        errorAugLidar =errorAugLidar+"!!"+erroraug;
                    }else {
                        errorAugLidar =erroraug;
                    }
                }
            }


            Report report = new Report();

            report.setImage(errorImage);
            report.setName(testtask.getName());
            report.setAugid(testtask.getAugtaskid());
            report.setTestid(testtask.getId());
            report.setBev(bevStr);
            report.setMiss(missRateStr);
            report.setAlarm(falseAlarmStr);
            report.setYaw(yawStr);
            report.setTestInfo(testinfo);
            reportService.insertReport(report);

            testtask.setErrorsetid(errorAugLidar);
            testtask.setStatus(2);
            testtaskService.modifytask( testtask.getId(),testtask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
