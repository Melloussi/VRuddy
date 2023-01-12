package com.app.vruddy.Models.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.app.vruddy.Models.Interfaces.RelatedResultCallBack;
import com.app.vruddy.Models.Objects.VideoObject;
import com.app.vruddy.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getRelatedVideoData extends AsyncTask<String, Integer, String> {
    private static JSONObject jsonObjectt;
    private static boolean isThereId;
    private static boolean isThereDate;
    private static boolean isThereTime;
    private static int index = 2;
    private static boolean badge;
    private Pattern scriptPattern;
    private Matcher scriptMatcher;
    private static Connection.Response doc;
    private String result;
    private boolean match = false;

    //new param-----------------------------------------------
    private static String Continuation;
    private static String Itct;
    private static String ClientVersionId;
    private static String jsonString = null;
    static String MoreVideosJsonString;
    private static List<VideoObject> videoObjectsList = new ArrayList<>();
    private static RelatedResultCallBack relatedResultCallBack;

    public getRelatedVideoData(RelatedResultCallBack localRelatedResultCallBack) {
        relatedResultCallBack = localRelatedResultCallBack;
    }

    //Getter and Setter
    public static String getMoreVideosJsonString() {
        return MoreVideosJsonString;
    }

    public static void setMoreVideosJsonString(String moreVideosJsonString) {
        MoreVideosJsonString = moreVideosJsonString;
    }

    public static String getContinuation() {
        return Continuation;
    }

    public static void setContinuation(String continuation) {
        Continuation = continuation;
    }

    public static String getItct() {
        return Itct;
    }

    public static void setItct(String itct) {
        Itct = itct;
    }

    public static String getClientVersionId() {
        return ClientVersionId;
    }

    public static void setClientVersionId(String clientVersionId) {
        ClientVersionId = clientVersionId;
    }
    //--------------------------------------------------------

    private static  ArrayList<String>videoId = new ArrayList<>();
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

    //arrayList getters ----------------------------------------------

    public ArrayList<Boolean> getIsLive() {
        return isLive;
    }

    public ArrayList<String> getVideoId() {
        return videoId;
    }

    public ArrayList<String> getThumbnailUrl() {
        return thumbnailUrl;
    }

    public ArrayList<String> getVideoTitle() {
        return videoTitle;
    }

    public ArrayList<String> getViewsInfo() {
        return viewsInfo;
    }

    public ArrayList<String> getShowTime() {
        return showTime;
    }

    public ArrayList<String> getDateInfo() {
        return dateInfo;
    }

    public ArrayList<String> getChannelName() {
        return channelName;
    }

    public ArrayList<String> getChannelImgUrl() {
        return channelImgUrl;
    }

    public ArrayList<Integer> getHeartIcon() {
        return heartIcon;
    }

    public ArrayList<Integer> getDownloadIcon() {
        return DownloadIcon;
    }

    public ArrayList<Integer> getBadgeIcon() {
        return badgeIcon;
    }


    @Override
    protected String doInBackground(String... strings) {
        //Jsoup Part -----------------------------------------------------------
        try {
            System.out.println("Url plus: "+strings[0]);
            doc = Jsoup.connect(strings[0])
                    //.userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .method(Connection.Method.GET)
                    .execute();
        } catch (IOException e) {
            System.out.println("error in: url part");
            e.printStackTrace();
        }

        //REGX Part --------------------------------------------------------
        jsonString = regex();
        while (jsonString == null){
            jsonString = regex();
        }
        getVideoDataInfo(jsonString);
        getClientVersion();


        return null;
    }
    public static String regex(){
        //in this REGX part I extract JSON data from youtube url source code and pass it into string
        System.out.println("Hello from regex!");
        String resultt = null;
        Pattern scriptPatternn;
        Matcher scriptMatcherr;
        String scriptStringPatternn;

        scriptStringPatternn = "var\\sytInitialData\\s=\\s\\{\"(.*?)\\}\\};";
        scriptPatternn = Pattern.compile(scriptStringPatternn);
        scriptMatcherr = scriptPatternn.matcher(doc.body());
        //System.out.println(doc.html());
        //boolean flag = scriptMatcherr.find();

        if(scriptMatcherr.find() != true){
            scriptStringPatternn = "window\\[\"ytInitialData\"\\](.*?)\"\\}\\};";
            scriptPatternn = Pattern.compile(scriptStringPatternn);
            scriptMatcherr = scriptPatternn.matcher(doc.body());
            //----------------
            if(scriptMatcherr.find()) {
                resultt = scriptMatcherr.group(0);
                resultt = resultt.replace("window[\"ytInitialData\"] = ", "");
                resultt = resultt.replace(";", "");
            }else {
                System.out.println("There's No match in Related Video Regex Pattern!");
            }
        }else {
            resultt = scriptMatcherr.group(0);
            resultt = resultt.replace("var ytInitialData = ", "");
            resultt = resultt.replace(";", "");
        }



        return resultt;
    }
    /*This method for convert a json string to a json object and extract related video data and store it in an ArrayList*/
    public static void getVideoDataInfo(String jsonString){

        //convert JSON string to JSON Object
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
//                System.out.println("my flag");
//                System.out.println("-------------------------------------------------------------");
//                System.out.println("-------------------------------------------------------------");
//                System.out.println("-------------------------------------------------------------");
//                //System.out.println(result);
//                Log.e(TAG, result);
//                System.out.println("-------------------------------------------------------------");
//                System.out.println("-------------------------------------------------------------");
//                System.out.println("-------------------------------------------------------------");
        } catch (ParseException e) {
            System.out.println("error in: convert JSON string to JSON Object");
            e.printStackTrace();
        }

//
//read JSON from a file-----------------------------------------
//        JSONParser parser = new JSONParser();
//        Object obj = parser.parse(new FileReader("a.json"));
//        JSONObject jsonObject = (JSONObject) obj;
//--------------------------------------------------------------


        //get data fom JSON
        //First path to go inside videos content data------------------------------------------
        JSONObject contents = (JSONObject) json.get("contents");
        JSONObject twoColumnWatchNextResults = (JSONObject) contents.get("twoColumnWatchNextResults");
        JSONObject secondaryResults = (JSONObject) twoColumnWatchNextResults.get("secondaryResults");
        JSONObject secondaryResults2 = (JSONObject)secondaryResults.get("secondaryResults");

//        secondaryResults2.get("trackingParams");
        //get continuation key parameters
        JSONArray continuationsArray = (JSONArray) secondaryResults2.get("continuations");
        if(continuationsArray != null){

            JSONObject continuations = (JSONObject) continuationsArray.get(0);
            JSONObject nextContinuationData = (JSONObject) continuations.get("nextContinuationData");
            //nextContinuationData.get("continuation");
            System.out.println("--------------------------------------------------");
            //continuation key
            //System.out.println("continuation key:");
            //System.out.println(nextContinuationData.get("continuation"));
            setContinuation((String) nextContinuationData.get("continuation"));
            //itct Key
            //System.out.println("itct Key: "+ secondaryResults2.get("trackingParams"));
            setItct((String) secondaryResults2.get("trackingParams"));
            System.out.println("--------------------------------------------------\n");
        }
//        JSONObject continuations = (JSONObject) continuationsArray.get(0);
//        JSONObject nextContinuationData = (JSONObject) continuations.get("nextContinuationData");
//        //nextContinuationData.get("continuation");
//        System.out.println("--------------------------------------------------");
//        //continuation key
//        //System.out.println("continuation key:");
//        //System.out.println(nextContinuationData.get("continuation"));
//        setContinuation((String) nextContinuationData.get("continuation"));
//        //itct Key
//        //System.out.println("itct Key: "+ secondaryResults2.get("trackingParams"));
//        setItct((String) secondaryResults2.get("trackingParams"));
//        System.out.println("--------------------------------------------------\n");

        //Array that have videos data info
        JSONArray resultsArray = (JSONArray) secondaryResults2.get("results");

        //this part for checking if the videos data start with 1 or 2 in JSON data array
        //and this test is for use the right index in the next loop to start extract the data
        JSONObject AgetCompactAutoplayRenderer = (JSONObject) resultsArray.get(1);
        JSONObject AcompactAutoplayRenderer = (JSONObject)AgetCompactAutoplayRenderer.get("compactVideoRenderer");

        //this check gives an idea to for loop for which index should start!
        if(AcompactAutoplayRenderer == null){
            index = 2;
        }else {
            index = 1;
        }

        //result array contain all data that I need: videos titles, videos time, etc...
        //I Loop inside result array to get each video's data
        for(int indexx=2; index < resultsArray.size(); index++){
            String video_id             = "";
            String video_title          = "";
            String video_by             = "";
            String video_time           = "";
            String video_date           = "";
            String video_views          = "";
            String video_thumbnail      = "";
            String video_channelImg     = "";
            boolean video_badge         = false;

            JSONObject getCompactAutoplayRenderer = (JSONObject) resultsArray.get(index);
            JSONObject compactAutoplayRenderer = (JSONObject)getCompactAutoplayRenderer.get("compactVideoRenderer");
            //compactRadioRenderer
            //compactVideoRenderer

            try{
                //video id
                System.out.println(compactAutoplayRenderer.get("videoId"));
                videoId.add((String) compactAutoplayRenderer.get("videoId"));
                video_id = (String) compactAutoplayRenderer.get("videoId");

                //videoId.add((String) compactAutoplayRenderer.get("videoId"));
                isThereId = true;
            }catch (Exception exception){
                System.out.println("error: video id doesn't found!");
                isThereId = false;
            }
            if(isThereId == true) {
                try {
                    //video title
                    JSONObject title = (JSONObject) compactAutoplayRenderer.get("title");
                    System.out.println(title.get("simpleText"));
                    videoTitle.add((String) title.get("simpleText"));
                    video_title = (String) title.get("simpleText");
                    //videoTitle.add((String) title.get("simpleText"));
                } catch (Exception exception) {
                    System.out.println("error: video title doesn't found!");
                    videoTitle.add("");
                }

                try {
                    //channel name
                    JSONObject longBylineText = (JSONObject) compactAutoplayRenderer.get("longBylineText");
                    JSONArray getRunsArray = (JSONArray) longBylineText.get("runs");
                    JSONObject getText = (JSONObject) getRunsArray.get(0);
                    System.out.println(getText.get("text"));
                    channelName.add((String) getText.get("text"));
                    video_by = (String) getText.get("text");
                } catch (Exception exception) {
                    System.out.println("error: channel name doesn't found!");
                    channelName.add("");
                }

                try {
                    //badge
                    //System.out.println("badge: " + compactAutoplayRenderer.get("ownerBadges"));
                    if(compactAutoplayRenderer.get("ownerBadges") != null){
                        System.out.println("badge: ✔");
                        badgeIcon.add(R.drawable.ic_badge);
                        video_badge = true;
                        //badgeIcon.add(R.drawable.ic_badge);
                    }else {
                        System.out.println("badge: no");
                        badgeIcon.add(0);
                        video_badge = false;
                        //badgeIcon.add(0);
                    }
                }catch (Exception e){
                    //System.out.println("is not official account");
//                    System.out.println("badge: no");
//                    badgeIcon.add(0);
                }

                try {
                    //video date
                    JSONObject publishedTimeText = (JSONObject) compactAutoplayRenderer.get("publishedTimeText");
                    System.out.println(publishedTimeText.get("simpleText"));
                    dateInfo.add((String) publishedTimeText.get("simpleText"));
                    isThereDate = true;
                    video_date = (String) publishedTimeText.get("simpleText");
                } catch (Exception exception) {
                    //System.out.println("error: video date found!");
                    dateInfo.add("");
                    isThereDate = false;
                }

                try {
                    //video time
                    JSONObject lengthText = (JSONObject) compactAutoplayRenderer.get("lengthText");
                    System.out.println(lengthText.get("simpleText"));
                    showTime.add((String) lengthText.get("simpleText"));
                    isThereTime = true;
                    video_time = (String) lengthText.get("simpleText");
                } catch (Exception exception) {
                    //System.out.println("error: video time doesn't found!");
                    showTime.add("");
                    isThereTime = false;
                }
                try {
                    //video views
                    JSONObject viewCountText = (JSONObject) compactAutoplayRenderer.get("shortViewCountText");
                    System.out.println(viewCountText.get("simpleText"));
                    viewsInfo.add((String) viewCountText.get("simpleText"));
                    video_views = (String) viewCountText.get("simpleText");
                } catch (Exception exception) {
                    System.out.println("error: Views doesn't found!");
                    viewsInfo.add("");
                }
                //this check helps to know if this video is a live video or not
                if(isThereDate == true && isThereTime == true) {
                    isLive.add(false);
                }else {
                    System.out.println("is live now");
                    isLive.add(true);
                }

                try {
                    //channel thumbnail
                    JSONObject channelThumbnail = (JSONObject) compactAutoplayRenderer.get("channelThumbnail");
                    JSONArray thumbnails = (JSONArray) channelThumbnail.get("thumbnails");
                    JSONObject getChannelPicUrl = (JSONObject) thumbnails.get(0);
                    System.out.println(getChannelPicUrl.get("url"));
                    channelImgUrl.add((String) getChannelPicUrl.get("url"));
                    video_channelImg = (String) getChannelPicUrl.get("url");
                } catch (Exception exception) {
                    System.out.println("error: channel thumbnail doesn't found!");
                    channelImgUrl.add("");
                }

                try {
                    //video thumbnail
                    JSONObject thumbnail = (JSONObject) compactAutoplayRenderer.get("thumbnail");
                    JSONArray getThumbnails = (JSONArray) thumbnail.get("thumbnails");
                    JSONObject getVideoPicUrl = (JSONObject) getThumbnails.get(0);
                    System.out.println(getVideoPicUrl.get("url"));
                    thumbnailUrl.add((String) getVideoPicUrl.get("url"));
                    video_thumbnail = (String) getVideoPicUrl.get("url");
                } catch (Exception exception) {
                    System.out.println("error: video thumbnail doesn't found!");
                    thumbnailUrl.add("");
                }

                //add icons
                heartIcon.add(R.drawable.ic_save_for_later);
                DownloadIcon.add(R.drawable.ic_download);

            }


            /*I try to get last index in "resultsArray" cause sometimes
             "itct parameter" & "continuation parameter" exists in the last index */
            if(index == resultsArray.size()-1) {
                /*I'm trying to extract "itct parameter" & "continuation parameter" from the last index of "resultsArray"
                 * this is the second try to extract these parameters the first try is above but, if it failed that's mean these
                 * parameters exists in last index, so  I check if the first try success or not*/
                if (continuationsArray == null) {
                    System.out.println("#############################################################################");

                    JSONObject getContinuationItemRenderer = (JSONObject) resultsArray.get(index);
                    JSONObject continuationItemRenderer = (JSONObject) getContinuationItemRenderer.get("continuationItemRenderer");
                    JSONObject continuationEndpoint = (JSONObject) continuationItemRenderer.get("continuationEndpoint");

                    //set itct parameter
                    System.out.println("Set itct: "+ continuationEndpoint.get("clickTrackingParams"));
                    setItct((String) continuationEndpoint.get("clickTrackingParams"));

                    //set continuation
                    JSONObject continuationCommand = (JSONObject) continuationEndpoint.get("continuationCommand");
                    System.out.println("Set con: "+ continuationCommand.get("token"));
                    setContinuation((String) continuationCommand.get("token"));
                }
            }
            System.out.println("--------------------------------------------------------");
            if(!video_id.isEmpty()) {
                videoObjectsList.add(new VideoObject(video_id, video_title, video_by, video_time, video_date, video_views, video_badge, video_thumbnail, video_channelImg));
            }
        }
        relatedResultCallBack.result(videoObjectsList);
    }
    /*this method for getting Client Version Id,
     * it's contain a regex pattern to extract a json data from html, this json data contain Client Version Id and some Other Parameters */
    public static void getClientVersion(){
        boolean flag = false;
        String result = null;

        /*in this REGX part I try to extract json data from html
         * json data that contain Client Version Id*/
        String scriptStringPattern = "\\{window.ytplayer\\s=\\s\\{\\};ytcfg.set\\((.*?)\\);";
        Pattern scriptPattern = Pattern.compile(scriptStringPattern);
        Matcher scriptMatcher = scriptPattern.matcher(doc.body());
        /*the source code of youtube sometimes change
        so, I check which regex pattern is work currently*/
        if(scriptMatcher.find() != true){
            scriptStringPattern = "\\(function.\\)\\s\\{window.ytplayer=.\\};\\n(.*?)\\}\\);var";
            scriptPattern = Pattern.compile(scriptStringPattern);
            scriptMatcher = scriptPattern.matcher(doc.body());

            if(scriptMatcher.find() == true) {
                result = scriptMatcher.group(0);
                result = result.replace("(function() {window.ytplayer={};", "");
                result = result.replace("ytcfg.set(", "");
                result = result.replace(");var", "");
                //System.out.println(result);
                flag = true;
            }else {
                System.out.println("No Matches Found in GetClientVersion Method!");
            }
        }else {
            result = scriptMatcher.group(0);
            result = result.replace("{window.ytplayer = {};ytcfg.set(", "");
            result = result.replace(");", "");
            //System.out.println(result);
            flag = true;
        }

        //I create a flag to tell me if my regex pattern works fine or note to see if I should convert json string to Json Object or not
        /*NOTE: in the future the Youtube may do some changes on its source code
         and The regex pattern that I made will not work anymore, and I should modify the regex pattern to fit the new Youtube's source code*/
        if(flag){
            //JSON-------------------------
            /*
             * in this part of JSON I convert result string to a JSON Object and after that
             * I extract some important parameters to use it in header request to get more data*/

            //convert JSON string to JSON Object
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(result);
            } catch (ParseException e) {
                System.out.println("error in: convert JSON string to JSON Object");
                e.printStackTrace();
            }

            //client version
            if(json.get("INNERTUBE_CLIENT_VERSION") != null) {
                setClientVersionId((String) json.get("INNERTUBE_CLIENT_VERSION"));
                System.out.println("id: " + json.get("INNERTUBE_CLIENT_VERSION"));
            }else {
                //
                setClientVersionId((String) json.get("INNERTUBE_CONTEXT_CLIENT_VERSION"));
                //System.out.println("id: " + json.get("INNERTUBE_CONTEXT_CLIENT_VERSION"));
            }
        }
    }
    /*This Method for calling Youtube server to get more related video data,
     Youtube server response with Json*/
    public static void LoadMoreData(String Client_Version_Id, String Continuation_key, String Itct_key){

        setMoreVideosJsonString(null);

        System.out.println("Client_Version_Id: "+ Client_Version_Id);
        System.out.println("Continuation_key: "+ Continuation_key);
        System.out.println("Itct_key: "+ Itct_key);

        if(Continuation_key != null & Itct_key != null & Client_Version_Id != null){
            String UrlAjax = "https://www.youtube.com/related_ajax?ctoken="+Continuation_key+"&continuation="+Continuation_key+"&itct="+Itct_key;
            Log.d("urlRequest ******[",UrlAjax);

            System.out.println(UrlAjax);
            String myCookies = String.valueOf(doc.cookies());
            myCookies = myCookies.replace("{","");
            myCookies = myCookies.replace("}","");

            //OkHttp Part--------------------------------------------

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlAjax)
                    .addHeader("Host","www.youtube.com")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
                    .addHeader("Content-Type","text/html")
                    .addHeader("charset","UTF-8")
                    .addHeader("Accept","*/*")
                    .addHeader("Accept-Language","en-US,en;q=0.5")
                    //.addHeader("Accept-Encoding","gzip, deflate, br")
                    .addHeader("X-YouTube-Client-Name","1")
                    .addHeader("X-YouTube-Client-Version",Client_Version_Id)
                    .addHeader("X-YouTube-Time-Zone","America/Los_Angeles")
                    .addHeader("DNT","1")
                    .addHeader("Connection","keep-alive")
                    .addHeader("Cookie", myCookies)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Bad request!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.isSuccessful()){
                        //System.out.println("Good one!");
                        setMoreVideosJsonString(response.body().string());
                    }
                }
            });
        }else {
            System.out.println("Bad input in LoadMoreData, Some of method input passed null");
        }



    }
    public static void ExtractMoreData(String jsonString){


        JSONParser jsonParser = new JSONParser();
        JSONArray json= null;
        boolean isThereTime= false;
        boolean isThereDate= false;
        JSONArray results = null;
        JSONArray continuations = null;

        //Convert String To JsonObject
        try {
            json = (JSONArray) jsonParser.parse(jsonString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            JSONObject getResponse = (JSONObject) json.get(1);
            JSONObject response = (JSONObject) getResponse.get("response");
            JSONArray continuationContents = (JSONArray) response.get("onResponseReceivedEndpoints");
            JSONObject get_appendContinuationItemsAction = (JSONObject) continuationContents.get(0);
            JSONObject appendContinuationItemsAction = (JSONObject) get_appendContinuationItemsAction.get("appendContinuationItemsAction");
            results = (JSONArray) appendContinuationItemsAction.get("continuationItems");

            System.out.println("--------> "+results);
        }catch (Exception e){
            System.out.println("Error in convert json String");
            e.printStackTrace();
        }

        if(results != null) {
            for (int i = 0; i < results.size(); i++) {

                JSONObject getCompactVideoRenderer = (JSONObject) results.get(i);
                JSONObject compactVideoRenderer = (JSONObject) getCompactVideoRenderer.get("compactVideoRenderer");


                try {
                    //Video id
                    if(compactVideoRenderer.get("videoId") != null){
                        System.out.println("Id: " + compactVideoRenderer.get("videoId"));
                        videoId.add((String) compactVideoRenderer.get("videoId"));

                        //Thumbnail
                        try {
                            JSONObject thumbnail = (JSONObject) compactVideoRenderer.get("thumbnail");
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
                            JSONObject title = (JSONObject) compactVideoRenderer.get("title");
                            System.out.println("Title: " + title.get("simpleText"));
                            videoTitle.add((String) title.get("simpleText"));
                        } catch (Exception e) {
                            System.out.println("Error in Title");
                            videoTitle.add("");
                        }

                        //Video Date
                        try {
                            JSONObject date = (JSONObject) compactVideoRenderer.get("publishedTimeText");
                            System.out.println("Date: " + date.get("simpleText"));
                            dateInfo.add((String) date.get("simpleText"));
                            isThereDate = true;
                        } catch (Exception e) {
                            isThereDate = false;
                            dateInfo.add("");
                        }

                        //Video Time
                        try {
                            JSONObject lengthText = (JSONObject) compactVideoRenderer.get("lengthText");
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
                                JSONObject shortViewCountText = (JSONObject) compactVideoRenderer.get("shortViewCountText");
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
                        }

                        //Channel Name
                        try {
                            JSONObject shortBylineText = (JSONObject) compactVideoRenderer.get("shortBylineText");
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
                            JSONObject channelThumbnail = (JSONObject) compactVideoRenderer.get("channelThumbnail");
                            JSONArray getChannelThumbnails = (JSONArray) channelThumbnail.get("thumbnails");
                            JSONObject Url = (JSONObject) getChannelThumbnails.get(0);
                            System.out.println("Channel Thumbnail: " + Url.get("url"));
                            channelImgUrl.add((String) Url.get("url"));
                        } catch (Exception e) {
                            System.out.println("Error in Channel Thumbnail");
                            channelImgUrl.add("");
                        }

                        //Badge
                        if (compactVideoRenderer.get("ownerBadges") != null) {
                            System.out.println("Badge: ✔");
                            badgeIcon.add(R.drawable.ic_badge);
                        } else {
                            System.out.println("Badge: ✘");
                            badgeIcon.add(0);
                    }
                        heartIcon.add(R.drawable.ic_save_for_later);
                        DownloadIcon.add(R.drawable.ic_download);

                    }else {
                        System.out.println("No ID");
                    }
                } catch (Exception e) {
                            System.out.println("No ID");
                }

                System.out.println("----------------------------------------------");

            }
        }else {
            System.out.println("Error: I can't extract video data cause results JSONArray is null!");
        }

        //continuation key parameter
        try {
            JSONObject getNextContinuationData = (JSONObject) continuations.get(0);
            JSONObject nextContinuationData = (JSONObject) getNextContinuationData.get("nextContinuationData");
            System.out.println("Co Key From ExtractMoreData: " + nextContinuationData.get("continuation"));
            setContinuation((String) nextContinuationData.get("continuation"));

            //clickTrackingParams key parameter
            System.out.println("itct Key From ExtractMoreData: "+nextContinuationData.get("clickTrackingParams"));
            setItct((String) nextContinuationData.get("clickTrackingParams"));
        }catch (Exception e){
            System.out.println("Error to handel!");
            System.out.println("the end of scroll");
            e.printStackTrace();
        }

        // System.out.println(watchNextSecondaryResultsContinuation.get("results"));
    }
    public static void moreData(){
        System.out.println("moreData");
        LoadMoreData(getClientVersionId(), getContinuation(), getItct());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("I'm still checking");
                if (getMoreVideosJsonString() != null) {
                    System.out.println("I get data");
                    ExtractMoreData(getMoreVideosJsonString());
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }
}
