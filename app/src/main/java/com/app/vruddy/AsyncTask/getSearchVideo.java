package com.app.vruddy.AsyncTask;

import android.os.AsyncTask;

import com.app.vruddy.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class getSearchVideo extends AsyncTask<String, Integer, String> {

    private static boolean isThereId = false;
    private static boolean isThereDate = false;
    private static boolean isThereTime = false;

    private static String continuation_key = null;
    private static String clickTrackingParams_key = null;
    private static String visitor_data_key = null;
    private static String client_version_key = null;
    private static String main_url = null;

    private static ArrayList<String> videoId = new ArrayList<>();
    private static  ArrayList<String>thumbnailUrl = new ArrayList<>();
    private static  ArrayList<String>videoTitle = new ArrayList<>();
    private static  ArrayList<String>viewsInfo = new ArrayList<>();
    private static  ArrayList<String>showTime = new ArrayList<>();
    private static  ArrayList<String>dateInfo = new ArrayList<>();
    private static  ArrayList<String>channelName = new ArrayList<>();
    private static  ArrayList<String>channelImgUrl = new ArrayList<>();
    private static  ArrayList<Integer>heartIcon = new ArrayList<>();
    private static  ArrayList<Integer>DownloadIcon = new ArrayList<>();
    private static  ArrayList<Integer>badgeIcon = new ArrayList<>();
    private static  ArrayList<Boolean>isLive = new ArrayList<>();

    //getters and setters


    public static ArrayList<String> getVideoId() {
        return videoId;
    }

    public static ArrayList<String> getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static ArrayList<String> getVideoTitle() {
        return videoTitle;
    }

    public static ArrayList<String> getViewsInfo() {
        return viewsInfo;
    }

    public static ArrayList<String> getShowTime() {
        return showTime;
    }

    public static ArrayList<String> getDateInfo() {
        return dateInfo;
    }

    public static ArrayList<String> getChannelName() {
        return channelName;
    }

    public static ArrayList<String> getChannelImgUrl() {
        return channelImgUrl;
    }

    public static ArrayList<Integer> getHeartIcon() {
        return heartIcon;
    }

    public static ArrayList<Integer> getDownloadIcon() {
        return DownloadIcon;
    }

    public static ArrayList<Integer> getBadgeIcon() {
        return badgeIcon;
    }

    public static ArrayList<Boolean> getIsLive() {
        return isLive;
    }

    //--------------------------------
    public static String getContinuation_key() {
        return continuation_key;
    }

    public static void setContinuation_key(String continuation_key) {
        getSearchVideo.continuation_key = continuation_key;
    }

    public static String getClickTrackingParams_key() {
        return clickTrackingParams_key;
    }

    public static void setClickTrackingParams_key(String clickTrackingParams_key) {
        getSearchVideo.clickTrackingParams_key = clickTrackingParams_key;
    }

    public static String getVisitor_data_key() {
        return visitor_data_key;
    }

    public static void setVisitor_data_key(String visitor_data_key) {
        getSearchVideo.visitor_data_key = visitor_data_key;
    }

    public static String getClient_version_key() {
        return client_version_key;
    }

    public static void setClient_version_key(String client_version_key) {
        getSearchVideo.client_version_key = client_version_key;
    }

    public static String getMain_url() {
        return main_url;
    }

    public static void setMain_url(String main_url) {
        getSearchVideo.main_url = main_url;
    }

    @Override
    protected String doInBackground(String... strings) {

        System.out.println("I cleared the previous data for you :)");
                   videoId.clear(); thumbnailUrl.clear(); videoTitle.clear();
                   viewsInfo.clear(); showTime.clear();
                   dateInfo.clear(); channelName.clear();
                   channelImgUrl.clear(); heartIcon.clear();
                   DownloadIcon.clear(); badgeIcon.clear();isLive.clear();

        searchRequset(strings[0]);
        return null;
    }
    public static void searchRequset(String search_keyword){

        search_keyword = search_keyword.replaceAll(" ", "+");
        setMain_url("https://www.youtube.com/results?search_query="+search_keyword+"&pbj=1");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getMain_url())
                .addHeader("Accept-Language","en-US,en;q=0.5")
                .addHeader("Connection","keep-alive")
                .addHeader("authority","www.youtube.com")
                .addHeader("x-youtube-client-name","1")
                .addHeader("x-youtube-client-version","2.20201202.04.00")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Bad Request!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //System.out.println(response.body().string());
                try{
                    extractData(response.body().string());
                }catch (Exception e){
                    //System.out.println("error: "+e);
                }
            }
        });
    }
    public static void extractData(String jsonString){

        if(jsonString != null){

            //Convert jsonString to JsonArray object
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = null;

            try {
                jsonArray = (JSONArray) parser.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //start extract data
            JSONObject jsonObject = (JSONObject) jsonArray.get(1);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject contents = (JSONObject) response.get("contents");
            JSONObject twoColumnSearchResultsRenderer = (JSONObject) contents.get("twoColumnSearchResultsRenderer");
            JSONObject primaryContents = (JSONObject) twoColumnSearchResultsRenderer.get("primaryContents");
            JSONObject sectionListRenderer = (JSONObject) primaryContents.get("sectionListRenderer");
            JSONArray sec_contents = (JSONArray) sectionListRenderer.get("contents");
            JSONObject data_contents = (JSONObject) sec_contents.get(0);
            JSONObject itemSectionRenderer = (JSONObject) data_contents.get("itemSectionRenderer");
            JSONArray thr_contents = (JSONArray) itemSectionRenderer.get("contents");

            for(int i = 0; i < thr_contents.size(); i++){

                System.out.println("-------------------------------------------");
                JSONObject get_data = (JSONObject) thr_contents.get(i);
                JSONObject videoRenderer = (JSONObject) get_data.get("videoRenderer");

                try {
                    //Check if arrive to the last index in the list
                    //the last index doesn't contain video data
                    if(i != thr_contents.size()-1) {

                        //Start getting Video Data
                        //Video id
                        try {
                            System.out.println("Video id: "+videoRenderer.get("videoId"));
                            videoId.add((String) videoRenderer.get("videoId"));
                            isThereId = true;
                        } catch (Exception e) {
                            System.out.println("There's no id");
                            isThereId = false;

                            //Check if this index contain on video id before start extracting data
                        }if(isThereId != false){

                            //Thumbnail
                            try {
                                JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                                JSONArray thumbnails = (JSONArray) thumbnail.get("thumbnails");
                                JSONObject getUrl = (JSONObject) thumbnails.get(0);

                                System.out.println("Thumbnails: " + getUrl.get("url"));
                                thumbnailUrl.add((String) getUrl.get("url"));
                            }catch (Exception e){
                                System.out.println("No Thumbnail!");
                                thumbnailUrl.add("");
                            }
                            //Title
                            try{
                                JSONObject title = (JSONObject) videoRenderer.get("title");
                                JSONArray runs = (JSONArray) title.get("runs");
                                JSONObject text = (JSONObject) runs.get(0);
                                System.out.println("Video Title: "+text.get("text"));
                                videoTitle.add((String) text.get("text"));
                            }catch (Exception e){
                                System.out.println("No Title!");
                                videoTitle.add("");
                            }
                            //Channel Name
                            try{
                                JSONObject longBylineText = (JSONObject) videoRenderer.get("longBylineText");
                                JSONArray runs = (JSONArray) longBylineText.get("runs");
                                JSONObject channel_name = (JSONObject) runs.get(0);
                                System.out.println("Channel name: "+channel_name.get("text"));
                                channelName.add((String) channel_name.get("text"));
                            }catch (Exception e){
                                System.out.println("No channel name!");
                                channelName.add("");
                            }
                            //Channel Picture
                            try{
                                JSONObject channelThumbnailSupportedRenderers = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                                JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnailSupportedRenderers.get("channelThumbnailWithLinkRenderer");
                                JSONObject channel_thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                                JSONArray channel_thumbnails = (JSONArray) channel_thumbnail.get("thumbnails");
                                JSONObject pic_url = (JSONObject) channel_thumbnails.get(0);
                                System.out.println("Channel Picture: "+pic_url.get("url"));
                                channelImgUrl.add((String) pic_url.get("url"));
                            }catch (Exception e){
                                System.out.println("No Channel Picture!");
                                channelImgUrl.add("");
                            }
                            //Date
                            try{
                                JSONObject publishedTimeText = (JSONObject) videoRenderer.get("publishedTimeText");
                                System.out.println("Video Date: "+ publishedTimeText.get("simpleText"));
                                dateInfo.add((String) publishedTimeText.get("simpleText"));
                                isThereDate = true;
                            }catch (Exception e){
                                System.out.println("No Video Date!");
                                dateInfo.add("");
                                isThereDate = false;
                            }
                            //Video Time
                            try{
                                JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                                System.out.println("Video time: "+ lengthText.get("simpleText"));
                                showTime.add((String) lengthText.get("simpleText"));
                                isThereTime = true;
                            }catch (Exception e){
                                System.out.println("No Video time!");
                                showTime.add("");
                                isThereTime = false;
                            }
                            if(isThereTime != false & isThereDate != false) {
                                //Video Views
                                try {
                                    JSONObject shortViewCountText = (JSONObject) videoRenderer.get("shortViewCountText");
                                    System.out.println("Video views: " + shortViewCountText.get("simpleText"));
                                    viewsInfo.add((String) shortViewCountText.get("simpleText"));
                                } catch (Exception e) {
                                    System.out.println("No Video views!");
                                    viewsInfo.add("");
                                }
                                isLive.add(false);
                            }else {
                                System.out.println("is Live Now ⬤");
                                isLive.add(true);
                                viewsInfo.add("");
                            }
                            //badge
                            try{
                                if(videoRenderer.get("ownerBadges") != null){
                                    System.out.println("Badge: ✔");
                                    badgeIcon.add(R.drawable.ic_badge);
                                }else {
                                    System.out.println("Badge: X");
                                    badgeIcon.add(0);
                                }
                            }catch (Exception e){
                                System.out.println("No Badge");
                                badgeIcon.add(0);
                            }
                            DownloadIcon.add(R.drawable.ic_download);
                            heartIcon.add(R.drawable.ic_save_for_later);
                            //
                            System.out.println("------------------------------------------------------------------");



                        }else {
                            System.out.println("I prefer to don't extract "+i+" index data cause it doesn't contain Video id!");
                        }

                    }else{

                        System.out.println("Last index!");
                    }
                }catch (Exception e){
                    System.out.println("I get Some troubles in extracting video data!");
                }

            }

            //continuation Key
            JSONObject continuationKey = (JSONObject) sec_contents.get(1);
            JSONObject continuationItemRenderer = (JSONObject) continuationKey.get("continuationItemRenderer");
            JSONObject continuationEndpoint = (JSONObject) continuationItemRenderer.get("continuationEndpoint");
            JSONObject continuationCommand = (JSONObject) continuationEndpoint.get("continuationCommand");

            //client version id
            JSONObject responseContext = (JSONObject) response.get("responseContext");
            JSONArray serviceTrackingParams = (JSONArray) responseContext.get("serviceTrackingParams");
            JSONObject get_params = (JSONObject) serviceTrackingParams.get(2);
            JSONArray params = (JSONArray) get_params.get("params");
            JSONObject get_clieant_version_id = (JSONObject) params.get(2);

            //visitor Data
            JSONObject webResponseContextExtensionData = (JSONObject) responseContext.get("webResponseContextExtensionData");
            JSONObject ytConfigData = (JSONObject) webResponseContextExtensionData.get("ytConfigData");

            /*
             *extract parameters Key
             */
            //extract client version id
            System.out.println("client version id key: "+get_clieant_version_id.get("value"));
            setClient_version_key((String) get_clieant_version_id.get("value"));

            //extract visitor Data
            System.out.println("Visitor data key: "+ ytConfigData.get("visitorData"));
            setVisitor_data_key((String) ytConfigData.get("visitorData"));

            //extract clickTrackingParams key
            System.out.println("clickTrackingParams Key: "+ continuationEndpoint.get("clickTrackingParams"));
            setClickTrackingParams_key((String) continuationEndpoint.get("clickTrackingParams"));

            //extract continuation Key
            System.out.println("Con Key: "+ continuationCommand.get("token"));
            setContinuation_key((String) continuationCommand.get("token"));


        }else {
            System.out.println("Error: JsonString in 'extractData' get null value!");
        }
    }
    public static void moreDataRequset(String url, String continuation_key, String trackingParams_key, String client_version_key, String visitor_data_key){

        if(continuation_key != null & trackingParams_key != null&
                visitor_data_key != null & client_version_key != null & url != null) {
            String json = "{\"context\":{\"client\":{\"hl\":\"en\",\"gl\":\"MA\",\"geo\":\"MA\",\"remoteHost\":\"\",\"isInternal\":true,\"deviceMake\":\"\",\"deviceModel\":\"\",\"visitorData\":\"" + getVisitor_data_key() + "\",\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0,gzip(gfe)\",\"clientName\":\"WEB\",\"clientVersion\":\"" + getClient_version_key() + "\",\"osName\":\"\",\"osVersion\":\"\",\"originalUrl\":\"https://www.youtube.com/\",\"internalClientExperimentIds\":[25456254],\"platform\":\"DESKTOP\",\"clientFormFactor\":\"UNKNOWN_FORM_FACTOR\",\"newVisitorCookie\":true,\"countryLocationInfo\":{\"countryCode\":\"MA\",\"countrySource\":\"\"},\"browserName\":\"Firefox\",\"browserVersion\":\"83.0\",\"screenWidthPoints\":1280,\"screenHeightPoints\":391,\"screenPixelDensity\":1,\"screenDensityFloat\":1,\"utcOffsetMinutes\":-480,\"userInterfaceTheme\":\"USER_INTERFACE_THEME_LIGHT\",\"mainAppWebInfo\":{\"graftUrl\":\"" + url + "\"},\"timeZone\":\"America/Los_Angeles\"},\"user\":{\"lockedSafetyMode\":false},\"request\":{\"useSsl\":true,\"sessionId\":0,\"parentEventId\":{\"timeUsec\":0,\"serverIp\":0,\"processId\":-25456254},\"internalExperimentFlags\":[],\"consistencyTokenJars\":[]},\"clickTracking\":{\"clickTrackingParams\":\"" + getClickTrackingParams_key() + "\"},\"adSignalsInfo\":{\"params\":[{\"key\":\"dt\",\"value\":\"0\"},{\"key\":\"flash\",\"value\":\"0\"},{\"key\":\"frm\",\"value\":\"0\"},{\"key\":\"u_tz\",\"value\":\"0\"},{\"key\":\"u_his\",\"value\":\"0\"},{\"key\":\"u_java\",\"value\":\"false\"},{\"key\":\"u_h\",\"value\":\"0\"},{\"key\":\"u_w\",\"value\":\"0\"},{\"key\":\"u_ah\",\"value\":\"0\"},{\"key\":\"u_aw\",\"value\":\"0\"},{\"key\":\"u_cd\",\"value\":\"0\"},{\"key\":\"u_nplug\",\"value\":\"0\"},{\"key\":\"u_nmime\",\"value\":\"0\"},{\"key\":\"bc\",\"value\":\"0\"},{\"key\":\"bih\",\"value\":\"0\"},{\"key\":\"biw\",\"value\":\"0\"},{\"key\":\"brdim\",\"value\":\"-0,-0,-0,-0,0,0,0,0,0,0\"},{\"key\":\"vis\",\"value\":\"0\"},{\"key\":\"wgl\",\"value\":\"true\"},{\"key\":\"ca_type\",\"value\":\"image\"}],\"consentBumpParams\":{\"consentHostnameOverride\":\"https://www.youtube.com\",\"urlOverride\":\"\"}},\"clientScreenNonce\":\"\"},\"continuation\":\"" + getContinuation_key() + "\"}";
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url("https://www.youtube.com/youtubei/v1/search?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
                    .header("Host", "www.youtube.com")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
                    .header("Accept", "*/*")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Referer", url)
                    .header("Content-Type", "application/json")
                    .header("X-Youtube-Client-Name", "1")
                    .header("X-Youtube-Client-Version", client_version_key)
                    .header("Origin", "https://www.youtube.com")
                    .header("DNT", "1")
                    .header("Connection", "keep-alive")
                    .header("Cookie", "GPS=\"1\"; VISITOR_INFO1_LIVE=\"sPQwkVUmUR8\"; YSC=\"nIEJQK1yKRU\"")

                    .method("POST", body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Bad Request: " + call);
                    System.out.println("Request error: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    //System.out.println(response.body().string());
                    try {
                        ExtractMoreData(response.body().string());
                    }catch (Exception e){
                        System.out.println("error Bla bla! in: "+e);
                    }
                }
            });
        }else {
            System.out.println("Error: null input in 'searchData' 'moreDataRequset' method");
        }

    }
    public static void ExtractMoreData(String jsonString){

        setContinuation_key(null);
        setClickTrackingParams_key(null);

        JSONParser Jparser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) Jparser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray onResponseReceivedCommands = (JSONArray) jsonObject.get("onResponseReceivedCommands");
        JSONObject get_appendContinuationItemsAction = (JSONObject) onResponseReceivedCommands.get(0);
        JSONObject appendContinuationItemsAction = (JSONObject) get_appendContinuationItemsAction.get("appendContinuationItemsAction");
        JSONArray continuationItems = (JSONArray) appendContinuationItemsAction.get("continuationItems");
        JSONObject get_itemSectionRenderer = (JSONObject) continuationItems.get(0);
        JSONObject itemSectionRenderer = (JSONObject) get_itemSectionRenderer.get("itemSectionRenderer");
        JSONArray contents = (JSONArray) itemSectionRenderer.get("contents");

        for (int i = 0; i < contents.size(); i++){

            JSONObject get_videoRenderer = (JSONObject) contents.get(i);
            JSONObject videoRenderer = (JSONObject) get_videoRenderer.get("videoRenderer");

            //Video id
            try {
                System.out.println("Id: " + videoRenderer.get("videoId"));
                videoId.add((String) videoRenderer.get("videoId"));

                //Thumbnail
                try {
                    JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                    JSONArray getThumbnails = (JSONArray) thumbnail.get("thumbnails");
                    JSONObject getUrl = (JSONObject) getThumbnails.get(0);
                    System.out.println("Thumbnail: " + getUrl.get("url"));
                    thumbnailUrl.add((String) getUrl.get("url"));
                } catch (Exception e) {
                    System.out.println("Error in Thumbnail");
                    thumbnailUrl.add("");
                }

                //title
                try {
                    JSONObject title = (JSONObject) videoRenderer.get("title");
                    JSONArray runs = (JSONArray) title.get("runs");
                    JSONObject get_text = (JSONObject) runs.get(0);
                    System.out.println("Title: " + get_text.get("text"));
                    videoTitle.add((String) get_text.get("text"));

                } catch (Exception e) {
                    System.out.println("Error in Title");
                    videoTitle.add("");
                }

                //Video Date
                try {
                    JSONObject date = (JSONObject) videoRenderer.get("publishedTimeText");
                    System.out.println("Date: " + date.get("simpleText"));
                    dateInfo.add((String) date.get("simpleText"));
                    isThereDate = true;
                } catch (Exception e) {
                    isThereDate = false;
                    dateInfo.add("");
                }

                //Video Time
                try {
                    JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                    System.out.println("Time: " + lengthText.get("simpleText"));
                    showTime.add((String) lengthText.get("simpleText"));
                    isThereTime = true;
                } catch (Exception e) {
                    isThereTime = false;
                    showTime.add("");
                }

                if (isThereDate != false & isThereTime != false) {

                    //Video Views
                    try {
                        JSONObject shortViewCountText = (JSONObject) videoRenderer.get("shortViewCountText");
                        System.out.println("Views: " + shortViewCountText.get("simpleText"));
                        viewsInfo.add((String) shortViewCountText.get("simpleText"));
                    } catch (Exception e) {
                        System.out.println("Error in Views");
                        viewsInfo.add("");
                    }
                    isLive.add(false);
                } else {
                    System.out.println("Live Now ●");
                    isLive.add(true);
                    viewsInfo.add("");
                }

                //Channel Name
                try {
                    JSONObject shortBylineText = (JSONObject) videoRenderer.get("shortBylineText");
                    JSONArray runs = (JSONArray) shortBylineText.get("runs");
                    JSONObject text = (JSONObject) runs.get(0);
                    System.out.println("Channel Name: " + text.get("text"));
                    channelName.add((String) text.get("text"));
                } catch (Exception e) {
                    System.out.println("Error in Channel Name");
                    channelName.add("");
                }

                //Channel Thumbnail
                try {
                    JSONObject channelThumbnail = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                    JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnail.get("channelThumbnailWithLinkRenderer");
                    JSONObject thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                    JSONArray getChannelThumbnails = (JSONArray) thumbnail.get("thumbnails");
                    JSONObject Url = (JSONObject) getChannelThumbnails.get(0);
                    System.out.println("Channel Thumbnail: " + Url.get("url"));
                    channelImgUrl.add((String) Url.get("url"));
                } catch (Exception e) {
                    System.out.println("Error in Channel Thumbnail");
                    channelImgUrl.add("");
                }

                //Badge
                if (videoRenderer.get("badges") != null) {
                    System.out.println("Badge: ✔");
                    badgeIcon.add(R.drawable.ic_badge);
                } else {
                    System.out.println("Badge: ✘");
                    badgeIcon.add(0);
                }
            } catch (Exception e) {

            }
            DownloadIcon.add(R.drawable.ic_download);
            heartIcon.add(R.drawable.ic_save_for_later);

            System.out.println("----------------------------------------------");

        }

        //Extract parameters
        JSONObject get_continuationItemRenderer = (JSONObject) continuationItems.get(1);
        JSONObject continuationItemRenderer = (JSONObject) get_continuationItemRenderer.get("continuationItemRenderer");
        JSONObject continuationEndpoint = (JSONObject) continuationItemRenderer.get("continuationEndpoint");

        //clickTrackingParams key
        System.out.println("clickTrackingParams key: "+continuationEndpoint.get("clickTrackingParams"));
        setClickTrackingParams_key((String) continuationEndpoint.get("clickTrackingParams"));

        //con key
        JSONObject continuationCommand = (JSONObject) continuationEndpoint.get("continuationCommand");
        System.out.println("con key: "+continuationCommand.get("token"));
        setContinuation_key((String) continuationCommand.get("token"));

    }
    public static void moreData(){

        if(getContinuation_key() != null & getClickTrackingParams_key() != null&
                getVisitor_data_key() != null & getClient_version_key() != null & getMain_url() != null) {

            moreDataRequset(getMain_url(), getContinuation_key(), getClickTrackingParams_key(), getClient_version_key(), getVisitor_data_key());
        }else {
            System.out.println("not ready!");
        }
    }
}
