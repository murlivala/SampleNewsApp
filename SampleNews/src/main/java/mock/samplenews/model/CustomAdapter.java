package mock.samplenews.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import mock.samplenews.R;
import mock.samplenews.ui.SampleNewsActivity;
import mock.samplenews.utils.NewsUtils;

public class CustomAdapter extends BaseAdapter {

    private final String TAG = CustomAdapter.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private List<NewsData> mList;

    public CustomAdapter(Context context, Activity activity) {
        mContext = context;
        mList = NewsUtils.getsSampleNewsHolder().getNewsDataList();
        if(activity instanceof SampleNewsActivity){
            mActivity = activity;
        }
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return NewsData.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return "";
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView description;
        ImageView imageView;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        View row = convertView;
        final ViewHolder viewHolder;
        try {
            if(null == convertView){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.row_samplenews, parent, false);
                viewHolder.description = (TextView) convertView.findViewById(R.id.item);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgIcon);
                viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);

                row = convertView;
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
                row = convertView;
            }
            if(mActivity instanceof SampleNewsActivity){
                viewHolder.title.setText(((SampleNewsActivity) mActivity).getNewsItem(position).title);
                viewHolder.description.setText(((SampleNewsActivity) mActivity).getNewsItem(position).description);
            }

            Picasso.with(mContext)
                    .load(mList.get(position).imgUrl)
                    .error(R.drawable.no_image_60_60)
                    .into(viewHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG,"CustomAdapter onSuccess:");
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG,"CustomAdapter onError()-"+mList.get(position).imgUrl);
                        }
                    });


        }catch(Exception e){
            Log.d(TAG,"CustomAdapter - getView ERROR => "+e.getMessage());
        }
        return (row);
    }

    public void updateNewsFeed() {
        mList = NewsUtils.getsSampleNewsHolder().getNewsDataList();
        notifyDataSetChanged();
    }
}