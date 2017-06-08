package com.team.codealmanac.w2do.contract;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by Choi Jaeung on 2017-01-18.
 */

public class FontContract {
    private AssetManager mAssets;

    public FontContract(AssetManager mAssets) {
        this.mAssets = mAssets;
    }
    public Typeface FranklinGothic_Demi(){
        return Typeface.createFromAsset(mAssets, "FranklinGothicDemi.TTF");
    }
    public Typeface FranklinGothic_Medium(){
        return Typeface.createFromAsset(mAssets, "FranklinGothicMedium.TTF");
    }
    public Typeface FranklinGothic_MediumCond(){
        return Typeface.createFromAsset(mAssets, "FranklinGothicMediumCond.TTF");
    }
    public Typeface NahumSquareB_Regular(){
        return Typeface.createFromAsset(mAssets, "NanumSquareB.ttf");
    }
    public Typeface NahumSquareR_Regular(){
        return Typeface.createFromAsset(mAssets, "NanumSquareR.ttf");
    }
    public Typeface YiSunShinDotumB_Regular(){
        return Typeface.createFromAsset(mAssets, "YiSunShinDotumB-Regular.ttf");
    }
    public Typeface YiSunShinDotumL_Regular(){
        return Typeface.createFromAsset(mAssets, "YiSunShinDotumL-Regular.ttf");
    }
    public Typeface YiSunShinDotumM_Regular(){
        return Typeface.createFromAsset(mAssets, "YiSunShinDotumM-Regular.ttf");
    }
    public Typeface RobotoLight(){
        return Typeface.createFromAsset(mAssets,"Roboto-Light.ttf");
    }
    public Typeface RobotoThin(){
        return Typeface.createFromAsset(mAssets,"Roboto-Thin.ttf");
    }
    public Typeface RobotoMedium(){
        return Typeface.createFromAsset(mAssets,"Roboto-Medium.ttf");
    }

}
