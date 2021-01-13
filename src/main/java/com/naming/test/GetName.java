package com.naming.test;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Map;

/**
 * @author Asswei
 */
public class GetName {
    public static class VariableInfoMap extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(VariableDeclarator vd, Map<String, Integer> collector) {
            super.visit(vd, collector);
            String name = vd.getNameAsString();
            Integer lineNum =  vd.getRange().get().begin.line;
            collector.put(name, lineNum);
        }
    }
    public static class PackageInfoMap extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(PackageDeclaration pd, Map<String, Integer> collector) {
            super.visit(pd, collector);
            String name = pd.getNameAsString();
            Integer lineNum =  pd.getRange().get().begin.line;
            collector.put(name, lineNum);
        }
    }
    public static class MethodInfoMap extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(MethodDeclaration md, Map<String, Integer> collector) {
            super.visit(md, collector);
            String name = md.getNameAsString();
            Integer lineNum =  md.getRange().get().begin.line;
            collector.put(name, lineNum);
        }
    }
    public static class ClassInfoMap extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cd, Map<String, Integer> collector) {
            super.visit(cd, collector);
            String name = cd.getNameAsString();
            Integer lineNum =  cd.getRange().get().begin.line;
            collector.put(name, lineNum);
        }
    }


}
