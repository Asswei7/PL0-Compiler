package com.naming.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Asswei
 */
public class NamingApp {
    //public static final String FILE_PATH = "F:\\java\\IDEA\\WorkSpace\\Student\\src\\Student.java";
    public static final int PACKAGE_TYPE = 1;
    public static final int CLASS_TYPE = 2;
    public static final int VAR_TYPE = 3;

    //原来的主函数，现在封装为一个类的方法
    //public static void main(String[] args) throws FileNotFoundException {
    public void printSymbolCheck(String FILE_PATH, List<String>config) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        NamingTest namingTest = new NamingTest();
        Advice advice = new Advice();
        //有用的是methodInfo，而不是methodInfoMap
        Map<String, Integer> methodInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> methodInfoMap = new GetName.MethodInfoMap();
        methodInfoMap.visit(cu, methodInfo);

        Map<String, Integer> varInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> varInfoMap = new GetName.VariableInfoMap();
        varInfoMap.visit(cu, varInfo);

        Map<String, Integer> packageInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> packageInfoMap = new GetName.PackageInfoMap();
        packageInfoMap.visit(cu, packageInfo);

        Map<String, Integer> classInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> classInfoMap = new GetName.ClassInfoMap();
        classInfoMap.visit(cu, classInfo);

        advice.printAdvice(packageInfo,namingTest,advice,PACKAGE_TYPE,config.get(0));
        advice.printAdvice(classInfo,namingTest,advice,CLASS_TYPE,config.get(2));
        advice.printAdvice(methodInfo,namingTest,advice,VAR_TYPE,config.get(1));
        advice.printAdvice(varInfo,namingTest,advice,VAR_TYPE,config.get(3));
    }
}
