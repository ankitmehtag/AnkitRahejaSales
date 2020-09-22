package com.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.AsmHistoryModel;
import com.sp.R;
import com.utils.PicassoImageGetter;

import java.util.ArrayList;


public class AsmHistoryAdapter extends RecyclerView.Adapter<AsmHistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AsmHistoryModel> historyList;
    private Spannable description;
    AsmHistoryModel mAsmModel;
    OnBackPressListener listener;


    public AsmHistoryAdapter(Context mContext, ArrayList<AsmHistoryModel> historyList) {
        this.mContext = mContext;
        this.historyList = historyList;
        listener = (OnBackPressListener) mContext;
    }


    @NonNull
    @Override
    public AsmHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asm_history_row_items, parent, false);
        return new AsmHistoryAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount() > 0) {
            mAsmModel = historyList.get(position);
            holder.tv_date_time.setText(mAsmModel.getTime());
            String text = mAsmModel.getDescription();
            PicassoImageGetter imageGetter = new PicassoImageGetter(holder.tv_description, mContext);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description = (Spannable) Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
            } else {
                description = (Spannable) Html.fromHtml(text, imageGetter, null);
            }
            holder.tv_description.setText(description);

            if (TextUtils.isEmpty(mAsmModel.getRecording())) {
                holder.toggle_play.setVisibility(View.GONE);
            } else {
                holder.toggle_play.setVisibility(View.VISIBLE);
                holder.toggle_play.setBackgroundResource(R.drawable.ic_play_circle);
            }
        } else {
            Toast.makeText(mContext, "No Data Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if (historyList == null || historyList.isEmpty())
            return 0;
        return historyList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date_time;
        TextView tv_description;
        ImageView toggle_play;

        public ViewHolder(View v) {
            super(v);
            tv_date_time = v.findViewById(R.id.tv_date_time);
            tv_description = v.findViewById(R.id.tv_description);
            toggle_play = v.findViewById(R.id.toggle_play);
            toggle_play.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.toggle_play) {
                try {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        mAsmModel = historyList.get(position);
                        listener.createAndPlayDialog(mAsmModel.getRecording());
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public interface OnBackPressListener {
        void createAndPlayDialog(String audioFilePath);
    }
}
