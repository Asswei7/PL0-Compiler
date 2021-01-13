package com;

import com.comment.test.GetComment;
import com.configPro.JsonPro;
import com.naming.test.NamingApp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Asswei
 * @function integrate
 */
public class StyleCheck {
    public static String FILE_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\java\\com\\comment\\test\\ReversePolishNotation.java";
    public static String JSON_PATH = "F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\resources\\config.json";
    public static void check() throws IOException {
        JsonPro jsonPro = new JsonPro();
        Map<String, List<String>> jsonConfig = jsonPro.getConfig(JSON_PATH);


        GetComment getComment = new GetComment();
        getComment.printAdvice(FILE_PATH,jsonConfig.get("comment"));

        NamingApp namingApp = new NamingApp();
        namingApp.printSymbolCheck(FILE_PATH,jsonConfig.get("symbol"));
    }
}
