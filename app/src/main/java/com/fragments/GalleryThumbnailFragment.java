/*
 * Copyright (C) 2014 Pixplicity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.model.GalleryRespData;
import com.utils.Utils;
import com.squareup.picasso.Picasso;


public class GalleryThumbnailFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "pageNumber";
    public GalleryRespData.Data data = null;

    public static GalleryThumbnailFragment getInstance(int pageNumber, GalleryRespData.Data data) {
        GalleryThumbnailFragment fragment = new GalleryThumbnailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        bundle.putSerializable("obj",data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        data = (GalleryRespData.Data) arguments.getSerializable("obj");
        View rootView = inflater.inflate(R.layout.gallery_thumbnail, container, false);
        ImageView iv_thumbnail = (ImageView)rootView.findViewById(R.id.iv_thumbnail);
        if(data.isSelected()) iv_thumbnail.setPadding((int) Utils.dp2px(2,getActivity()),(int)Utils.dp2px(2,getActivity()),(int)Utils.dp2px(2,getActivity()),(int)Utils.dp2px(2,getActivity()));
        else iv_thumbnail.setPadding(0,0,0,0);
        rootView.setTag(R.integer.project_item,data);
        rootView.setOnClickListener(mOnClickListener);
        if(data.getType().equalsIgnoreCase("photo")) {
            String separator = UrlFactory.IMG_BASEURL.endsWith("/") ? "" : "/";
            String imgurl = data.getUrl().startsWith(UrlFactory.IMG_BASEURL) ? data.getUrl() : UrlFactory.IMG_BASEURL + separator + data.getUrl();
            Picasso.with(getActivity()).load(imgurl).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER)
                    .resize((int) Utils.dp2px(150, getActivity()), (int) Utils.dp2px(100, getActivity()))
                    .into(iv_thumbnail);
            return rootView;
        }else if(data.getType().equalsIgnoreCase("Video")){
            iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
            Picasso.with(getActivity()).load(Utils.getYoutubeThumbnailUrl(data.getUrl())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER)
                    .resize((int) Utils.dp2px(150, getActivity()), (int) Utils.dp2px(100, getActivity()))
                    .into(iv_thumbnail);
        }else{
            //TODO:
            iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
            return rootView;
        }
        return rootView;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentClickListener mFragmentClickListener =  (FragmentClickListener)getActivity();
            mFragmentClickListener.myOnClickListener(GalleryThumbnailFragment.this,v.getTag(R.integer.project_item));
        }
    };

}
