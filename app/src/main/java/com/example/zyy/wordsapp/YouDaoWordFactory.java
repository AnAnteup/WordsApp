package com.example.zyy.wordsapp;

import com.example.zyy.wordsapp.word.YouDaoWord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
public class YouDaoWordFactory {

    private static final String key = "webKey";
    private static final String value = "webValue";

    //处理JSON数据，生产YouDaoWord的对象
    public static YouDaoWord newInstance(String jsonString) throws JSONException {

        YouDaoWord youDaoWord = new YouDaoWord();

        // 将JSON转换为对象
        JSONObject jsonObject = new JSONObject(jsonString);

        // 得到query属性的值
        youDaoWord.setQuery(jsonObject.getString("query"));

        youDaoWord.setTranslation(jsonObject.getString("translation"));

        // 因为web属性返回来是一个列表，所以放在List中，方便在ListView中显示
        List<Map<String, String>> listMap = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("web");
        for(int i=0; i<jsonArray.length(); i++){
            Map<String, String> map = new HashMap<>();
            map.put(key, jsonArray.getJSONObject(i).getString("key"));
            map.put(value, jsonArray.getJSONObject(i).getString("value"));
            listMap.add(map);
        }
        youDaoWord.setWebs(listMap);

        String basicString = jsonObject.getString("basic");
        jsonObject = new JSONObject(basicString);
        String[] phonetics = new String[3];
        phonetics[0] = jsonObject.getString("us-phonetic");
        phonetics[1] = jsonObject.getString("phonetic");
        phonetics[2] = jsonObject.getString("uk-phonetic");
        youDaoWord.setPhonetics(phonetics);

        // 将所有的explain的值取出
        jsonArray = jsonObject.getJSONArray("explains");
        List<String> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            list.add(jsonArray.get(i).toString());
        }
        youDaoWord.setExplains(list);

        return youDaoWord;
    }

}
