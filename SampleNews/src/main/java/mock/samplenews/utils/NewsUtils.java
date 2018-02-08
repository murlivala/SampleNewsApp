package mock.samplenews.utils;

import mock.samplenews.SampleNewsHolder;

public class NewsUtils {
    private static SampleNewsHolder sSampleNewsHolder;
    public static void setNewsFeedHolder(SampleNewsHolder sampleNewsHolder){
        sSampleNewsHolder = sampleNewsHolder;
    }
    public static SampleNewsHolder getsSampleNewsHolder(){
        return sSampleNewsHolder;
    }
}
