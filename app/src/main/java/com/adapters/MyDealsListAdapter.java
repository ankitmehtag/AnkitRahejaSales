package com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.helper.BMHConstants;
import com.model.InventoryRespModel;
import com.sp.R;
import com.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class MyDealsListAdapter extends RecyclerView.Adapter<MyDealsListAdapter.MyViewHolder> {

    private ArrayList<InventoryRespModel.Data> inventoryList;
    private Context mContext;
    private File mFile;
    private String savingPath;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, title;
        private ImageView fileIcon;
        private ImageView iv_download_status;

        private MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.tv_date);
            title = view.findViewById(R.id.tv_title);
            fileIcon = view.findViewById(R.id.iv_file_ico);
            iv_download_status = view.findViewById(R.id.iv_download_status);
        }
    }

    public MyDealsListAdapter(Context context, ArrayList<InventoryRespModel.Data> inventoryList) {
        this.inventoryList = inventoryList;
        mContext = context;
        mFile = Utils.createFileDirectory(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_deals_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InventoryRespModel.Data data = inventoryList.get(position);
        holder.title.setText(data.getFile_name());
        holder.date.setText(data.getUpload_date());

        if (mFile != null) {
            savingPath = mFile + File.separator + data.getFile_name();
            File file = new File(savingPath);
            if (file.exists()) {
                holder.iv_download_status.setImageResource(R.mipmap.undownload);
            } else {
                holder.iv_download_status.setImageResource(R.mipmap.download);
            }
        }
        if (data.getDownload_path() != null && !data.getDownload_path().isEmpty()) {
          //  String imgUrl = data.getDownload_path().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + data.getDownload_path() : UrlFactory.IMG_BASEURL + data.getDownload_path();
            if (data.getFile_type().equalsIgnoreCase("image")) {
                Glide.with(mContext).load(R.drawable.jpeg).placeholder(BMHConstants.PLACE_HOLDER).into(holder.fileIcon);
            } else if (data.getFile_type().equalsIgnoreCase("pdf")) {
                Glide.with(mContext).load(R.drawable.pdf).placeholder(BMHConstants.PLACE_HOLDER).into(holder.fileIcon);
            } else if (data.getFile_type().equalsIgnoreCase("docs")) {
                Glide.with(mContext).load(R.drawable.word).placeholder(BMHConstants.PLACE_HOLDER).into(holder.fileIcon);
            } else if (data.getFile_type().equalsIgnoreCase("excel")) {
                Glide.with(mContext).load(R.drawable.excel).placeholder(BMHConstants.PLACE_HOLDER).into(holder.fileIcon);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (inventoryList != null) {
            return inventoryList.size();
        }
        return 0;
    }

   /* @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private MyDealsListAdapter mAdapter;
        private Context mContext;
        private CustomFilter(Context context,MyDealsListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
            if (context instanceof InventoriesActivity){
                mContext=context;
            }else{
                mContext=context;
            }
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<InventoryRespModel.Data> mList= inventoryList;

            inventoryList.clear();
            final FilterResults results = new FilterResults();

           *//* if (constraint.length() == 0) {
                if (mContext instanceof InventoriesActivity){
                    inventoryList.addAll(((InventoriesActivity) mContext).filteredList);
                }else {
                    inventoryList.addAll(((MyDealsActivity) mContext).filteredList);
                }

            } else {*//*
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final InventoryRespModel.Data obj : mList) {
                    if (obj.getUpload_date().toLowerCase().startsWith(filterPattern)) {
                        inventoryList.add(obj);
                    }
               // }
            }
            System.out.println("Count Number " + inventoryList.size());
            results.values = inventoryList;
            results.count = inventoryList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<InventoryRespModel>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }*/
}