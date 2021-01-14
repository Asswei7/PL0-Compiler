package com.configPro;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Asswei
 */
public class JsonPro {
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, List<String>> getConfig(String path){
    //public static void main(String[] args) {
        Map<String, List<String>> jsonConfig = new HashMap<>();
        //String path = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\resources\\config.json";
        //String path = JsonPro.class.getClassLoader().getResource("config.json").getPath();
        String s = readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);

        //读取爬虫模块的配置文件
        List<String> crawlConfig = new ArrayList<>();
        JSONObject crawlModule =  jobj.getJSONObject("crawlModule");
        String inName = (String) crawlModule.get("inName");
        String inDescription = (String) crawlModule.get("inDescription");
        String inReadme = (String) crawlModule.get("inReadme");
        String stars = (String) crawlModule.get("stars");
        String size = (String) crawlModule.get("size");
        String pushed = (String) crawlModule.get("pushed");
        String isCrawler;
        if(crawlModule.get("isCrawler")==null){
            isCrawler = "off";
        }else{
            isCrawler = (String) crawlModule.get("isCrawler") ;
        }

        crawlConfig.add(inName);
        crawlConfig.add(inReadme);
        crawlConfig.add(inDescription);
        crawlConfig.add(stars);
        crawlConfig.add(size);
        crawlConfig.add(pushed);
        crawlConfig.add(isCrawler);
        jsonConfig.put("crawl",crawlConfig);

        //读取注释模块的配置文件
        List<String> commentConfig = new ArrayList<>();
        JSONObject commentModule =  jobj.getJSONObject("commentModule");
        String lineTailComment = (String) commentModule.get("lineTailComment");
        String orphanComment = (String) commentModule.get("orphanComment");
        String beforeClassComment = (String) commentModule.get("beforeClassComment");
        String beforeMethodComment = (String) commentModule.get("beforeMethodComment");
        commentConfig.add(lineTailComment);
        commentConfig.add(orphanComment);
        commentConfig.add(beforeClassComment);
        commentConfig.add(beforeMethodComment);
        jsonConfig.put("comment",commentConfig);

        //读取符号命名模块配置文件
        List<String> symbolConfig = new ArrayList<>();
        JSONObject symbolModule =  jobj.getJSONObject("symbolModule");
        String packageName = (String) symbolModule.get("packageName");
        String constName = (String) symbolModule.get("constName");
        String className = (String) symbolModule.get("className");
        String variableName = (String) symbolModule.get("variableName");
        symbolConfig.add(packageName);
        symbolConfig.add(constName);
        symbolConfig.add(className);
        symbolConfig.add(variableName);
        jsonConfig.put("symbol",symbolConfig);

        //读取类库推荐模块配置文件
        List<String> libraryConfig = new ArrayList<>();
        JSONObject libraryModule =  jobj.getJSONObject("libraryModule");
        String num4display = (String) libraryModule.get("num4display");
        String displayMethod = (String) libraryModule.get("displayMethod");
        libraryConfig.add(num4display);
        libraryConfig.add(displayMethod);
        jsonConfig.put("library",libraryConfig);

        return jsonConfig;
    }
}
