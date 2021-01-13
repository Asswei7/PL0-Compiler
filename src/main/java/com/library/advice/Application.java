package com.library.advice;

import com.configPro.JsonPro;
import com.database.CreateTable;
import com.database.Query;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 *
 * @author Asswei
 */
public class Application {
    public static final String JSON_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\resources\\config.json";
    public static final String FILE_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\resources\\config.json";
    public static void main(String[] args) throws Exception {
        Map<Pair<String,String>,Integer> libraryMap = new HashMap<>();


        String libraryName = "java.awt.geom.Line2D";
        List<String> exceptLibrary = new ArrayList<>(20);
        //通过前端读入输入的已经输入的类库
        exceptLibrary.add("java.awt.Point");
        exceptLibrary.add("java.awt.BasicStroke");
        //获取所有代码中某个类库出现次数
        //System.out.println(count_lib.get("java.awt.Color"));
        //打印字典信息
        //GetLibrary.printMap(libraryMap);
        //GetLibrary.printCount(count_lib);
        //根据用户输入的库名，查找他出现次数最多的pair对
        //String libraryName = "java.awt.geom.Line2D";
        //数据库中存在的库
        //String libraryName = "java.awt.geom.Rectangle2D";

        //获取配置文件中的相关信息
        JsonPro jsonPro = new JsonPro();
        Map<String, List<String>> jsonConfig = jsonPro.getConfig(JSON_PATH);
        String num = jsonConfig.get("library").get(0);
        int n = Integer.parseInt(num);
        System.out.println(num);

        //接下来是数据库的查询
        Query query = new Query();

        boolean flag = query.querySQL(libraryName,n,exceptLibrary);


        //flag = false;
        //如果类库名找不到,启动爬虫代码，并读取爬虫的结果
        if(!flag){
            System.out.println("数据库中查询不到，需要进行爬虫！");
            Process proc;
            try {
                String s = "import "+libraryName;
                //System.out.println(s);
                String[] args1 = new String[] { "F:\\Anaconda3\\python.exe", "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\java\\com\\library\\advice\\crawl.py", s};
                proc=Runtime.getRuntime().exec(args1);
                //用输入输出流来截取结果


                //获取的python代码的输出
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();


                proc.waitFor();
                //读txt文件并进行处理
                //----------------------------------------------------------------
                String txtPath = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\java\\com\\library\\advice\\test.txt";
                String encoding="GBK";
                File file=new File(txtPath);

                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int cnt = 0;
                while((lineTxt = bufferedReader.readLine()) != null){
                    cnt++;
                    if(cnt>1){
                        int idx = lineTxt.indexOf("'", 4);
                        //System.out.println(lineTxt);只输出类库的字符
                        System.out.println(lineTxt.substring(2,idx));
                    }
                    if(cnt>n){
                        break;
                    }

                }
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /** 这里使用数据库进行查询匹配加快速度
        int maxNum = 0;
        String candidateLibrary = "";
        for(Pair<String,String> key:keys) {
            if(libraryName.equals(key.getKey()) || libraryName.equals(key.getValue())){
                int value = libraryMap.get(key);
                if(value > maxNum){
                    maxNum = value;
                    if(libraryName.equals(key.getKey())) {
                        candidateLibrary = key.getValue();
                    }else{
                        candidateLibrary = key.getKey();
                    }
                }
            }
        }
        if("java.awt.geom.Line2D".equals(candidateLibrary)){
            System.out.println("您可能需要使用这个类库："+candidateLibrary);
            System.out.println("该函数可以判断两条指定线段是否相交。");
        }
        else{
            System.out.println("您可能需要使用这个类库："+candidateLibrary);
        }
        */
    }

}
