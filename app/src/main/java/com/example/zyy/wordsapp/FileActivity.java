package com.example.zyy.wordsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = "myTag";

    private static TextView tv_inputFile;
    private Button btn_inputButton;
    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        tv_inputFile = (TextView)findViewById(R.id.tv_inputFile);
        btn_inputButton = (Button)findViewById(R.id.btn_inputFile);
        btn_inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


    }

    public String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        Log.i(TAG, "ReadTxtFile: " + file);
        //如果path是传递过来的参数，可以做一个非目录的判断
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null)
            {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                instream.close();
                tv_inputFile.setText(content, TextView.BufferType.SPANNABLE);

                getEachWord(tv_inputFile);

                tv_inputFile.setMovementMethod(LinkMovementMethod.getInstance());

            }
        }
        catch (Exception e)
        {
            Log.i(TAG, "The File doesn't not exist.");
        }
        return content;
    }

    /** 调用文件选择软件来选择文件 **/
    private void showFileChooser() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要读取的文件"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(FileActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /** 根据返回选择的文件，来进行上传操作 **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            Log.i(TAG, "onActivityResult: " + uri);
            if(uri.toString().contains(".txt")){
                url = uri.toString().substring(7);
                Log.i(TAG, "onActivityResult: " + url);
                ReadTxtFile(url);
            }else {
                Toast.makeText(FileActivity.this, "请打开一个txt文件", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getEachWord(TextView textView){
        Spannable spans = (Spannable)textView.getText();
        Log.i(TAG, "getEachWord: " + spans);
        Integer[] indices = getIndices( textView.getText().toString().trim(), ' ');
        int start = 0;
        int end = 0;
        for (int i = 0; i <= indices.length; i++) {
            ClickableSpan clickSpan = getClickableSpan();
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
    }
    private ClickableSpan getClickableSpan(){
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();
                s = s.replaceAll("[.,！]","");
                Log.i(TAG ,"tapped on:" + s);
                Intent intent = new Intent(FileActivity.this, YoudaoActivity.class);
                intent.putExtra("ydWord", s);
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    public Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return indices.toArray(new Integer[0]);
    }

}
