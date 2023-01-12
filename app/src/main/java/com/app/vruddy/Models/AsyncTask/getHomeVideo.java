package com.app.vruddy.Models.AsyncTask;

import android.os.AsyncTask;

import com.app.vruddy.Views.Fragment.HomeFragment;
import com.app.vruddy.R;
import com.app.vruddy.Models.TestCallBack;
import com.app.vruddy.Models.Objects.VideoObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class getHomeVideo extends AsyncTask<String, Integer, String> {

    private static String JsonStringRes = null;
    private static boolean isThereId = false;
    private static boolean isThereDate = false;
    private static boolean isThereTime = false;
    private static String jsonData = null;

    private static String continuation = null;
    private static String trackingParams = null;
    private static String Version = null;
    private static String Data = null;

    private static ArrayList<String> videoId = new ArrayList<>();
    private static ArrayList<String> thumbnailUrl = new ArrayList<>();
    private static ArrayList<String> videoTitle = new ArrayList<>();
    private static ArrayList<String> viewsInfo = new ArrayList<>();
    private static ArrayList<String> showTime = new ArrayList<>();
    private static ArrayList<String> dateInfo = new ArrayList<>();
    private static ArrayList<String> channelName = new ArrayList<>();
    private static ArrayList<String> channelImgUrl = new ArrayList<>();
    private static ArrayList<Integer> heartIcon = new ArrayList<>();
    private static ArrayList<Integer> DownloadIcon = new ArrayList<>();
    private static ArrayList<Integer> badgeIcon = new ArrayList<>();
    private static ArrayList<Boolean> isLive = new ArrayList<>();

    private static List<VideoObject> videoObjectList = new ArrayList<>();
    private static TestCallBack testCallBack;

    //arrayList getters ----------------------------------------------


    public static String getContinuation() {
        return continuation;
    }

    public static void setContinuation(String continuation) {
        getHomeVideo.continuation = continuation;
    }

    public static String getTrackingParams() {
        return trackingParams;
    }

    public static void setTrackingParams(String trackingParams) {
        getHomeVideo.trackingParams = trackingParams;
    }

    public static String getVersion() {
        return Version;
    }

    public static void setVersion(String version) {
        Version = version;
    }

    public static String getData() {
        return Data;
    }

    public static void setData(String data) {
        Data = data;
    }

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


    //Getter and Setter


    public static String getJsonStringRes() {
        return JsonStringRes;
    }

    public static void setJsonStringRes(String jsonStringRes) {
        JsonStringRes = jsonStringRes;
    }

    public static boolean isIsThereId() {
        return isThereId;
    }

    public static void setIsThereId(boolean isThereId) {
        getHomeVideo.isThereId = isThereId;
    }

    public static boolean isIsThereDate() {
        return isThereDate;
    }

    public static void setIsThereDate(boolean isThereDate) {
        getHomeVideo.isThereDate = isThereDate;
    }

    public static boolean isIsThereTime() {
        return isThereTime;
    }

    public static void setIsThereTime(boolean isThereTime) {
        getHomeVideo.isThereTime = isThereTime;
    }

    public static String getJsonData() {
        return jsonData;
    }

    public static void setJsonData(String jsonData) {
        getHomeVideo.jsonData = jsonData;
    }

    @Override
    protected String doInBackground(String... strings) {
        System.out.println("I cleared the previous data for you :)");
        videoId.clear();
        thumbnailUrl.clear();
        videoTitle.clear();
        viewsInfo.clear();
        showTime.clear();
        dateInfo.clear();
        channelName.clear();
        channelImgUrl.clear();
        heartIcon.clear();
        DownloadIcon.clear();
        badgeIcon.clear();
        isLive.clear();
        sendRequest("https://www.youtube.com/?pbj=1");
        return null;
    }

    public static void sendRequest(String url) {

        setJsonStringRes(null);
        //send first request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("Connection", "keep-alive")
                .addHeader("authority", "www.youtube.com")
                .addHeader("x-youtube-client-name", "1")
                .addHeader("x-youtube-client-version", "2.20201202.04.00")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Bar Request");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    extractData(response.body().string());
                } catch (Exception e) {
                    System.out.println("I can't extract data from this json string: error in getHomeVideo class, at send Request method");
                }

            }
        });
    }

    public static void extractData(String jsonString) {

        JSONParser Jparser = new JSONParser();
        JSONArray jsonArray = null;

        //System.out.println("json data: "+jsonString);

        if (jsonString != null) {
            try {
                //System.out.println(jsonString);
                jsonArray = (JSONArray) Jparser.parse(jsonString);

                //start extract data from Json
                JSONObject getResponse = (JSONObject) jsonArray.get(1);
                JSONObject Response = (JSONObject) getResponse.get("response");
                //
                JSONObject contents = (JSONObject) Response.get("contents");
                JSONObject twoColumnBrowseResultsRenderer = (JSONObject) contents.get("twoColumnBrowseResultsRenderer");
                //
                JSONArray tabs = (JSONArray) twoColumnBrowseResultsRenderer.get("tabs");
                JSONObject getTabRenderer = (JSONObject) tabs.get(0);
                //
                JSONObject tabRenderer = (JSONObject) getTabRenderer.get("tabRenderer");
                JSONObject content = (JSONObject) tabRenderer.get("content");
                //
                JSONObject richGridRenderer = (JSONObject) content.get("richGridRenderer");
                JSONArray SecSontents = (JSONArray) richGridRenderer.get("contents");

                //trackingParams Key
                //System.out.println("trackingParams Key: "+richGridRenderer.get("trackingParams"));

                //extract video data
                for (int i = 0; i < SecSontents.size(); i++) {


                    //Check if arrive to the last index in the list
                    //the last index doesn't contain video data
                    if (i != SecSontents.size() - 1) {

                        JSONObject getRichItemRenderer = (JSONObject) SecSontents.get(i);

                        if(getRichItemRenderer.get("richItemRenderer") != null) {

                            JSONObject richItemRenderer = (JSONObject) getRichItemRenderer.get("richItemRenderer");
                            JSONObject ThirdContent = (JSONObject) richItemRenderer.get("content");
                            JSONObject videoRenderer = (JSONObject) ThirdContent.get("videoRenderer");

                            //Start getting Video Data
                            //Check if this index contain a video id and not a live before start extracting data
                            if (videoRenderer.get("videoId") != null && videoRenderer.get("lengthText") != null) {
                                //Video id
                                System.out.println("Video id: " + videoRenderer.get("videoId"));
                                videoId.add((String) videoRenderer.get("videoId"));
                                String video_id = (String) videoRenderer.get("videoId");

                                //Thumbnail
                                JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                                JSONArray thumbnails = (JSONArray) thumbnail.get("thumbnails");
                                JSONObject getUrl = (JSONObject) thumbnails.get(0);
                                //JSONObject thumbnailUrl = (JSONObject) getUrl.get("url");
                                System.out.println("Thumbnails from Home Video: " + getUrl.get("url"));
                                thumbnailUrl.add((String) getUrl.get("url"));
                                String video_thumbnail = (String) getUrl.get("url");

                                //Title
                                JSONObject title = (JSONObject) videoRenderer.get("title");
                                JSONArray runs = (JSONArray) title.get("runs");
                                JSONObject text = (JSONObject) runs.get(0);
                                System.out.println("Video Title: " + text.get("text"));
                                videoTitle.add((String) text.get("text"));
                                String video_title = (String) text.get("text");

                                //Channel Name
                                JSONObject longBylineText = (JSONObject) videoRenderer.get("longBylineText");
                                JSONArray longBylineText_runs = (JSONArray) longBylineText.get("runs");
                                JSONObject channel_name = (JSONObject) longBylineText_runs.get(0);
                                System.out.println("Channel name: " + channel_name.get("text"));
                                channelName.add((String) channel_name.get("text"));
                                String by = (String) channel_name.get("text");

                                //Channel Picture
                                JSONObject channelThumbnailSupportedRenderers = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                                JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnailSupportedRenderers.get("channelThumbnailWithLinkRenderer");
                                JSONObject channel_thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                                JSONArray channel_thumbnails = (JSONArray) channel_thumbnail.get("thumbnails");
                                JSONObject pic_url = (JSONObject) channel_thumbnails.get(0);
                                System.out.println("Channel Picture: " + pic_url.get("url"));
                                channelImgUrl.add((String) pic_url.get("url"));
                                String channel_image = (String) pic_url.get("url");

                                //Date
                                JSONObject publishedTimeText = (JSONObject) videoRenderer.get("publishedTimeText");
                                System.out.println("Video Date: " + publishedTimeText.get("simpleText"));
                                dateInfo.add((String) publishedTimeText.get("simpleText"));
                                String date = (String) publishedTimeText.get("simpleText");

                                //Video Time
                                JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                                System.out.println("Video time: " + lengthText.get("simpleText"));
                                showTime.add((String) lengthText.get("simpleText"));
                                String time = (String) lengthText.get("simpleText");

                                //Video Views
                                JSONObject shortViewCountText = (JSONObject) videoRenderer.get("shortViewCountText");
                                System.out.println("Video views: " + shortViewCountText.get("simpleText"));
                                viewsInfo.add((String) shortViewCountText.get("simpleText"));
                                String views = (String) shortViewCountText.get("simpleText");

                                //badge
                                boolean badge;
                                if (videoRenderer.get("ownerBadges") != null) {
                                    System.out.println("Badge: ✔");
                                    badge = true;
                                } else {
                                    System.out.println("Badge: X");
                                    badge = false;
                                }
                                //
                                System.out.println("------------------------------------------------------------------");
                                videoObjectList.add(new VideoObject(video_id, video_title, by, time, date, views, badge, video_thumbnail, channel_image));
                            }
                        }
                    } else {

                        System.out.println("Last index!");
//                        JSONObject getContinuationItemRenderer = (JSONObject) SecSontents.get(i);
//                        JSONObject continuationItemRenderer = (JSONObject) getContinuationItemRenderer.get("continuationItemRenderer");
//                        JSONObject continuationEndpoint = (JSONObject) continuationItemRenderer.get("continuationEndpoint");
//                        JSONObject continuationCommand = (JSONObject) continuationEndpoint.get("continuationCommand");
                        //continuation key
//                        System.out.println("continuation key: " + continuationCommand.get("token"));
//                        setContinuation((String) continuationCommand.get("token"));
//                        //trackingParams Key
//                        System.out.println("trackingParams Key 2: " + continuationEndpoint.get("clickTrackingParams"));
//                        setTrackingParams((String) continuationEndpoint.get("clickTrackingParams"));
                    }


                    /*---------*/
                    //Extract Parameters
                    JSONObject responseContext = (JSONObject) Response.get("responseContext");
                    JSONArray Second_serviceTrackingParams = (JSONArray) responseContext.get("serviceTrackingParams");
                    JSONObject get_params = (JSONObject) Second_serviceTrackingParams.get(2);

                    JSONObject sec_params = (JSONObject) get_params;
                    JSONArray param = (JSONArray) sec_params.get("params");
                    JSONObject client_version = (JSONObject) param.get(2);

                    //Client Version Key
                    System.out.println("Client Version Key: " + client_version.get("value"));
                    setVersion((String) client_version.get("value"));

                    /*----------*/
                    JSONObject webResponseContextExtensionData = (JSONObject) responseContext.get("webResponseContextExtensionData");
                    JSONObject ytConfigData = (JSONObject) webResponseContextExtensionData.get("ytConfigData");

                    //Visitor Data Key
                    System.out.println("Visitor Data Key: " + ytConfigData.get("visitorData"));
                    setData((String) ytConfigData.get("visitorData"));


                }
                testCallBack = new HomeFragment();
                testCallBack.recommended(videoObjectList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void getMoreData(String continuation_key, String
            trackingParams_key, String client_version_key, String visitor_data_key) {
        //String json = "{\"context\":{\"client\":{\"hl\":\"en\",\"gl\":\"MA\",\"visitorData\":\"CgtzUFF3a1ZVbVVSOCi4h7T-BQ%3D%3D\",\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0,gzip(gfe)\",\"clientName\":\"WEB\",\"clientVersion\":\"2.20201202.06.01\",\"osName\":\"Windows\",\"osVersion\":\"10.0\",\"browserName\":\"Firefox\",\"browserVersion\":\"83.0\",\"screenWidthPoints\":1280,\"screenHeightPoints\":391,\"screenPixelDensity\":1,\"screenDensityFloat\":1,\"utcOffsetMinutes\":-480,\"userInterfaceTheme\":\"USER_INTERFACE_THEME_LIGHT\",\"mainAppWebInfo\":{\"graftUrl\":\"https://www.youtube.com/\"},\"timeZone\":\"America/Los_Angeles\"},\"request\":{\"sessionId\":\"6903177893666761359\",\"internalExperimentFlags\":[],\"consistencyTokenJars\":[]},\"adSignalsInfo\":{\"params\":[{\"key\":\"dt\",\"value\":\"1607271359842\"},{\"key\":\"flash\",\"value\":\"0\"},{\"key\":\"frm\",\"value\":\"0\"},{\"key\":\"u_tz\",\"value\":\"-480\"},{\"key\":\"u_his\",\"value\":\"1\"},{\"key\":\"u_java\",\"value\":\"false\"},{\"key\":\"u_h\",\"value\":\"1024\"},{\"key\":\"u_w\",\"value\":\"1280\"},{\"key\":\"u_ah\",\"value\":\"984\"},{\"key\":\"u_aw\",\"value\":\"1280\"},{\"key\":\"u_cd\",\"value\":\"24\"},{\"key\":\"u_nplug\",\"value\":\"0\"},{\"key\":\"u_nmime\",\"value\":\"0\"},{\"key\":\"bc\",\"value\":\"31\"},{\"key\":\"bih\",\"value\":\"391\"},{\"key\":\"biw\",\"value\":\"1263\"},{\"key\":\"brdim\",\"value\":\"-8,-8,-8,-8,1280,0,1296,1000,1280,391\"},{\"key\":\"vis\",\"value\":\"1\"},{\"key\":\"wgl\",\"value\":\"true\"},{\"key\":\"ca_type\",\"value\":\"image\"}],\"bid\":\"ANyPxKr9DV_IjrBa3F1HUbPxmsAnsgCP-EObSpdxEOAt9JEkA_KyirU1cMv-FeSiHKkC0aoCVZPjdwqtSpfOA_pZzGyRHIeoIg\",\"consentBumpParams\":{\"consentHostnameOverride\":\"https://www.youtube.com\",\"urlOverride\":\"\"}},\"user\":{},\"clientScreenNonce\":\"MC42OTg1ODgyOTMyNjM1OTU.\",\"clickTracking\":{\"clickTrackingParams\":\"CBwQ8eIEIhMIhfuHsOC57QIVgkHGCh04vQYJ\"}},\"continuation\":\"4qmFsgL9AxIPRkV3aGF0X3RvX3dhdGNoGq4DQ0JoNnV3SkRhVGhCUVVkV2RVRkJSazVSVVVGQ1ZGVkZRVUZSUWtkU1dHUnZXVmhTWm1SSE9XWmtNa1l3V1RKblFVRlJRVUZCVVVWQ1FVRkJRVUZSUVVKQlFVRkNRVkpwYjE5TGVYYzBURzUwUVdwSlRFTktaVVkwWlRjNExYQmZNRzkzUmt0TFoyOW9RMmc0ZDJWRVdtdGpWemxhVTJ0d1dHUXhjRE5UTUU0MlVWY3hSRlZJVG5sYWJuQjJZbGRzVDFkWWFHcEhUM1UzTjAxaWFreHNjSFpEYlRCTFIxaHNNRmd6UW1oYU1sWm1ZekkxYUdOSVRtOWlNMUptWTIxV2JtRlhPWFZaVjNkVFNIcENORTV0VW5oaU1XeExVMnhrTTFkdVpFeFJNM0JDWWxWT1VXTXpTbTFsYlRsMFlWVTFXbVZIVFdGTWQwRkJXbGMwUVVGVk1VSkJRVVpPVVZGQlFrRkZXa1prTW1ob1pFWTVNR0l4T1ROWldGSnFZVUZCUWtGQlFVSkJVVVZCUVVGQlFrRkJSVUZCUVVWQ0xYQjZTSFpSYTBORFFVRSUzRJICGxoXaHR0cHM6Ly93d3cueW91dHViZS5jb20iAJoCGmJyb3dzZS1mZWVkRkV3aGF0X3RvX3dhdGNo\"}";
        if (true) {
            String json = "{\"context\":{\"client\":{\"hl\":\"en\",\"gl\":\"\",\"visitorData\":\"" + getData() + "\",\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0,gzip(gfe)\",\"clientName\":\"WEB\",\"clientVersion\":\"" + getVersion() + "\",\"osName\":\"\",\"osVersion\":\"\",\"browserName\":\"Firefox\",\"browserVersion\":\"\",\"screenWidthPoints\":1280,\"screenHeightPoints\":391,\"screenPixelDensity\":1,\"screenDensityFloat\":1,\"utcOffsetMinutes\":-480,\"userInterfaceTheme\":\"USER_INTERFACE_THEME_LIGHT\",\"mainAppWebInfo\":{\"graftUrl\":\"https://www.youtube.com/\"},\"timeZone\":\"America/Los_Angeles\"},\"request\":{\"sessionId\":\"\",\"internalExperimentFlags\":[],\"consistencyTokenJars\":[]},\"adSignalsInfo\":{\"params\":[{\"key\":\"dt\",\"value\":\"1607271359842\"},{\"key\":\"flash\",\"value\":\"0\"},{\"key\":\"frm\",\"value\":\"0\"},{\"key\":\"u_tz\",\"value\":\"-480\"},{\"key\":\"u_his\",\"value\":\"1\"},{\"key\":\"u_java\",\"value\":\"false\"},{\"key\":\"u_h\",\"value\":\"1024\"},{\"key\":\"u_w\",\"value\":\"1280\"},{\"key\":\"u_ah\",\"value\":\"984\"},{\"key\":\"u_aw\",\"value\":\"1280\"},{\"key\":\"u_cd\",\"value\":\"24\"},{\"key\":\"u_nplug\",\"value\":\"0\"},{\"key\":\"u_nmime\",\"value\":\"0\"},{\"key\":\"bc\",\"value\":\"31\"},{\"key\":\"bih\",\"value\":\"391\"},{\"key\":\"biw\",\"value\":\"1263\"},{\"key\":\"brdim\",\"value\":\"-8,-8,-8,-8,1280,0,1296,1000,1280,391\"},{\"key\":\"vis\",\"value\":\"1\"},{\"key\":\"wgl\",\"value\":\"true\"},{\"key\":\"ca_type\",\"value\":\"image\"}],\"bid\":\"\",\"consentBumpParams\":{\"consentHostnameOverride\":\"https://www.youtube.com\",\"urlOverride\":\"\"}},\"user\":{},\"clientScreenNonce\":\"\",\"clickTracking\":{\"clickTrackingParams\":\"" + getTrackingParams() + "\"}},\"continuation\":\"" + getContinuation() + "\"}";
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url("https://www.youtube.com/youtubei/v1/browse?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
                    .header("Host", "www.youtube.com")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
                    .header("Accept", "*/*")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Referer", "https://www.youtube.com/")
                    .header("Content-Type", "application/json")
                    //.header("X-Goog-Visitor-Id","CgtzUFF3a1ZVbVVSOCi4h7T-BQ%3D%3D")
                    .header("X-Youtube-Client-Name", "1")
                    .header("X-Youtube-Client-Version", getVersion())
                    .header("Origin", "https://www.youtube.com")
                    //.header("Content-Length","2389")
                    .header("DNT", "1")
                    .header("Connection", "keep-alive")
                    .header("Cookie", "GPS=\"1\"; VISITOR_INFO1_LIVE=\"sPQwkVUmUR8\"; YSC=\"nIEJQK1yKRU\"")

                    .method("POST", body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Bad request!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //System.out.println(response.body().string());
                    try {
                        //setJsonData(response.body().string());
                        extraxtMoreData(response.body().string());
                    } catch (Exception e) {
                        System.out.println("I can't pass it");

                    }
                }
            });
        } else {
            System.out.println("error in loadMoreData!");
        }
    }

    public static void extraxtMoreData(String json) {

        setContinuation(null);
        setTrackingParams(null);

        JSONParser parser = new JSONParser();
        JSONObject myJson = null;
        try {
            myJson = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error in convert json String!");
        }

        JSONArray onResponseReceivedActions = (JSONArray) myJson.get("onResponseReceivedActions");
        JSONObject get_appendContinuationItemsAction = (JSONObject) onResponseReceivedActions.get(0);
        JSONObject appendContinuationItemsAction = (JSONObject) get_appendContinuationItemsAction.get("appendContinuationItemsAction");
        JSONArray continuationItems = (JSONArray) appendContinuationItemsAction.get("continuationItems");

        for (int i = 0; i < continuationItems.size(); i++) {

            try {
                //Check if arrive to the last index in the list
                //the last index doesn't contain video data
                if (i != continuationItems.size() - 1) {

                    JSONObject getRichItemRenderer = (JSONObject) continuationItems.get(i);
                    JSONObject richItemRenderer = (JSONObject) getRichItemRenderer.get("richItemRenderer");

                    JSONObject ThirdContent = (JSONObject) richItemRenderer.get("content");
                    JSONObject videoRenderer = (JSONObject) ThirdContent.get("videoRenderer");

                    //Start getting Video Data
                    //Video id
                    try {
                        System.out.println("Video id: " + videoRenderer.get("videoId"));
                        videoId.add((String) videoRenderer.get("videoId"));
                        isThereId = true;
                    } catch (Exception e) {
                        System.out.println("There's no id");
                        isThereId = false;

                        //Check if this index contain on video id before start extracting data
                    }
                    if (isThereId != false) {

                        //Thumbnail
                        try {
                            JSONObject thumbnail = (JSONObject) videoRenderer.get("thumbnail");
                            JSONArray thumbnails = (JSONArray) thumbnail.get("thumbnails");
                            JSONObject getUrl = (JSONObject) thumbnails.get(0);
                            //JSONObject thumbnailUrl = (JSONObject) getUrl.get("url");
                            System.out.println("Thumbnails: " + getUrl.get("url"));
                            thumbnailUrl.add((String) getUrl.get("url"));
                        } catch (Exception e) {
                            System.out.println("No Thumbnail!");
                            thumbnailUrl.add("");
                        }
                        //Title
                        try {
                            JSONObject title = (JSONObject) videoRenderer.get("title");
                            JSONArray runs = (JSONArray) title.get("runs");
                            JSONObject text = (JSONObject) runs.get(0);
                            System.out.println("Video Title: " + text.get("text"));
                            videoTitle.add((String) text.get("text"));
                        } catch (Exception e) {
                            System.out.println("No Title!");
                            videoTitle.add("");
                        }
                        //Channel Name
                        try {
                            JSONObject longBylineText = (JSONObject) videoRenderer.get("longBylineText");
                            JSONArray runs = (JSONArray) longBylineText.get("runs");
                            JSONObject channel_name = (JSONObject) runs.get(0);
                            System.out.println("Channel name: " + channel_name.get("text"));
                            channelName.add((String) channel_name.get("text"));
                        } catch (Exception e) {
                            System.out.println("No channel name!");
                            channelName.add("");
                        }
                        //Channel Picture
                        try {
                            JSONObject channelThumbnailSupportedRenderers = (JSONObject) videoRenderer.get("channelThumbnailSupportedRenderers");
                            JSONObject channelThumbnailWithLinkRenderer = (JSONObject) channelThumbnailSupportedRenderers.get("channelThumbnailWithLinkRenderer");
                            JSONObject channel_thumbnail = (JSONObject) channelThumbnailWithLinkRenderer.get("thumbnail");
                            JSONArray channel_thumbnails = (JSONArray) channel_thumbnail.get("thumbnails");
                            JSONObject pic_url = (JSONObject) channel_thumbnails.get(0);
                            System.out.println("Channel Picture: " + pic_url.get("url"));
                            channelImgUrl.add((String) pic_url.get("url"));
                        } catch (Exception e) {
                            System.out.println("No Channel Picture!");
                            channelImgUrl.add("");
                        }
                        //Date
                        try {
                            JSONObject publishedTimeText = (JSONObject) videoRenderer.get("publishedTimeText");
                            System.out.println("Video Date: " + publishedTimeText.get("simpleText"));
                            dateInfo.add((String) publishedTimeText.get("simpleText"));
                            isThereDate = true;
                        } catch (Exception e) {
                            System.out.println("No Video Date!");
                            dateInfo.add("");
                            isThereDate = false;
                        }
                        //Video Time
                        try {
                            JSONObject lengthText = (JSONObject) videoRenderer.get("lengthText");
                            System.out.println("Video time: " + lengthText.get("simpleText"));
                            showTime.add((String) lengthText.get("simpleText"));
                            isThereTime = true;
                        } catch (Exception e) {
                            System.out.println("No Video time!");
                            showTime.add("");
                            isThereTime = false;
                        }
                        if (isThereTime != false & isThereDate != false) {
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
                        } else {
                            System.out.println("is Live Now ⬤");
                            isLive.add(true);
                            viewsInfo.add("");
                        }
                        //badge
                        try {
                            if (videoRenderer.get("ownerBadges") != null) {
                                System.out.println("Badge: ✔");
                                badgeIcon.add(R.drawable.ic_badge);
                            } else {
                                System.out.println("Badge: X");
                                badgeIcon.add(0);
                            }
                        } catch (Exception e) {
                            System.out.println("No Badge");
                            badgeIcon.add(0);
                        }
                        //
                        DownloadIcon.add(R.drawable.ic_download);
                        heartIcon.add(R.drawable.ic_save_for_later);
                        System.out.println("------------------------------------------------------------------");


                    } else {
                        System.out.println("I prefer to don't extract " + i + " index data cause it doesn't contain Video id!");
                    }

                } else {

                    System.out.println("Last index!");
                    try {
                        JSONObject getContinuationItemRenderer = (JSONObject) continuationItems.get(i);
                        JSONObject continuationItemRenderer = (JSONObject) getContinuationItemRenderer.get("continuationItemRenderer");
                        JSONObject continuationEndpoint = (JSONObject) continuationItemRenderer.get("continuationEndpoint");
                        JSONObject continuationCommand = (JSONObject) continuationEndpoint.get("continuationCommand");
                        //continuation key
                        System.out.println("continuation key: " + continuationCommand.get("token"));
                        setContinuation((String) continuationCommand.get("token"));
                        //trackingParams Key
                        System.out.println("trackingParams Key 2: " + continuationEndpoint.get("clickTrackingParams"));
                        setTrackingParams((String) continuationEndpoint.get("clickTrackingParams"));
                    } catch (Exception e) {
                        System.out.println("No more!");
                    }
                }
            } catch (Exception e) {
                System.out.println("I get Some troubles in extracting video data!");
            }
        }
    }

    public static void loadMore() {
        if (getContinuation() != null & getTrackingParams() != null &
                getVersion() != null & getData() != null) {
            getMoreData(getContinuation(), getTrackingParams(), getVersion(), getData());
        } else {
            System.out.println("error in getMoreData!");
        }
    }
}
