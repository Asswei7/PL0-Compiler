package com.library.advice;

import com.configPro.JsonPro;
import com.database.CreateTable;
import com.database.Query;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 *
 * @author Asswei
 */
public class Application {
    public static String JSON_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\resources\\config.json";
    public static String FILE_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\java\\com\\comment\\test\\ReversePolishNotation.java";
    static class ImportNameCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(ImportDeclaration id, List<String> collector) {
            super.visit(id, collector);
            collector.add(id.getNameAsString());
        }
    }

    public static List<Pair<String,String>> advice() throws Exception {
        Map<Pair<String,String>,Integer> libraryMap = new HashMap<>();
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));
        List<String> libraryNames = new ArrayList<>();
        VoidVisitor<List<String>> libraryNameCollector = new GetLibrary.ImportNameCollector();
        //获取文件中所有导入的类库名，以列表形式存入libraryNames中
        libraryNameCollector.visit(cu, libraryNames);
        System.out.println(libraryNames.size());
        //获取配置文件中的相关信息
        JsonPro jsonPro = new JsonPro();
        Map<String, List<String>> jsonConfig = jsonPro.getConfig(JSON_PATH);
        String num = jsonConfig.get("library").get(0);
        int n = Integer.parseInt(num);
        //System.out.println(num);


        //System.out.println(libraryName);
        List<String> exceptLibrary = new ArrayList<>(20);

        List<Pair<String,String>> res = new ArrayList<>();
        for(String libraryName:libraryNames){
            exceptLibrary.add(libraryName);

            //接下来是数据库的查询
            Query query = new Query();
            boolean flag = query.querySQL(libraryName,n,exceptLibrary,res);


            //flag = false;
            //如果类库名找不到,启动爬虫代码，并读取爬虫的结果
            if(!flag){
                System.out.println("数据库中查询不到类库"+libraryName+"，需要进行爬虫！");
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
                        if(lineTxt.charAt(1)=='N'){
                            continue;
                        }
                        cnt++;
                        int idx = lineTxt.indexOf("'", 4);
                        //System.out.println(lineTxt);只输出类库的字符
                        Pair<String,String > tmp = new Pair<>(lineTxt.substring(2,idx),"爬虫得到的");

                        res.add(tmp);
                        //System.out.println(idx);
                        //System.out.println(lineTxt.substring(2,idx));

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
        }
        //打印得到的类库信息
        //for(String ans:res){
         //   System.out.println(ans);
        //}
        //System.out.println(res.get(0).getValue());
        return res;

    }



}
