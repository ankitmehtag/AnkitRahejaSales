package com.adapters;

        import android.content.Context;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentStatePagerAdapter;
        import com.fragments.GalleryThumbnailFragment;
        import com.model.GalleryRespData;
        import java.util.ArrayList;

/**
 * Created by Naresh on 21-Jan-17.
 */

public class GalleryThumbnailAdapter extends FragmentStatePagerAdapter{
    private Context context;
    public ArrayList<GalleryRespData.Data> mGalleryRespData;
    private ArrayList<GalleryThumbnailFragment> mPageFragments = new ArrayList<>();

    public GalleryThumbnailAdapter(FragmentManager fm, Context c, ArrayList<GalleryRespData.Data> mGalleryRespData) {
        super(fm);
        context = c;
        this.mGalleryRespData = mGalleryRespData;
    }

    public int getCount() {
        if(mGalleryRespData != null)return mGalleryRespData.size();
        else return 0;
    }

    @Override
    public Fragment getItem(int position) {
        GalleryThumbnailFragment pageFragment =  GalleryThumbnailFragment.getInstance(position,mGalleryRespData.get(position));
       mPageFragments.add(pageFragment);
        return pageFragment;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

    public void setSelectedView(GalleryRespData.Data data){
        if(data == null)return;
        for (int i = 0; i< mGalleryRespData.size(); i++){
            if(data.getUrl().equals(mGalleryRespData.get(i).getUrl())){
               mPageFragments.get(i).data.setSelected(true);
                notifyDataSetChanged();
                break;
            }
            //if(data.getUrl().equals(mGalleryRespData.get(i).getUrl())) mGalleryRespData.get(i).setSelected(true);
           // else  mGalleryRespData.get(i).setSelected(false);
        }
       // notifyDataSetChanged();
    }
}

