package mock.samplenews;

import java.util.ArrayList;
import java.util.List;

import mock.samplenews.model.NewsData;

public class SampleNewsHolder {
    private List<NewsData> newsData = new ArrayList<NewsData>();
    public void addNewsFeed(NewsData aFeed){
        newsData.add(aFeed);
    }

    public void addNewsFeed(int index,NewsData aFeed){
        newsData.add(index,aFeed);
    }

    public NewsData getNewsFeed(int index){
        return newsData.get(index);
    }

    public List<NewsData> getNewsDataList(){
        return newsData;
    }

    public void updateFeed(int index,NewsData aFeed){
        newsData.get(index).title = aFeed.title;
        newsData.get(index).description = aFeed.description;
        newsData.get(index).imgUrl = aFeed.imgUrl;
    }
}
