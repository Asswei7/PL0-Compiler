package bright.web;

import bright.OPG.OPG_Analyzer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SettingController {
    @Autowired
    public SettingController() {

    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String ui(Model model){
        return "Setting";
    }

    @ResponseBody
    @RequestMapping(value = "/setting/save", method = RequestMethod.POST)
    public Object analyze(@RequestBody JSONObject jsonParam) throws IOException {
        File file = new File("config_new.json");
        FileWriter writer = new FileWriter(file);
        writer.write(jsonParam.toString());
        writer.close();

//        String []grammarString = jsonParam.getString("grammar").replaceAll("\r", "").split("\n");
//        List<String> grammar = new ArrayList<>(Arrays.asList(grammarString));
//        String sentence = jsonParam.getString("sentence");
//        OPG_Analyzer.AnalyzeResult result;
//
//        OPG_Analyzer analyzer = new OPG_Analyzer(grammar);
//        analyzer.analyze(sentence);
//        result = analyzer.result;
//        System.out.println(result.isError());
        //System.out.println(JSON.toJSONString(result));

        return JSON.toJSONString(jsonParam);
    }
}
