package com.nineya.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nineya.springboot.entity.Augmenttask;
import com.nineya.springboot.entity.Report;
import com.nineya.springboot.entity.Testtask;
import com.nineya.springboot.service.AugmenttaskService;
import com.nineya.springboot.service.ReportService;
import com.nineya.springboot.service.TesttaskService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cw
 * @since 2023-05-07
 */
@RestController
public class ReportController {

    @Autowired
    ReportService reportService;
    @Autowired
    AugmenttaskService augmenttaskService;
    @Autowired
    TesttaskService testtaskService;
    @ResponseBody
    @RequestMapping(value = "/report/getAll",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getReport(HttpServletRequest req, HttpSession session) {
        System.out.println("获取测试报告列表");
        List<Report> reportList = (List<Report>) reportService.getAll().getData();
        Map<Integer, String> reportMap =new HashMap<Integer, String>();
        for (Report report  :reportList){
            reportMap.put(report.getId(),report.getName());
        }
        JSONObject data = new JSONObject();
        JSONArray reportOption =new JSONArray(reportMap.size());
        int i=0;
        for (Map.Entry<Integer, String> entry : reportMap.entrySet()){
            JSONObject item = new JSONObject();
            item.put("label",entry.getValue());
            item.put("value",entry.getKey());
            reportOption.add(i,item);
            i++;
        }
        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");
        testresult.put("data",reportOption);
        return testresult.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/report/getImageById",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getImage(HttpServletRequest req, HttpSession session) {
        int id;
        if(req.getParameter("id").equals("")){
            return null;
        }else {
            id = Integer.parseInt(req.getParameter("id"));
        }
        List<Report> reportList = (List<Report>) reportService.getById(id).getData();
        Report report = reportList.get(0);
        if(report.getImage()==null){
            return null;
        }
        String[] imgURL = report.getImage().split("!!");
        JSONArray reportOption =new JSONArray(imgURL.length);
        for (int j =1;j<imgURL.length;j=j+2){
            String[] address =imgURL[j].split("/");
            //JSONObject data = new JSONObject();
            // data.put("iamge",imgURL[j]);
            reportOption.add("http://localhost:3000/pages/resource/test/aug/"+address[address.length-1]);
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

    @ResponseBody
    @RequestMapping(value = "/report/getGrade",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getGrade(HttpServletRequest req, HttpSession session) {
        System.out.println("获取测试得分");
        int id;
        int type;
        if(req.getParameter("id").equals("")){
            return null;
        }else {
            id = Integer.parseInt(req.getParameter("id"));
        }
        type= Integer.parseInt(req.getParameter("type"));
        List<Report> reportList = (List<Report>) reportService.getById(id).getData();
        Report report = reportList.get(0);

        JSONArray reportOption =new JSONArray(1);
        JSONArray reportOption1 =new JSONArray(1);
        JSONObject data = new JSONObject();
        int augid =report.getAugid();
        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.getById(augid).getData();
        String weather;
        if(augTaskList.get(0).getWeathertype()==0){
            weather = "雨";
        }else if(augTaskList.get(0).getWeathertype()==1){
            weather = "雪";
        }else {
            weather = "雾";
        }

        String[] alarms =report.getAlarm().split("!!");
        String[] misss =report.getMiss().split("!!");
        String[] yaws =report.getYaw().split("!!");
        String[] bevs =report.getBev().split("!!");
        String[] infos =report.getTestInfo().split("!!");

        for(int i=0;i<alarms.length;i++){
            String[] info =infos[i].split(" ");
            JSONObject data1 = new JSONObject();
            data1.put("f",info[0]);
            data1.put("g",info[1]);
            data1.put("m",info[2]);
            data1.put("h",info[3]);
            data1.put("i",info[4]);
            data1.put("j",info[5]);
            data1.put("k",bevs[i]);
            reportOption1.add(data1);
        }

        Double alarm =0.0;
        Double miss =0.0;
        Double yaw =0.0;
        Double bev =0.0;
        for (int i=0;i<alarms.length;i++){
            alarm = alarm+Double.valueOf(alarms[i]);
            miss = miss+Double.valueOf(misss[i]);
            yaw = yaw+Double.valueOf(yaws[i]);
            bev = bev+Double.valueOf(bevs[i]);
        }
        alarm=alarm/alarms.length;
        miss=miss/alarms.length;
        yaw=yaw/alarms.length;
        bev=bev/alarms.length;

        data.put("e",weather);
        data.put("f",alarm);
        data.put("g",miss);
        data.put("h",yaw);
        data.put("i",bev);

        reportOption.add(data);
        JSONObject grade = new JSONObject();
        grade.put("rows1",reportOption);

        JSONObject grade1 = new JSONObject();
        grade1.put("rows1",reportOption1);

        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");

        System.out.println(testresult);

        JSONObject chartre = new JSONObject();
        JSONArray chart =new JSONArray(2);

        if(type==0){
            testresult.put("data",grade);
        }else if(type == 1){
            String[] label ={"false alarm", "miss rate"};
            double[] falseMiss = {alarm, miss};
            chart.add(label);
            chart.add(falseMiss);
            chartre.put("source",chart);
            testresult.put("data",chartre);
        }else if(type == 2) {
            String[] label ={"yaw accuracy", "bev AP"};
            double[] falseMiss = {yaw, bev};
            chart.add(label);
            chart.add(falseMiss);
            chartre.put("source",chart);
            testresult.put("data",chartre);
        }else {
            testresult.put("data",grade1);
        }
        System.out.println(testresult.toJSONString());
        return testresult.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/report/download",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String download(HttpServletRequest req, HttpSession session , HttpServletResponse response) throws IOException, InvalidFormatException {


        System.out.println("测试报告下载");
        int id;
        if(req.getParameter("id").equals("")){
            return null;
        }else {
            id = Integer.parseInt(req.getParameter("id"));
        }
        System.out.println(id);

        String reportdir = "/home/chenwei/amis/amis-admin-master/pages/resource/report/"+id;
        File reportfiles = new File(reportdir);
        if(!reportfiles.exists()){
            boolean hasSucceeded = reportfiles.mkdir();
        }else {
            System.out.println("报告已存在");
            JSONObject testresult = new JSONObject();
            testresult.put("status",0);
            testresult.put("msg","");
            JSONObject chartre = new JSONObject();
            chartre.put("source","http://localhost:3000/pages/resource/report/"+id+".zip");
            testresult.put("data",chartre);
            return testresult.toJSONString();
        }

        List<File> files =new ArrayList<>();
        List<Report> reportList = (List<Report>) reportService.getById(id).getData();
        int augid =reportList.get(0).getAugid();
        List<Testtask> testtasks = (List<Testtask>) testtaskService.getByAugId(augid).getData();
        List<Augmenttask> augmenttasks = (List<Augmenttask>) augmenttaskService.getById(augid).getData();
        int cars =0;
        if(augmenttasks.get(0).getRoad()<2){
            cars=2;
        }else {
            cars=3;
        }
        if(testtasks.get(0).getErrorsetid()!=null){
            String[] errorset =testtasks.get(0).getErrorsetid().split("!!");
            for (int i =0;i<errorset.length;i++){
                File file=new File(errorset[i]);
                File out2=null;
                //更改目标路径
                if(cars==2){
                    if(i%2==0){
                        out2 = new File(reportdir, file.getName().substring(0,file.getName().length()-4)+"(car1).pcd");
                    }else {
                        out2 = new File(reportdir, file.getName().substring(0,file.getName().length()-4)+"(car2).pcd");
                    }
                }else {
                    if(i%3==0){
                        out2 = new File(reportdir, file.getName().substring(0,file.getName().length()-4)+"(car1).pcd");
                    }else if(i%3==1){
                        out2 = new File(reportdir, file.getName().substring(0,file.getName().length()-4)+"(car2).pcd");
                    }else {
                        out2 = new File(reportdir, file.getName().substring(0,file.getName().length()-4)+"(car3).pcd");
                    }
                }

                FileCopyUtils.copy(file, out2);
                file =out2;
                files.add(file);
            }
        }


        Report report = reportList.get(0);
        List<Augmenttask> augTaskList = (List<Augmenttask>) augmenttaskService.getById(augid).getData();
        String weather;
        if(augTaskList.get(0).getWeathertype()==0){
            weather = "rain";
        }else if(augTaskList.get(0).getWeathertype()==1){
            weather = "snow";
        }else {
            weather = "fog";
        }

        String[] alarms =report.getAlarm().split("!!");
        String[] misss =report.getMiss().split("!!");
        String[] yaws =report.getYaw().split("!!");
        String[] bevs =report.getBev().split("!!");
        String[] infos =report.getTestInfo().split("!!");

        String[][] rows =new String[alarms.length][8];
        String[][] row1 =new String[1][5];
        String[][] row2 =new String[1][6];
        for(int i=0;i<alarms.length;i++){
            String[] info =infos[i].split(" ");
            rows[i][0]=weather;
            rows[i][1]=info[0];
            rows[i][2]=info[1];
            rows[i][3]=info[2];
            rows[i][4]=info[3];
            rows[i][5]=info[4];
            rows[i][6]=info[5];
            rows[i][7]=bevs[i];
        }

        Double alarm =0.0;
        Double miss =0.0;
        Double yaw =0.0;
        Double bev =0.0;
        for (int i=0;i<alarms.length;i++){
            alarm = alarm+Double.valueOf(alarms[i]);
            miss = miss+Double.valueOf(misss[i]);
            yaw = yaw+Double.valueOf(yaws[i]);
            bev = bev+Double.valueOf(bevs[i]);
        }
        alarm=alarm/alarms.length;
        miss=miss/alarms.length;
        yaw=yaw/alarms.length;
        bev=bev/alarms.length;
        Augmenttask augmenttask = augmenttasks.get(0);
        String road =null;
        if(augmenttask.getRoad()<2){
            road="双车场景"+(augmenttask.getRoad()+1);
        }else {
            road="三车场景"+(augmenttask.getRoad()-1);
        }
        row1[0]= new String[]{weather, String.valueOf(alarm), String.valueOf(miss), String.valueOf(yaw), String.valueOf(bev)};
        row2[0]= new String[]{augmenttask.getTaskname(), String.valueOf(augmenttask.getOriginsetid().split("!!").length), weather, String.valueOf(augmenttask.getWeatherintensity()), String.valueOf(augmenttask.getParameter()),road};

        String[] headers = new String[]{ "算子", "实际车辆数", "检测车辆数", "匹配车辆数", "虚检车辆数", "漏检车辆数", "姿态错误车辆数","bev" };
        String[] headers1 = new String[]{ "算子", "虚检率", "漏检率", "姿态错误率", "bev AP"};
        String[] headers2 = new String[]{ "任务名", "点云文件数", "变异算子", "天气强度", "特有参数", "场景"};

        // 创建一个新文档。
        Document document = new Document(PageSize.LETTER.rotate());
        try {
            // 获取PdfWriter的实例并创建Table.pdf文件
            // 作为输出。
            PdfWriter.getInstance(document,
                    new FileOutputStream(new File("/home/chenwei/amis/amis-admin-master/pages/resource/report/"+id+"/测试报告.pdf")));
            document.open();
            //创建一个PdfPTable实例。之后我们转型
            //将标头和行数组转换为PdfPCell对象。什么时候
            // 每个表格行都是完整的，我们必须调用
            // table.completeRow()方法。
            //
            // 为了更好地呈现，我们还设置了单元字体名称，
            //大小和重量。我们还定义了背景填充
            // 为细胞。

            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font fontRow = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            PdfPTable table = new PdfPTable(headers.length);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setPhrase(new Phrase(header.toUpperCase(), setChineseFont()));
                table.addCell(cell);
            }
            table.completeRow();
            for (String[] row : rows) {
                for (String data : row) {
                    Phrase phrase = new Phrase(data, setChineseFont());
                    table.addCell(new PdfPCell(phrase));
                }
                table.completeRow();
            }

            PdfPTable table1 = new PdfPTable(headers1.length);
            for (String header : headers1) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setPhrase(new Phrase(header.toUpperCase(), setChineseFont()));
                table1.addCell(cell);
            }
            table1.completeRow();
            for (String[] row : row1) {
                for (String data : row) {
                    Phrase phrase = new Phrase(data, setChineseFont());
                    table1.addCell(new PdfPCell(phrase));
                }
                table1.completeRow();
            }

            PdfPTable table2 = new PdfPTable(headers2.length);
            for (String header : headers2) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setPhrase(new Phrase(header.toUpperCase(), setChineseFont()));
                table2.addCell(cell);
            }
            table2.completeRow();
            for (String[] row : row2) {
                for (String data : row) {
                    Phrase phrase = new Phrase(data, setChineseFont());
                    table2.addCell(new PdfPCell(phrase));
                }
                table2.completeRow();
            }

            document.addTitle("PDF Table Demo");

            Paragraph par3=new Paragraph("预测结果展示",setChineseFont());

            document.add(new Paragraph("         ", setChineseFont()));
            document.add(new Paragraph("         ", setChineseFont()));
            par3=new Paragraph("扩增详情",setChineseFont());
            //设置局中对齐
            par3.setAlignment(Element.ALIGN_CENTER);
            document.add(par3);
            document.add(new Paragraph("         ", setChineseFont()));
            document.add(table2);

            document.add(new Paragraph("         ", setChineseFont()));
            document.add(new Paragraph("         ", setChineseFont()));
            par3=new Paragraph("预测检测结果",setChineseFont());
            //设置局中对齐
            par3.setAlignment(Element.ALIGN_CENTER);
            document.add(par3);
            document.add(new Paragraph("         ", setChineseFont()));
            document.add(table);
            document.add(new Paragraph("         ", setChineseFont()));
            document.add(new Paragraph("         ", setChineseFont()));
            par3=new Paragraph("预测检测评估",setChineseFont());
            //设置局中对齐
            par3.setAlignment(Element.ALIGN_CENTER);
            document.add(par3);
            document.add(new Paragraph("         ", setChineseFont()));
            document.add(table1);

            String[] filename = report.getImage().split("!!");
            for (int i=0;i<filename.length;i++){
                File out2=null;
                File iamgefile =new File(filename[i]);
                if(i%2==0){
                    par3=new Paragraph("原始/预测结果"+(i/2+1),setChineseFont());
                    par3.setAlignment(Element.ALIGN_CENTER);
                    document.add(par3);
                    out2 = new File(reportdir, iamgefile.getName());
                }else {
                    out2 = new File(reportdir, iamgefile.getName().substring(0,iamgefile.getName().length()-4)+"(aug).png");
                }
                FileCopyUtils.copy(iamgefile, out2);
                iamgefile =out2;
                files.add(iamgefile);

                //设置局中对齐

                Image image = Image.getInstance(filename[i]);
                image.scaleToFit(1200, 600);//自定义大小
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            }

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        File reportfile = new File("/home/chenwei/amis/amis-admin-master/pages/resource/report/"+id+"/测试报告.pdf");
        files.add(reportfile);
        JSONObject testresult = new JSONObject();
        testresult.put("status",0);
        testresult.put("msg","");
        JSONObject chartre = new JSONObject();

        String zipDir ="/home/chenwei/amis/amis-admin-master/pages/resource/report/"+id+".zip";
        zipFile(files,zipDir);
        chartre.put("source","http://localhost:3000/pages/resource/report/"+id+".zip");
        testresult.put("data",chartre);
        System.out.println(testresult.toJSONString());
        return testresult.toJSONString();

    }

    private static Font setChineseFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {
            // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, 12, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }


    private static void zipFile(List<File> fileList , String dir) throws IOException {
        for (int i =0;i<fileList.size();i++){
            System.out.println(fileList.get(i).getName());
        }
        // 文件的压缩包路径
        String zipPath = dir;
        // 获取文件压缩包输出流
        try (OutputStream outputStream = new FileOutputStream(zipPath);
             CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream,new Adler32());
             ZipOutputStream zipOut = new ZipOutputStream(checkedOutputStream)){
            for (File file : fileList) {
                // 获取文件输入流
                InputStream fileIn = new FileInputStream(file);
                // 使用 common.io中的IOUtils获取文件字节数组
                byte[] bytes = getFileByteArray(file);
                // 写入数据并刷新
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                zipOut.write(bytes,0,bytes.length);
                zipOut.flush();
                fileIn.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getFileByteArray(File file) {
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        byte[] buffer = null;
        try (FileInputStream fi = new FileInputStream(file)) {
            buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
