package com.team.codealmanac.w2do.bean;

/**
 * Created by sihyeon on 2016-12-04.
 */

import android.graphics.drawable.Drawable;

public class ListViewItem {
    // 아이템 타입을 구분하기 위한 type 변수.
    private int type ;

    private String titleStr ;
    private String descStr ;

    private Drawable iconDrawable ;
    private Drawable quesDrawable;
    private String nameStr ;


    public void setType(int type) {
        this.type = type ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }

    public void setquesIcon(Drawable icon1) {
        quesDrawable = icon1 ;
    }

    public void setName(String name) {
        nameStr = name ;
    }

    public int getType() {
        return this.type ;
    }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public Drawable getquesIcon() {
        return this.quesDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public String getName() {
        return this.nameStr ;
    }
}