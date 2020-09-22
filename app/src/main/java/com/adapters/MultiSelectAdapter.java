package com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.model.Projects;
import com.model.SelectableProject;
import com.sp.R;

import java.util.List;
import java.util.Set;

/**
 * SANJAYA VERMA
 */
public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.ViewHolder> {

    Projects projectModel;
    List<Projects> mProjectsList;
    Set<Projects> projectsSet;
    Context context;
    OnItemSelectedListener itemSelectedListener;
    // private List<SelectableProject> mValues;

    public MultiSelectAdapter(Context context, List<Projects> mProjectsList, Set<Projects> selectedSet) {
        this.context = context;
        this.mProjectsList = mProjectsList;
        this.projectsSet = selectedSet;
        itemSelectedListener = (OnItemSelectedListener) context;

     /*   mValues = new ArrayList<>();
        for (Projects item : mProjectsList) {
            mValues.add(new SelectableProject(item, false));
        }*/
    }

    @NonNull
    @Override
    public MultiSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_row_item_recyclerview, parent, false);
        return new MultiSelectAdapter.ViewHolder(v, itemSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectAdapter.ViewHolder holder, int position) {
        projectModel = mProjectsList.get(position);
        holder.checkBox.setText(projectModel.getProject_name());
        holder.checkBox.setChecked(false);
        for (Projects project : projectsSet) {
            if (project.getProject_name().contains(projectModel.getProject_name())) {
                holder.checkBox.setChecked(true);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mProjectsList != null) {
            return mProjectsList.size();
        } else
            return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        CheckBox checkBox;
        SelectableProject mItem;

        public ViewHolder(View convertView, OnItemSelectedListener listener) {
            super(convertView);
            itemSelectedListener = listener;
            checkBox = convertView.findViewById(R.id.checkBox_project);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //    mItem = new SelectableProject(projectModel, isChecked);
            int position = getAdapterPosition();
            projectModel = mProjectsList.get(position);
            if (isChecked) {
                mItem = new SelectableProject(projectModel, isChecked);
                mItem.setSelected(true);
            } else {
                mItem.setSelected(false);
            }
            itemSelectedListener.onProjectSelected(mItem);
        }
    }

  /*  public List<Projects> getSelectedItems() {

        List<Projects> selectedItems = new ArrayList<>();
        for (SelectableProject item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }*/

    public interface OnItemSelectedListener {
        void onProjectSelected(SelectableProject item);
    }
}