package com.gf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: <br>
 * Packet:com.gf<br>
 * Description: <br>
 * Author:GuoFu<br>
 * Create Date: 2018-2-6.<br>
 * Modify User: <br>
 * Modify Date: <br>
 * Modify Description: <br>
 */
public class Test {
    public static void main(String[] args) {
        //String str = "guof-郭富,libl-李丙龙,sudq-苏大泉,chenhc-陈洪昌,zhanggc-张广超,wujj-吴纪军";
        //String[] userArr=str.split(",");
        //Map<String,String> userMap=new HashMap<>();
        //for (String item : userArr){
        //    String[] tmp=item.split("-");
        //    userMap.put(tmp[0],tmp[1]);
        //    System.out.println(item);
        //}
        //System.out.println();


        String str = "/PrivateDoc-ef9b7cc968d5929faaef4d5e51aa28b8b1938aa3400bd5bf2f7478c0b1f4882d,/CommonDoc-ef9b7cc968d5929faaef4d5e51aa28b8b1938aa3400bd5bf2f7478c0b1f4882d|05b860739c6ac5984bcafa1faede929daf5df3de6282854017a65e7d75917706";
        String[] pushArr = str.split(",");
        Map<String,List<String>> pushMap=new HashMap<>();
        for (String item : pushArr) {
            List<String> tokenList=new ArrayList<>();
            String[] dirAndTokenArr = item.split("-");

            System.out.println("目录："+dirAndTokenArr[0]);
            String[] tokensArr = dirAndTokenArr[1].split("\\|");
            for (String token : tokensArr) {
                tokenList.add(token);
                System.out.println("\t组ID：" + token);
            }
            pushMap.put(dirAndTokenArr[0],tokenList);
        }

        System.out.println();
    }

}
