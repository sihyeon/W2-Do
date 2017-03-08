package com.team.codealmanac.w2do.viewholder;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.team.codealmanac.w2do.FontContract;
import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class TodoFolderViewHolder extends RecyclerView.ViewHolder {
    public TextView folder_name;
    public TextView todo_count;
    public TodoFolderViewHolder(final View itemView) {
        super(itemView);
        FontContract fontContract = new FontContract(itemView.getContext().getAssets());
        folder_name = (TextView) itemView.findViewById(R.id.adp_todofolder_name);
        todo_count = (TextView) itemView.findViewById(R.id.adp_todofolder_count);
        folder_name.setTypeface(fontContract.NahumSquareR_Regular());
        todo_count.setTypeface(fontContract.NahumSquareR_Regular());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), InFolderActivity.class);
                intent.putExtra("folderName", folder_name.getText());
                itemView.getContext().startActivity(intent);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("folder", "gg");
                return true;
            }
        });
    }
}

