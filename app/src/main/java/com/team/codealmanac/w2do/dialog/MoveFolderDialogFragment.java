package com.team.codealmanac.w2do.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Jaeung on 2017-06-09.
 */

public class MoveFolderDialogFragment extends DialogFragment implements View.OnClickListener{
    private final String TAG = MoveFolderDialogFragment.class.getSimpleName();
    private FolderAdapter folderAdapter;
    private SQLiteManager mSQLiteManager;
    private MoveFolderDialongListener moveFolderDialongListener;

    public interface MoveFolderDialongListener{
        void OnMoveButtonClickListener(String newFolder);
    }

    public MoveFolderDialogFragment() {
    }

    public static MoveFolderDialogFragment newInstance() {
        return new MoveFolderDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            moveFolderDialongListener = (MoveFolderDialongListener)context;
        } catch (Exception e){
            Log.d(TAG, "error : " + e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialogfragment_movefolder, container, false);
        FontContract font = new FontContract(getDialog().getContext().getAssets());
        mSQLiteManager = SQLiteManager.getInstance(getDialog().getContext());

        TextView dialogfragment_movefolder_title = (TextView)v.findViewById(R.id.dialogfragment_movefolder_title);
        RecyclerView dialogfragment_movefolder_folderlist = (RecyclerView)v.findViewById(R.id.dialogfragment_movefolder_folderlist);
        Button dialogfragment_movefolder_cancel_btn = (Button)v.findViewById(R.id.dialogfragment_movefolder_cancel_btn);
        Button dialogfragment_movefolder_move_btn = (Button)v.findViewById(R.id.dialogfragment_movefolder_move_btn);

        dialogfragment_movefolder_folderlist.setHasFixedSize(true);
        dialogfragment_movefolder_folderlist.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        folderAdapter = new FolderAdapter(mSQLiteManager.getAllTodoFolderOnlyName());
        dialogfragment_movefolder_folderlist.setAdapter(folderAdapter);

        dialogfragment_movefolder_title.setTypeface(font.NahumSquareB_Regular());
        dialogfragment_movefolder_cancel_btn.setTypeface(font.NahumSquareR_Regular());
        dialogfragment_movefolder_move_btn.setTypeface(font.NahumSquareR_Regular());

        dialogfragment_movefolder_cancel_btn.setOnClickListener(this);
        dialogfragment_movefolder_move_btn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialogfragment_movefolder_move_btn){
            if(!Objects.equals(folderAdapter.selectedFolder, "")){
                moveFolderDialongListener.OnMoveButtonClickListener(folderAdapter.selectedFolder);
            } else {
                Toast.makeText(getDialog().getContext(), "이동할 폴더를 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        this.dismiss();
    }

    private class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{
        ArrayList<String> mItems;
        String selectedFolder = "";
        ArrayList<FolderAdapter.ViewHolder> mHolder;
        FolderAdapter(ArrayList<String> mItems) {
            this.mItems = mItems;
            mHolder = new ArrayList<>();
        }

        @Override
        public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getDialog().getContext()).inflate(R.layout.adpitem_movefolder, parent, false));
        }

        @Override
        public void onBindViewHolder(final FolderAdapter.ViewHolder holder, int position) {
            String folder = mItems.get(position);
            holder.adp_movefolder_text.setText(folder);
            if(selectedFolder.equals(folder)){
                holder.itemView.setBackgroundResource(R.color.select_list_bg);
            } else {
                holder.itemView.setBackgroundResource(R.color.real_white);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFolder = holder.adp_movefolder_text.getText().toString();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mItems == null){
                return 0;
            }
            return mItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView adp_movefolder_text;
            ViewHolder(View itemView) {
                super(itemView);
                adp_movefolder_text = (TextView)itemView.findViewById(R.id.adp_movefolder_text);
                adp_movefolder_text.setTypeface((new FontContract(itemView.getContext().getAssets()).NahumSquareR_Regular()));
            }
        }
    }
}
