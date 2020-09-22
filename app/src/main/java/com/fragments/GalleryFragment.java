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
import android.widget.LinearLayout;

import com.AppEnums.MediaType;
import com.sp.BMHApplication;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.model.GalleryRespData;
import com.utils.Utils;
import com.views.TouchImageView;
import com.squareup.picasso.Picasso;


public class GalleryFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "pageNumber";

    public static GalleryFragment getInstance(int pageNumber, GalleryRespData.Data data) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        bundle.putSerializable("obj",data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BMHApplication app = (BMHApplication) getActivity().getApplication();
        final Bundle arguments = getArguments();
        GalleryRespData.Data data = (GalleryRespData.Data) arguments.getSerializable("obj");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if(MediaType.PHOTO == MediaType.getEnum(data.getType())) {
            ImageView iv_thumbnail = new TouchImageView(getActivity());
            iv_thumbnail.setImageResource(R.drawable.app_icon_img);
            iv_thumbnail.setTag(R.integer.project_item,data);
            iv_thumbnail.setOnClickListener(mOnClickListener);
            iv_thumbnail.setLayoutParams(params);
            if(data.getUrl() != null && !data.getUrl().isEmpty()) {
                String separator = UrlFactory.IMG_BASEURL.endsWith("/") ? "" : "/";
                String imgurl = data.getUrl().startsWith(UrlFactory.IMG_BASEURL) ? data.getUrl() : UrlFactory.IMG_BASEURL + separator + data.getUrl();
                Picasso.with(getActivity()).load(UrlFactory.getShortImageByWidthUrl(app.getWidth(getActivity()), imgurl)).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).into(iv_thumbnail);
            }
            return iv_thumbnail;
        }else if(MediaType.VIDEO == MediaType.getEnum(data.getType())){
            View videoView = LayoutInflater.from(getActivity()).inflate(R.layout.gallery_video_thumbnail,null);
            videoView.setTag(R.integer.project_item,data);
            videoView.setOnClickListener(mOnClickListener);
            ImageView iv_thumbnail = (ImageView)videoView.findViewById(R.id.iv_thumbnail);
            iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
            if(data.getUrl() != null && !data.getUrl().isEmpty()) {
                Picasso.with(getActivity()).load(Utils.getYoutubeThumbnailUrl(data.getUrl())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).resize((int) Utils.dp2px(150, getActivity()), (int) Utils.dp2px(100, getActivity())).into(iv_thumbnail);
            }
            return videoView;
        }else{
            ImageView iv_thumbnail = new ImageView(getActivity());
            iv_thumbnail.setTag(R.integer.project_item,data);
            iv_thumbnail.setOnClickListener(mOnClickListener);
            iv_thumbnail.setLayoutParams(params);
            iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
            return iv_thumbnail;
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentClickListener mFragmentClickListener =  (FragmentClickListener)getActivity();
            mFragmentClickListener.myOnClickListener(GalleryFragment.this,v.getTag(R.integer.project_item));
        }
    };

}
