package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 21-Jan-17.
 */
public class GalleryRespData extends BaseRespModel implements Serializable{

    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data  implements Serializable{
        private String gallary_type;
        private String url;
        private String type;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getGallary_type() {
            return gallary_type;
        }

        public void setGallary_type(String gallary_type) {
            this.gallary_type = gallary_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
