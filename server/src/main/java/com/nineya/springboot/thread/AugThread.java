package com.nineya.springboot.thread;

import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Testtask;
import com.nineya.springboot.service.AugmenttaskService;
import com.nineya.springboot.service.TesttaskService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.Random;



public class AugThread extends Thread{
    private Augmenttask augmenttask;

    private AugmenttaskService augmenttaskService;

    private Testtask testtask;
    private TesttaskService testtaskService;

    public AugThread(Augmenttask augmenttask , AugmenttaskService augmenttaskService){
        this.augmenttask = augmenttask;
        this.augmenttaskService = augmenttaskService;
    }

    public AugThread(Augmenttask augmenttask, AugmenttaskService augmenttaskService, Testtask testtask, TesttaskService testtaskService) {
        this.augmenttask = augmenttask;
        this.augmenttaskService = augmenttaskService;
        this.testtask=testtask;
        this.testtaskService=testtaskService;
    }

    @Override
    public void run() {
        String command ="sudo /home/chenwei/test_pcl/build ./test_pcl 1 1 1";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("等待3秒钟");
        try {
            File directory1 = new File("/home/chenwei/test_pcl/opv2v_origin_data/car1");
            File directory2 = new File("/home/chenwei/test_pcl/opv2v_origin_data/car2");
            File directory3 = new File("/home/chenwei/test_pcl/augmentation_data/default");
            //Process p =Runtime.getRuntime().exec(new String[]{"/home/chenwei/test_pcl/build/","-c","./test_pcl 1 1 1"});
            //Process p = Runtime.getRuntime().exec("pwd" );


            String[] dirs =augmenttask.getOriginsetid().split("!!");

            if(augmenttask.getWeatherintensity()==null){
                Random r = new Random();
                int i1 = r.nextInt(9);
                augmenttask.setWeatherintensity((double) i1);
                augmenttask.setParameter((double) i1);
            }

            for (int i=0;i<dirs.length;i++){
                FileUtils.cleanDirectory(directory1);
                FileUtils.cleanDirectory(directory2);
                File myFoo = new File("/home/chenwei/test_pcl/opv2v_origin_data/data.txt");
                FileOutputStream fooStream = new FileOutputStream(myFoo, false); // true to append // false to overwrite.
                String weather;
                if(augmenttask.getWeathertype()==0){
                    weather = "rain";
                } else if (augmenttask.getWeathertype()==1) {
                    weather = "snow";
                }else {
                    weather = "fog";
                }
                fooStream.write((weather+"\n").getBytes());
                String[] d=dirs[i].split("/");
                byte[] myBytes = (d[d.length-1].substring(0,d[d.length-1].length()-4)+"\n").getBytes();
                fooStream.write(myBytes);
                fooStream.close();
                File file= new File(dirs[i]);
                //更改目标路径
                File out1 = new File("/home/chenwei/test_pcl/opv2v_origin_data/car1", file.getName());
                File out2 = new File("/home/chenwei/test_pcl/opv2v_origin_data/car2", file.getName());
                FileCopyUtils.copy(file, out1);
                FileCopyUtils.copy(file, out2);
                Process p = Runtime.getRuntime().exec("./test_pcl "
                        +augmenttask.getWeathertype()+" "+augmenttask.getWeatherintensity()+" "+augmenttask.getParameter() );
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()) );
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                p.waitFor();

                File newfile= new File("/home/chenwei/test_pcl/augmentation_data/default/"+weather.toString()+"/car1/01/"+file.getName());
                //更改目标路径
                File out3 = new File("/home/chenwei/test_pcl/upload/"+d[5]+"/aug"+d[6]+"/", file.getName());
                FileCopyUtils.copy(newfile, out3);
                if(augmenttask.getAugsetid()==null){
                    augmenttask.setAugsetid(out3.getAbsolutePath());
                }else {
                    augmenttask.setAugsetid(augmenttask.getAugsetid()+"!!"+out3.getAbsolutePath());
                }
                FileUtils.cleanDirectory(directory3);
            }
            testtaskService.insertTestTask(testtask);
            augmenttask.setStatus(2);
            augmenttaskService.modifytask(augmenttask.getId(),augmenttask);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
