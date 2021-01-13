package com.library.advice;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.util.Pair;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Asswei
 */
public class GetLibrary {
    public String filePath;
    public static void printMap(Map<Pair<String, String>, Integer> libraryMap){
        Set<Pair<String,String>> keys=libraryMap.keySet();
        for(Pair<String,String> key:keys) {
            // 获取键的值
            Integer value = libraryMap.get(key);
            System.out.println(key.getKey()+" "+key.getValue()+" "+value);
        }
    }
    public static void printCount(Map<String, Integer> count_lib){
        Set<String> keys=count_lib.keySet();
        for(String key:keys) {
            // 获取键的值
            Integer value = count_lib.get(key);
            System.out.println(key+" "+" "+value);
        }
    }
    public static Map<Pair<String,String>,Integer> getMap(String filePath,Map<Pair<String,String>,Integer> libraryMap) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(filePath));
        List<String> libraryNames = new ArrayList<>();
        VoidVisitor<List<String>> libraryNameCollector = new ImportNameCollector();
        //获取文件中所有导入的类库名，以列表形式存入libraryNames中
        libraryNameCollector.visit(cu, libraryNames);
        //将两两类库匹配作为键，一起出现次数作为值，存入字典libraryMap中
        buildMap(libraryNames, libraryMap);
        return libraryMap;
    }
    public static Map<String, Integer> countLib(String filePath,Map<String,Integer>  count_lib) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(filePath));
        List<String> libraryNames = new ArrayList<>();
        VoidVisitor<List<String>> libraryNameCollector = new ImportNameCollector();
        //获取文件中所有导入的类库名，以列表形式存入libraryNames中
        libraryNameCollector.visit(cu, libraryNames);
        for(String libName : libraryNames){
            if(count_lib.containsKey(libName)){
                count_lib.put(libName,count_lib.get(libName)+1);
            }else{
                count_lib.put(libName,1);
            }

        }
        return count_lib;
    }
    static class ImportNameCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(ImportDeclaration id, List<String> collector) {
            super.visit(id, collector);
            collector.add(id.getNameAsString());
        }
    }
    public static void buildMap(List<String> libraryNames, Map<Pair<String, String>, Integer> map){
        int n = libraryNames.size();
        //对字符串列表排序，避免键值对的重复计算
        Collections.sort(libraryNames);
        for(int i=0;i<n;i++){
            String s1 = libraryNames.get(i);
            for(int j=i+1;j<n;j++){
                String s2 = libraryNames.get(j);
                Pair<String,String> tempPair = new Pair<>(s1,s2);
                if(map.containsKey(tempPair)){
                    map.put(tempPair,map.get(tempPair)+1);
                }else{
                    map.put(tempPair,1);
                }
            }
        }
    }

}
