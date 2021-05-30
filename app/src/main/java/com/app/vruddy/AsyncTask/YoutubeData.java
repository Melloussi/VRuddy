package com.app.vruddy.AsyncTask;



import android.os.AsyncTask;

import com.app.vruddy.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeData extends AsyncTask<String, Integer, String> {


    private ArrayList<String> videoTitle = new ArrayList<>();
    private ArrayList<String> channelName = new ArrayList<>();
    private ArrayList<String> videoTime = new ArrayList<>();
    private ArrayList<String> videoDate = new ArrayList<>();
    private ArrayList<String> videoViews = new ArrayList<>();
    private ArrayList<String> videoThumbnail = new ArrayList<>();
    private ArrayList<String> channelPicture = new ArrayList<>();
    private ArrayList<String> videoId = new ArrayList<>();
    private ArrayList<Integer>heartIcon = new ArrayList<>();
    private ArrayList<Integer>downloadIcon = new ArrayList<>();

    private boolean dataFetched = false;

    public ArrayList<Integer> getDownloadIcon() {
        return downloadIcon;
    }

    public void setDownloadIcon(ArrayList<Integer> downloadIcon) {
        this.downloadIcon = downloadIcon;
    }

    public ArrayList<Integer> getHeartIcon() {
        return heartIcon;
    }

    private String doc;

    public void setHeartIcon(ArrayList<Integer> heartIcon) {
        this.heartIcon = heartIcon;
    }

    //getter
    public ArrayList<String> getVideoId() {
        return videoId;
    }

    public ArrayList<String> getVideoTitle() {
        return videoTitle;
    }

    public ArrayList<String> getChannelName() {
        return channelName;
    }

    public ArrayList<String> getVideoTime() {
        return videoTime;
    }

    public ArrayList<String> getVideoDate() {
        return videoDate;
    }

    public ArrayList<String> getVideoViews() {
        return videoViews;
    }

    public ArrayList<String> getVideoThumbnail() {
        return videoThumbnail;
    }

    public ArrayList<String> getChannelPicture() {
        return channelPicture;
    }

    public boolean isDataFetched() {
        return dataFetched;
    }

    public void setDataFetched(boolean dataFetched) {
        this.dataFetched = dataFetched;
    }

    @Override
    protected String doInBackground(String... strings) {

        System.out.println("I cleared the previous data for you :)");
        videoId.clear(); videoThumbnail.clear(); videoTitle.clear();
        videoViews.clear(); videoTime.clear();downloadIcon.clear();
        videoDate.clear(); channelName.clear();
        channelPicture.clear(); heartIcon.clear();


        //my code
        //"https://www.youtube.com/"
        try {
            Document docc = Jsoup.connect("https://www.youtube.com/feed/trending").get();
            doc = docc.html();


            //pattren for jsoup
            String titlePattren = "\"title\":.\"runs\":\\[\\{\"text\":\".{1,200}\"\\}\\],";
            String channelNamePattren = ".ownerText.:.\"runs\":..\"text\":\"(.*?)\",";
            String longTimePattren = ".\"text\":.\"accessibility\":.\"accessibilityData\":.\"label\":\"(.*?)\"\\}\\},";
            String videoDatePattren = "\"publishedTimeText\":.\"simpleText\":\"(.*?)\"";
            String videoViewsPattren = "\"viewCountText\":.\"simpleText\":\"(.*?)\".,";
            String videoThumbnailsPattren = "\"thumbnail\":\\{\"thumbnails\":\\[\\{\"url\":\"https://i.ytimg.com(.*?)\",";
            String channelPicturePattren = "\"thumbnail\":\\{\"thumbnails\":\\[\\{\"url\":\"https://yt3.ggpht.com(.*?)\",";
            String videoIdPattren = "\"webCommandMetadata\":.\"url\":\"/watch.v=(.*?)\"";

            //for video title
            Pattern matcherT = Pattern.compile(titlePattren);
            Matcher mT = matcherT.matcher(doc);

            //for channel name
            Pattern matcherChannelName = Pattern.compile(channelNamePattren);
            Matcher channelNameMatcher = matcherChannelName.matcher(doc);

            //for video time
            Pattern matcherVideoTime = Pattern.compile(longTimePattren);
            Matcher VideoTimeMatcher = matcherVideoTime.matcher(doc);

            //for date of the video
            Pattern matcherVideoDate = Pattern.compile(videoDatePattren);
            Matcher videoDateMatcher = matcherVideoDate.matcher(doc);

            //for video views
            Pattern matcherVideoViews = Pattern.compile(videoViewsPattren);
            Matcher videoViewsMatcher = matcherVideoViews.matcher(doc);

            //for thumbnails
            Pattern matcherVideoThumbnails = Pattern.compile(videoThumbnailsPattren);
            Matcher videoThumbnailsMatcher = matcherVideoThumbnails.matcher(doc);

            //for channel picture
            Pattern matcherChannelPicture = Pattern.compile(channelPicturePattren);
            Matcher channelPictureMatcher = matcherChannelPicture.matcher(doc);

            //for video id
            Pattern matcherVideoId = Pattern.compile(videoIdPattren);
            Matcher videoIdMatcher = matcherVideoId.matcher(doc);

            //for video title
            while (mT.find() && channelNameMatcher.find() && VideoTimeMatcher.find() &&
                    videoDateMatcher.find() && videoViewsMatcher.find() && videoThumbnailsMatcher.find()
                    &&channelPictureMatcher.find() && videoIdMatcher.find()) {

                for (int i = 0; i <= mT.groupCount(); i++) {

                    //for video title
                    String titleResult = mT.group(0);
                    titleResult = titleResult.replace("\"title\":{\"runs\":[{\"text\":\"","");
                    titleResult = titleResult.replace("\"}],","");
                    titleResult = titleResult.replace("\\u0026","&");
                    //System.out.println("Video Title: " + titleResult);
                    videoTitle.add(titleResult);

                    //for channel Name
                    String channelNameResult = channelNameMatcher.group(0);
                    channelNameResult = channelNameResult.replace("\"ownerText\":{\"runs\":[{\"text\":\"","");
                    channelNameResult = channelNameResult.replace("\",","");
                    channelNameResult = channelNameResult.replace("\\u0026","&");
                    //System.out.println("Channel name: " + channelNameResult);
                    channelName.add(channelNameResult);

                    //for video time
                    String videoTimeResult = VideoTimeMatcher.group(0);
                    videoTimeResult = videoTimeResult.replace("{\"text\":{\"accessibility\":{\"accessibilityData\":{\"label\":\"","");
                    videoTimeResult = videoTimeResult.replace("\"}},","");
                    videoTimeResult = videoTimeResult.replace("\\u0026","&");
                    //System.out.println("Video Time: " + videoTimeResult);
                    videoTime.add(videoTimeResult);


                    //for video date
                    String videoDateResult = videoDateMatcher.group(0);
                    videoDateResult = videoDateResult.replace("\"publishedTimeText\":{\"simpleText\":\"","");
                    videoDateResult = videoDateResult.replace("\"","");
                    videoDateResult = videoDateResult.replace("\\u0026","&");
                    //System.out.println("Video Date: " + videoDateResult);
                    videoDate.add(videoDateResult);

                    //for video Views
                    String videoViewsResult = videoViewsMatcher.group(0);
                    videoViewsResult = videoViewsResult.replace("\"viewCountText\":{\"simpleText\":\"","");
                    videoViewsResult = videoViewsResult.replace("\"},","");
                    //System.out.println("Video Views: " + videoViewsResult);
                    videoViews.add(videoViewsResult);

                    //for Videos Thumbnails
                    String videoThumbnailsResult = videoThumbnailsMatcher.group(0);
                    videoThumbnailsResult = videoThumbnailsResult.replace("\"thumbnail\":{\"thumbnails\":[{\"url\":\"","");
                    videoThumbnailsResult = videoThumbnailsResult.replace("\",","");
                    //System.out.println("Video Thumbnail: " + videoThumbnailsResult);
                    videoThumbnail.add(videoThumbnailsResult);

                    //for channel picture
                    String channelPictureResult = channelPictureMatcher.group(0);
                    channelPictureResult = channelPictureResult.replace("\"thumbnail\":{\"thumbnails\":[{\"url\":\"","");
                    channelPictureResult = channelPictureResult.replace("\",","");
                    //System.out.println("Channel Picture: " + channelPictureResult);
                    channelPicture.add(channelPictureResult);

                    //for video id
                    String videoIdResult = videoIdMatcher.group(0);
                    videoIdResult = videoIdResult.replace("\"webCommandMetadata\":{\"url\":\"/watch?v=","");
                    videoIdResult = videoIdResult.replace("\"","");
                    //System.out.println("Video Id: " + videoIdResult);
                    videoId.add(videoIdResult);

                    if(i <= mT.groupCount()){
                        for(int i2 = 0; i2 <= mT.groupCount(); i2++){
                            heartIcon.add(R.drawable.ic_save_for_later);
                            downloadIcon.add(R.drawable.ic_download);
                        }
                    }

                    System.out.println("-------------------------------------");
                }
            }

//            VideoTimeMatcher.find();
//        System.out.println(VideoTimeMatcher.group(0));


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
