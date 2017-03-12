package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team.codealmanac.w2do.contract.FontContract;

/**
 * Created by Choi Jaeung on 2017-02-05.
 */

public class FolderSpinnerAdapter extends ArrayAdapter<String> {
    private FontContract mFontContract;
    public FolderSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mFontContract = new FontContract(context.getAssets());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView)super.getView(position, convertView, parent);
        v.setTypeface(mFontContract.NahumSquareR_Regular());
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView)super.getDropDownView(position, convertView, parent);
        v.setTypeface(mFontContract.NahumSquareR_Regular());
        return v;
    }
}
