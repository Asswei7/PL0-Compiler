package bright.web;

import com.StyleCheck;
import bright.Compiler.Error;
import bright.Compiler.Interpreter;
import bright.Compiler.Pcode;
import bright.Compiler.RD_Analyzer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Result{
    private List<Pcode> pcodeList;
    private List<Error> errorList;
    private List<String> outputList;

    public Result(List<Pcode> pcodeList, List<Error> errorList) {
        this.pcodeList = pcodeList;
        this.errorList = errorList;
    }
    public Result(List<Pcode> pcodeList, List<Error> errorList, List<String> outputList) {
        this.pcodeList = pcodeList;
        this.errorList = errorList;
        this.outputList = outputList;
    }

    public List<Pcode> getPcodeList() {
        return pcodeList;
    }
    public List<Error> getErrorList() {
        return errorList;
    }
    public List<String> getOutputList() {
        return outputList;
    }
}



class Result_style{
    private String output;
    private List<PackageInfo> packageList;

    public Result_style(String output) {
        this.output = output;
    }
    public Result_style(String output, List<PackageInfo> packageList) {
        this.output = output;
        this.packageList = packageList;
    }

    public String getOutput() {
        return output;
    }
    public List<PackageInfo> getPackageList() {
        return packageList;
    }
}

@Controller
public class CompilerController {

    @RequestMapping(value = "/compiler", method = RequestMethod.GET)
    public String ui(){
        return "Compiler";
    }

    // 编译
    @ResponseBody
    @RequestMapping(value = "/compiler/compile", method = RequestMethod.POST)
    public String compile(@RequestBody JSONObject jsonParam) throws IOException {
        //System.out.println(jsonParam);


        String code = jsonParam.getString("code").replaceAll("\r", "");
        Random random = new Random();
        File file = new File("upload-dir/code" + random.nextInt(10000) + ".txt");
        FileWriter writer = new FileWriter(file);
        writer.write(code);
        writer.close();
        com.StyleCheck.FILE_PATH = file.getPath();
        com.StyleCheck.JSON_PATH = "/Users/jiangzeren/Documents/GitHub/PL0-Compiler/src/main/resources/config.json";

        System.setOut(new PrintStream(new File("/Users/jiangzeren/Documents/GitHub/PL0-Compiler/style.txt")));
        com.StyleCheck.check();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        RD_Analyzer compiler = new RD_Analyzer(file);
        try {
            compiler.compile();
        }catch (Exception e){
            List<String> error = new ArrayList<>();
            error.add("Compile Error!");
            Result compileResult = new Result(compiler.getPcodes(), compiler.getErrors(), error);
            return JSON.toJSONString(compileResult);
        }

        Result compileResult = new Result(
                compiler.getPcodes(), compiler.getErrors());
        return JSON.toJSONString(compileResult);
    }

    // 编译并执行
//    @ResponseBody
//    @RequestMapping(value = "/compiler/interpret", method = RequestMethod.POST)
//    public String interpret(@RequestBody JSONObject jsonParam) throws IOException {
//        String code = jsonParam.getString("code").replaceAll("\r", "");
//        List<Integer> inputList = getInput(jsonParam.getString("input"));
//
//        Random random = new Random();
//        File file = new File("upload-dir/code" + random.nextInt(10000) + ".txt");
//        FileWriter writer = new FileWriter(file);
//        writer.write(code);
//        writer.close();
//
//        RD_Analyzer compiler = new RD_Analyzer(file);
//        compiler.compile();
//
//        Interpreter interpreter = new Interpreter(compiler.getPcodes(), inputList);
//        try {
//            interpreter.interpret();
//        }catch (Exception e){
//            List<String> error = new ArrayList<>();
//            error.add("Runtime Error!");
//            Result interpretResult = new Result(compiler.getPcodes(), compiler.getErrors(), error);
//            return JSON.toJSONString(interpretResult);
//        }
//        Result interpretResult = new Result(compiler.getPcodes(), compiler.getErrors(), interpreter.getOutput());
//        return JSON.toJSONString(interpretResult);
//    }
    @ResponseBody
    @RequestMapping(value = "/compiler/interpret", method = RequestMethod.POST)
    public String interpret(@RequestBody JSONObject jsonParam) throws IOException {
        String code = jsonParam.getString("code").replaceAll("\r", "");
        List<Integer> inputList = getInput(jsonParam.getString("input"));

        Random random = new Random();
        File file = new File("upload-dir/code" + random.nextInt(10000) + ".txt");
        FileWriter writer = new FileWriter(file);
        writer.write(code);
        writer.close();

        com.StyleCheck.FILE_PATH = file.getPath();
        com.StyleCheck.JSON_PATH = "src/main/resources/config.json";

        System.setOut(new PrintStream(new File("./style.txt")));
        com.StyleCheck.check();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        File file_style = new File("./style.txt");
        FileReader reader = new FileReader(file_style);
        int aa;
        aa = reader.read();
        String content = "";
        while(aa!=(-1)){
            content += (char)aa;
            aa = reader.read();
        }


        PackageInfo packageInfo1 = new PackageInfo("java.test11","this is description","http://127.0.0.1");
        PackageInfo packageInfo2 = new PackageInfo("java.test22","this is description","http://127.0.0.1");
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(packageInfo1);
        packageInfoList.add(packageInfo2);
        Result_style result = new Result_style(content, packageInfoList);
//        RD_Analyzer compiler = new RD_Analyzer(file);
//        compiler.compile();
//
//        Interpreter interpreter = new Interpreter(compiler.getPcodes(), inputList);
//        try {
//            interpreter.interpret();
//        }catch (Exception e){
//            List<String> error = new ArrayList<>();
//            error.add("Runtime Error!");
//            Result interpretResult = new Result(compiler.getPcodes(), compiler.getErrors(), error);
//            return JSON.toJSONString(interpretResult);
//        }
//        Result interpretResult = new Result(compiler.getPcodes(), compiler.getErrors(), interpreter.getOutput());
        return JSON.toJSONString(result);
    }

    private List<Integer> getInput(String s){
        s = s.replaceAll("\r", " ");
        s = s.replaceAll("\n", " ");
        List<Integer> ret = new ArrayList<>();
        String[] nums = s.split(" ");
        for(String num : nums){
            if(num.equals("")) continue;
            try {
                ret.add(Integer.parseInt(num));
            }catch (NumberFormatException ignored){
            }
        }
        return ret;
    }
}
