package com.database;

import com.library.advice.GetLibrary;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author qcl
 * @date 2020
 * 数据库连接
 */
public class CreateTable {

    //根据类库字典建立数据库
    public static void create(Map<Pair<String, String>, Integer> libraryMap, Map<String, Integer> count_lib){
        Connection con;
        String driver="com.mysql.cj.jdbc.Driver";
        //这里我的数据库是qcl
        String url="jdbc:mysql://localhost:3306/schematest";
        String user="root";
        String password="s374857303";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
            Statement statement = con.createStatement();
            //获取map中的所有键值对，然后生成SQL语句
            Set<Pair<String,String>> keys=libraryMap.keySet();
            for(Pair<String,String> key:keys) {
                String libA = key.getKey();
                String libB = key.getValue();
                int value = libraryMap.get(key);
                int cnt1 = count_lib.get(libA);
                int cnt2 = count_lib.get(libB);
                double prob1 = (double)value/cnt1;
                double prob2 = (double) value/cnt2;
                System.out.println(prob1);
                //String sql = "INSERT INTO `schematest`.`draw_lib` (`pairFirst`, `pairSecond`, `value`) VALUES (libA, libB, value);";
                String sql = "INSERT INTO `schematest`.`draw_lib` (`pairFirst`, `pairSecond`, `value`,`prob1`,`prob2`,`description1`,`description2`) VALUES (";
                sql += "'"+ libA +"'"+","+"'"+libB+"'"+",'"+value+"',"+String.valueOf(prob1)+","+String.valueOf(prob2)+",'the func is..','the func is..'"+");";
                //System.out.println(sql);

                int resultSet = statement.executeUpdate(sql);
            }


            //String sql = "select * from draw_lib;";
            //sql = "INSERT INTO `schematest`.`draw_lib` (`pairFirst`, `pairSecond`, `value`) VALUES ('java.awt.Graphics', 'java.awt.Graphics', '3');";
            //ResultSet resultSet = statement.executeQuery(sql);
            /*
            String name;
            while (resultSet.next()) {
                name = resultSet.getString("pairFirst");
                System.out.println("姓名：" + name);
            }
            resultSet.close();

             */
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("数据库连接失败");
        }
    }

    public static void main(String[] args) throws Exception {
        Map<Pair<String,String>,Integer> libraryMap = new HashMap<>();
        //统计每个类库出现的总次数
        Map<String, Integer> count_lib = new HashMap<>();
        String filePath1 = "F:\\Graduate\\ASE2020\\DrawCode\\draw3D.java";
        String filePath2 = "F:\\Graduate\\ASE2020\\DrawCode\\re6g3y-3d_geometry_template-master\\3d_geometry_template\\src\\main\\java\\girvate\\GrivateNode.java";
        String filePath3 = "F:\\Graduate\\ASE2020\\DrawCode\\re6g3y-3d_geometry_template-master\\3d_geometry_template\\src\\main\\java\\frameWork\\Drawer.java";
        String filePath4 = "F:\\Graduate\\ASE2020\\DrawCode\\re6g3y-3d_geometry_template-master\\3d_geometry_template\\src\\main\\java\\frameWork\\Link.java";
        String filePath5 = "F:\\Graduate\\ASE2020\\DrawCode\\re6g3y-3d_geometry_template-master\\3d_geometry_template\\src\\main\\java\\frameWork\\Node.java";
        String filePath6 = "F:\\Graduate\\ASE2020\\DrawCode\\re6g3y-3d_geometry_template-master\\3d_geometry_template\\src\\main\\java\\frameWork\\Paint.java";

        //根据一个文件名列表，生成一个MAP
        List<String> fileList = new ArrayList<>();
        fileList.add(filePath1);
        fileList.add(filePath2);
        fileList.add(filePath3);
        fileList.add(filePath4);
        fileList.add(filePath5);
        fileList.add(filePath6);
        for(String s:fileList){
            GetLibrary.getMap(s, libraryMap);
            count_lib = GetLibrary.countLib(s,count_lib);
        }
        //System.out.println(count_lib);
        create(libraryMap,count_lib);
    }
}

