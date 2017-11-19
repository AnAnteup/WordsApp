package com.example.zyy.wordsapp.word;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
public class YouDaoWord implements Serializable{

    private String query;

    private String translation;

    private String[] phonetics;

    private List<String> explains;

    private List<Map<String, String>> webs;

    public YouDaoWord(){}

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String[] getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(String[] phonetics) {
        this.phonetics = phonetics;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public List<Map<String, String>> getWebs() {
        return webs;
    }

    public void setWebs(List<Map<String, String>> webs) {
        this.webs = webs;
    }
}
