package com.example.zyy.wordsapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zyy.wordsapp.word.Words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyy on 2016/9/17.
 */
public class WordsDB {

    private static final String TAG = "myTag";

    private static WordsDBHelper mDbHelper;

    //采用单例模式
    private static WordsDB instance=new WordsDB();
    public static WordsDB getWordsDB(){
        return WordsDB.instance;
    }

    private WordsDB() {
        if (mDbHelper == null) {
            mDbHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }

    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }

    //获得单个单词的全部信息
    public Words.WordDescription getSingleWord(String id) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where _ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            Words.WordDescription item = new Words.WordDescription(cursor.getString(cursor.getColumnIndex(Words.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            return item;
        }
        return null;

    }

    //得到全部单词列表
    public ArrayList<Map<String, String>> getAllWords() {

        if (mDbHelper == null) {
            return null;
        }

        //查询操作要获取到可读取的数据库
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING
        };

        //倒序排列，即新加入的单词放在前面
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";


        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // 表名
                projection, // 列名
                null,   // 查询条件，相当于where后面的语句，允许存在占位符
                null,   // 对应查询条件中的占位符的值
                null,   // 相当于group by
                null,   // 相当于having
                sortOrder
        );

        return cursorToWordList(c);
    }



    //将游标转化为单词列表，转换好的列表是ListView可以直接使用的格式
    private ArrayList<Map<String, String>> cursorToWordList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getString(cursor.getColumnIndex(Words.Word._ID))));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            map.put(Words.Word.COLUMN_NAME_MEANING, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)));
            result.add(map);
        }

        return result;
    }

    //使用Sql语句插入单词
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {

        String sql = "insert into  words(_id,word,meaning,sample) values(?,?,?,?)";

        //插入操作时要获取到可写入的数据库
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(sql, new String[]{GUID.getGUID(),strWord, strMeaning, strSample});
    }

    //使用insert方法增加单词
    public void Insert(String strWord, String strMeaning, String strSample) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //ContentValues类似于HashMap，但是只能存储基本数据类型
        ContentValues values = new ContentValues();
        values.put(Words.Word._ID, GUID.getGUID());
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        // 插入成功，返回ID
        // 插入失败返回-1
        long newRowId = db.insert(
                Words.Word.TABLE_NAME,  //表名
                null,   // 空列的默认值
                values);    // ContentValues封装好的键值对
    }


    //使用Sql语句删除单词
    public void DeleteUseSql(String strId) {

        String sql = "delete from words where _id='" + strId + "'";

        //删除操作也需要可读的数据库
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        db.execSQL(sql);
    }

    //删除单词
    public void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = Words.Word._ID + " = ?";

        String[] selectionArgs = {strId};

        db.delete(Words.Word.TABLE_NAME,
                selection,  // 相当于where语句
                selectionArgs); //selection中的占位符的具体值
    }


    //使用Sql语句更新单词
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "update words set word=?,meaning=?,sample=? where _id=?";

        db.execSQL(sql, new String[]{strWord,strMeaning, strSample, strId});
    }

    //使用方法更新
    public void Update(String strId, String strWord, String strMeaning, String strSample) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};

        int count = db.update(
                Words.Word.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public ArrayList<Map<String, String>> SearchOne(String strWordSearch){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from words where word = ? order by word desc";

        Cursor c = db.rawQuery(sql, new String[]{strWordSearch});

        return cursorToWordList(c);
    }


    //使用Sql语句查找
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where word like ? order by word desc";

        //用两个百分号实现模糊查询
        Cursor c = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});

        return cursorToWordList(c);
    }

    //使用query方法查找
    public ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        String sortOrder =
                Words.Word._ID + " DESC";

        String selection = Words.Word._ID + " LIKE ?";
        String[] selectionArgs = {"%" + strWordSearch + "%"};

        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // The table to query
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return cursorToWordList(c);
    }

}
