package com.model;

public class SelectableProject extends Projects {
    private boolean isSelected = false;

    public SelectableProject(Projects item,boolean isSelected) {
        super(item.getProject_id(),item.getProject_name());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
