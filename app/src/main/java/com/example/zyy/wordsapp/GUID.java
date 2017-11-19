package com.example.zyy.wordsapp;

import android.util.Log;

import java.util.UUID;

/**
 * Created by zyy on 2016/9/17.
 */
public class GUID {

    private static final String TAG = "myTag";

    public static String getGUID(){
        // 创建 UUID 对象
        // 产生唯一的标识码
        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String a = uuid.toString();

        Log.i(TAG, "getGUID: " + a);

        // 生成的格式为 045a6295-9982-4fbc-9ff5-9735e4797561
        // 存储到数据库中要去掉横线
        a = a.replaceAll("-", "");
        return a;
    }

}
