package com.comment.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.naming.test.GetName;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Asswei
 */
public class GetComment {
    //private static final String FILE_PATH = "/src/main/java/com/comment/test/ReversePolishNotation.java";
    private static final String FILE_PATH ="F:/java/IDEA/WorkSpace/helpForProgram/src/main/java/com/comment/test/ReversePolishNotation.java";
    //获取单行注释下一行代码的开始的列号
    public static int getCodeColNum(int lineNum) throws IOException {
        File file = new File(FILE_PATH);
        FileReader fileReader = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(fileReader);
        String txt = "";
        int lines = 0;
        while (txt != null) {
            lines++;
            txt = reader.readLine();
            if (lines == lineNum) {
                //System.out.println("第" + reader.getLineNumber() + "的内容是：" + txt + "\n");
                break;
            }
        }
        reader.close();
        fileReader.close();
        //用于确定第一个不是空格的字符所在的位置
        int beginIndex = 0;
        //用于确定从后起第一个不为0的字符的位置
        int endIndex = txt.length()-1;

        while(beginIndex < endIndex && txt.charAt(beginIndex) == ' ') {
            beginIndex ++;
        }
        beginIndex++;
        return beginIndex;

    }
    //在此处输出对注释的建议，可以在这里根据配置文件设置输出
    public static void commentAdvice(List<CommentReportEntry> ls, Map<String, Integer> classInfo,List<String>config) throws IOException {
        //config 行尾、孤立、方法前、类前
        List<Integer> flag = new ArrayList<>();
        Map<Integer, Boolean> lineMap = new HashMap<>();
        for(CommentReportEntry comment: ls){
            if("LineComment".equals(comment.type) && !comment.isOrphan){
                int codeColNum = getCodeColNum(comment.lineNumber+1);
                //如果配置文件为none，不处理也不打印
                if(comment.colNumber != codeColNum && !config.get(0).equals("none")){
                    //System.out.println("注释的列号"+comment.colNumber+"下一条语句的列好"+codeColNum);
                    System.out.println("提醒：第"+comment.lineNumber+"行不应有行尾注释！");
                }
                //System.out.println(comment.colNumber+"  "+comment.lineNumber);

            }
            if(comment.isOrphan && !config.get(1).equals("none")){
                System.out.println("提醒：第"+comment.lineNumber+"行不应有孤立的注释！");
            }
            if("JavadocComment".equals(comment.type)){
                lineMap.put(comment.endLineNumber+1, true);
                //System.out.println(comment.endLineNumber+1);
            }
        }
        Set<String> keys=classInfo.keySet();
        for(String key:keys) {
            // 获取键的值
            Integer value = classInfo.get(key);
            //System.out.println(value);
            if (lineMap.get(value)==null && !config.get(2).equals("none")) {
                System.out.println("提醒：第" + value + "行" + "类名" + key + "前没有javadoc形式注释。");
            }
        }
    }

    //原来的主函数，现在将其封装为一个类中的方法
    //public static void main(String[] args) throws Exception {
    public void printAdvice(String FILE_PATH,List<String> config) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        Map<String, Integer> classInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> classInfoMap = new GetName.ClassInfoMap();
        classInfoMap.visit(cu, classInfo);

        List<CommentReportEntry> comments = cu.getAllContainedComments()
                .stream()
                .map(p -> new CommentReportEntry(p.getClass().getSimpleName(),
                        p.getContent(),
                        p.getRange().get().begin.line,
                        p.getRange().get().begin.column,
                        p.getRange().get().end.line,
                        !p.getCommentedNode().isPresent()))
                .collect(Collectors.toList());

        //comments.forEach(System.out::println);
        //验证孤立注释，行尾注释，类前的javadoc注释
        commentAdvice(comments, classInfo,config);
    }

    public void printAdviceStr(String FILE_PATH,List<String> config) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        Map<String, Integer> classInfo = new HashMap<>() ;
        VoidVisitor<Map<String, Integer>> classInfoMap = new GetName.ClassInfoMap();
        classInfoMap.visit(cu, classInfo);

        List<CommentReportEntry> comments = cu.getAllContainedComments()
                .stream()
                .map(p -> new CommentReportEntry(p.getClass().getSimpleName(),
                        p.getContent(),
                        p.getRange().get().begin.line,
                        p.getRange().get().begin.column,
                        p.getRange().get().end.line,
                        !p.getCommentedNode().isPresent()))
                .collect(Collectors.toList());

        //comments.forEach(System.out::println);
        //验证孤立注释，行尾注释，类前的javadoc注释
        commentAdvice(comments, classInfo,config);
    }

    private static class CommentReportEntry {
        private final String type;
        private final String text;
        private final int lineNumber;
        private final int colNumber;
        private final int endLineNumber;
        private final boolean isOrphan;

        CommentReportEntry(String type, String text, int lineNumber, int colNumber, int endLineNumber, boolean isOrphan) {
            this.type = type;
            this.text = text;
            this.lineNumber = lineNumber;
            this.colNumber = colNumber;
            this.endLineNumber = endLineNumber;
            this.isOrphan = isOrphan;
        }

        @Override
        public String toString() {
            return lineNumber +"&"+colNumber + "|" + type + "|" + isOrphan + "|" + text.replaceAll("\\n","").trim();
        }
        
    }
    class ClassInfoMap extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cd, Map<String, Integer> collector) {
            super.visit(cd, collector);
            String name = cd.getNameAsString();
            Integer lineNum =  cd.getRange().get().begin.line;
            collector.put(name, lineNum);
        }
    }
}
