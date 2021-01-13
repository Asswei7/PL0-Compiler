package com.naming.test;

import java.util.Map;
import java.util.Set;

public class Advice {
    String ad4package(String str) {
        //大写字母改成小写
        return str.toLowerCase();
    }

    String ad4ClassName(String str) {
        //首字母大写
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    String ad4SymbolName(String str) {
        //首字母小写
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public void printAdvice(Map<String, Integer> symbolInfo, NamingTest namingTest, Advice advice, int type, String config) {

        //遍历字典,返回集合中所有键
        Set<String> keys = symbolInfo.keySet();
        for (String key : keys) {
            // 获取键的值
            Integer value = symbolInfo.get(key);
            if (type == 1 && !"none".equals(config)) {
                if (!namingTest.test4PackageName(key)) {
                    String res = advice.ad4package(key);
                    System.out.println("提示：第" + value + "行的包名：" + key + "字母应当全小写！可以改为" + res);
                }
            } else if (type == 2 && !"none".equals(config)) {
                if (!namingTest.isUpperFirst(key)) {
                    String res = advice.ad4ClassName(key);
                    System.out.println("提示：第" + value + "行的类名：" + key + "首字母应当大写！可以改为" + res);
                }
            } else if (type == 3 && !"none".equals(config)) {
                {
                    if (!namingTest.isLowerFirst(key)) {
                        String res = advice.ad4SymbolName(key);
                        System.out.println("提示：第" + value + "行的方法名或变量名：" + key + "首字母不应大写！可以改为" + res);
                    }
                }

            }
        }
    }
}
