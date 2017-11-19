package com.example.zyy.wordsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyy.wordsapp.word.Words;
import com.example.zyy.wordsapp.word.YouDaoWord;

import java.util.ArrayList;
import java.util.Map;

public class YoudaoActivity extends AppCompatActivity {


    private static final String TAG = "myTag";

    private TextView tvQuery,tvExplains;
    private TextView[] tvPhonetics = new TextView[3];
    private ListView listView;
    private YouDaoWord ydWord = null;
    private Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youdao);

        tvQuery = (TextView) findViewById(R.id.tv_query);
        tvPhonetics[0] = (TextView) findViewById(R.id.tv_USPhonetic);
        tvPhonetics[1] = (TextView) findViewById(R.id.tv_Phonetic);
        tvPhonetics[2] = (TextView) findViewById(R.id.tv_UKPhonetic);
        tvExplains = (TextView) findViewById(R.id.tv_explains);
        listView = (ListView) findViewById(R.id.listView_web);

        // 获取传进来的Word的值
        Intent intent = getIntent();
        String word = intent.getStringExtra("ydWord");
        Log.i(TAG, "onCreate: " + word);

        // 创建调用有道API的线程，启动线程
        YouDao yd = new YouDao(word);
        yd.start();
        try {
            // 等待子线程执行完毕
            yd.join();

            // 获取子线程中得到的JSON数据
            String jsonString = yd.getJsonResult();

            // 使用工厂方法解析返回的JSON数据并创建有道的单词对象
            ydWord = YouDaoWordFactory.newInstance(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将创建好的有道单词对象的数据放到Activity中显示
        tvQuery.setText(ydWord.getQuery());
        for(int i=0; i<tvPhonetics.length; i++){
            tvPhonetics[i].setText(ydWord.getPhonetics()[i]);
        }
        String explains = "";
        int num = ydWord.getExplains().size();
        for(int i=0; i<num; i++){
            explains = explains + ydWord.getExplains().get(i);
            if(i != num-1){
                explains = explains + "\n";
            }
        }
        tvExplains.setText(explains);

        SimpleAdapter adapter = new SimpleAdapter(this, ydWord.getWebs(), R.layout.youdaoweb_item
                ,new String[]{"webKey", "webValue"}
                ,new int[]{R.id.tv_webKey, R.id.tv_webValue});
        listView.setAdapter(adapter);

        try{
            btn_update = (Button)findViewById(R.id.btn_update);
            if( btn_update != null) {

                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WordsDB wordsDB = WordsDB.getWordsDB();
                        if (wordsDB != null) {
                            ArrayList<Map<String, String>> items = wordsDB.SearchOne(ydWord.getQuery().toLowerCase());
                            if (items.size() == 0) {
                                wordsDB.Insert(ydWord.getQuery(), ydWord.getTranslation().substring(2, ydWord.getTranslation().length()-2), ydWord.getExplains().toString());
                                Toast.makeText(YoudaoActivity.this, "添加单词成功", Toast.LENGTH_SHORT).show();
                            } else {
                                String id = items.get(0).get(Words.Word._ID);
                                wordsDB.Update(id, ydWord.getQuery().toLowerCase(), ydWord.getTranslation().substring(2, ydWord.getTranslation().length()-2)
                                        , ydWord.getExplains().toString());
                                Toast.makeText(YoudaoActivity.this, "更新单词成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }catch (Exception e){

        }
    }
}
