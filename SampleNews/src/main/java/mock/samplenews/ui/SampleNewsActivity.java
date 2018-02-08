package mock.samplenews.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import mock.samplenews.Constants;
import mock.samplenews.model.CustomAdapter;
import mock.samplenews.utils.JsonUtils;
import mock.samplenews.model.NewsData;
import mock.samplenews.utils.NewsUtils;
import mock.samplenews.R;
import mock.samplenews.ResponseCallback;
import mock.samplenews.SampleNewsHolder;
import mock.samplenews.service.ServiceDataClass;

public class SampleNewsActivity extends AppCompatActivity implements ResponseCallback {
    private final String TAG = SampleNewsActivity.class.getSimpleName();
	private ListView newsList;
    private CustomAdapter adapter = null;
    private int firstVisible = -1;
    private int totalVisible = -1;
    private JsonUtils jsonUtils;
    private ServiceDataClass serviceDataClass;
    private ProgressDialog mProgressDialog;
    private SampleNewsHolder sampleNewsHolder = new SampleNewsHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        newsList = (ListView)findViewById(R.id.menuList);
        NewsUtils.setNewsFeedHolder(sampleNewsHolder);
        serviceDataClass = new ServiceDataClass(SampleNewsActivity.this,
                Constants.SERVICE_URL);
        serviceDataClass.setResponseCallback(this);
        serviceDataClass.execute();

        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING){
                    Log.d(TAG,"SampleNewsActivity - onScrollState: FLING ----:"+scrollState);
                }else if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    Log.d(TAG,"SampleNewsActivity - onScrollState:IDLE ----:"+scrollState);
                    /**
                     * Images will start downloading for visible section
                     * of screen once user has swiped or pressed refresh
                     */
                }else{
                    Log.d(TAG,"SampleNewsActivity - onScrollState: ----:"+scrollState);
                }

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                totalVisible = visibleItemCount;
                if(firstVisible == firstVisibleItem)
                    return;
                if(firstVisible == -1 ){
                    firstVisible = firstVisibleItem;
                    totalVisible = visibleItemCount;
                    return;
                }

                if(firstVisible != firstVisibleItem){
                    firstVisible = firstVisibleItem;
                    totalVisible = visibleItemCount;
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            /**
             * Images will start downloading for visible section
             * of screen once user has swiped or pressed refresh
             */
            /*imageUtils = new ImageUtils(SampleNewsActivity.this,
                    firstVisible,totalVisible);
            imageUtils.setResponseCallback(SampleNewsActivity.this);
            imageUtils.execute();*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String result) {
        jsonUtils = new JsonUtils(SampleNewsActivity.this,
                result);
        jsonUtils.setResponseCallback(this);
        jsonUtils.execute();
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(getApplicationContext(),"Error:"+errorMessage,Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onUpdate(int state,int index){
        switch(state){
            case Constants.DIALOG_DISMISS:
                dismiss();
                break;
            case Constants.LIST_UPDATE:
                update(index);
                break;
            case Constants.JSON_PARSE_PARTIAL:
                update(index);
                break;
            case Constants.JSON_PARSE_COMPLETED:
                dismiss();
                break;
            case Constants.SHOW_DIALOG:
                showProgressDialog("");
                break;
            case Constants.UPDATE_TITLE:
                getSupportActionBar().setTitle(NewsData.newsTitle);
                break;
            case Constants.IMAGE_DOWNLOAD_COMPLETED:
                update(index);
                dismiss();
                break;

            default:
        }
    }

    public NewsData getNewsItem(int index){
        return sampleNewsHolder.getNewsDataList().get(index);
    }

    public void update(int index){
        if(adapter == null){
            adapter = new CustomAdapter(this,this);
            newsList.setAdapter(adapter);
        }
        adapter.updateNewsFeed();
    }

    public void showProgressDialog(String message){
        if(null == message || "".equals(message)){
            message = "Updating";
        }
        if(null == mProgressDialog){
            mProgressDialog = ProgressDialog.show(this, "", message + "...", true, false);
            mProgressDialog.setCancelable(true);
        }
    }

    public void dismiss(){
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch(IllegalArgumentException e){
            Log.e(TAG, "dismiss() - IllegalArgumentException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "dismiss() - Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy(){
        dismiss();
        if(null != serviceDataClass)
            serviceDataClass.cancel(true);
        if(null != jsonUtils)
            jsonUtils.cancel(true);
        NewsUtils.setNewsFeedHolder(null);
        super.onDestroy();
    }

}
