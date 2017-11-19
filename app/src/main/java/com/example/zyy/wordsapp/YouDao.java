package com.example.zyy.wordsapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/19.
 */
public class YouDao extends Thread{


    private static final String TAG = "myTag";

    private String word;    //需要查询的单词
    private String jsonResult;  //查询结果组成的JSON字符串

    public YouDao(String word){
        this.word = word;
    }

    public String getJsonResult(){
        return jsonResult;
    }

    public void run(){

        try {

            URL url = new URL("http://fanyi.youdao.com/openapi.do");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("encoding", "utf-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            BufferedWriter bufferW = new BufferedWriter(outWriter);

            //固定格式的前缀
            String require = "keyfrom=haobaoshui&key=1650542691&type=data&doctype=json&version=1.1&q=";

            bufferW.write(require + word);
            bufferW.flush();

            InputStream in = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader bufferR = new BufferedReader(inReader);

            String line;
            StringBuilder strBuilder = new StringBuilder();
            while((line = bufferR.readLine()) != null){
                strBuilder.append(line);
            }

            jsonResult = strBuilder.toString();

            out.close();
            outWriter.close();
            bufferW.close();

            in.close();
            inReader.close();
            bufferR.close();


        } catch (Exception e) {

        }
    }

}
