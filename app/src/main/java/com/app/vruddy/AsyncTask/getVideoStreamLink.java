package com.app.vruddy.AsyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.vruddy.InsertDataToSharedPreferences;
import com.app.vruddy.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Objects.VideoStreamObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getVideoStreamLink extends AsyncTask<String, Integer, String> {

    private static String data;
    private static String result;
    private static String streamLink;
    private static String youtubeVideoUrl;
    private static String youtubeVideoId;
    private static Context context;
    private static FetchDownloadingLinksCallback fetchDownloadingLinksCallback;
    private static boolean Flag = false;
    private static boolean flag = true;
    private static boolean isStreamLinksReady = false;


//    private static ArrayList<String> mimeType = new ArrayList<>();
//    private static ArrayList<Long> contentLength = new ArrayList<>();
//    private static ArrayList<String> quality = new ArrayList<>();
//    private static ArrayList<String> qualityLabel = new ArrayList<>();
//    private static ArrayList<Long> bitrate = new ArrayList<>();
//    private static ArrayList<String> downloadLink = new ArrayList<>();
    //contentLength

    private static List<VideoStreamObject> videoStreamObjectList = new ArrayList<>();
    
    @SuppressLint("StaticFieldLeak")
    private static InsertDataToSharedPreferences shareP = new InsertDataToSharedPreferences();

    //getter & setter


    public static boolean isStreamLinksReady() {
        return isStreamLinksReady;
    }

    public static void setIsStreamLinksReady(boolean isStreamLinksReady) {
        getVideoStreamLink.isStreamLinksReady = isStreamLinksReady;
    }

//    public static ArrayList<Long> getContentLength() {
//        return contentLength;
//    }
//
//    public static ArrayList<String> getMimeType() {
//        return mimeType;
//    }
//
//    public static ArrayList<String> getQuality() {
//        return quality;
//    }
//
//    public static ArrayList<String> getQualityLabel() {
//        return qualityLabel;
//    }
//
//    public static ArrayList<Long> getBitrate() {
//        return bitrate;
//    }
//
//    public static ArrayList<String> getDownloadLink() {
//        return downloadLink;
//    }

    public static String getYoutubeVideoUrl() {
        return youtubeVideoUrl;
    }

    public static void setYoutubeVideoUrl(String youtubeVideoUrl) {
        getVideoStreamLink.youtubeVideoUrl = youtubeVideoUrl;
    }

    public static String getData() {
        return data;
    }

    public static void setData(String data) {
        getVideoStreamLink.data = data;
    }

    public static String getStreamLink() {
        return streamLink;
    }

    public static void setStreamLink(String streamLink) {
        getVideoStreamLink.streamLink = streamLink;
    }

    public static String getResult() {
        return result;
    }

    public static void setResult(String result) {
        getVideoStreamLink.result = result;
    }

    public getVideoStreamLink(Context context, FetchDownloadingLinksCallback fetchDownloadingLinksCallback) {
        this.context = context;
        this.fetchDownloadingLinksCallback = fetchDownloadingLinksCallback;
    }

    //-------------------------------------------------------------------------------

    @Override
    protected String doInBackground(String... strings) {

//        mimeType.clear();
//        contentLength.clear();
//        quality.clear();
//        qualityLabel.clear();
//        bitrate.clear();
//        downloadLink.clear();

        System.out.println("Youtube link that you pass: " + strings[0]);
        setYoutubeVideoUrl(strings[0]);
        youtubeVideoId = strings[0].replaceAll("(.*?)=", "");
        //getData(strings[0], result);
        result = videoDataRequest(strings[0]);
        System.out.println(" ------------------- New Json" + result);
        //jsonData(getResult());
        try {
            handelJsonData(getResult());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static void jsonData(String jsonString) {
//
//        if (jsonString != null) {
//            //convert JSON string to JSON Object
//            JSONParser parser = new JSONParser();
//            JSONObject videoJsonDataObject = null;
//            try {
//                videoJsonDataObject = (JSONObject) parser.parse(jsonString);
//            } catch (ParseException e) {
//                System.out.println("error in: convert JSON string to JSON Object");
//                e.printStackTrace();
//            }
//
//            //System.out.println(getResult());
//            System.out.println("----------------------------------------");
//            //get data fom JSON
//            //First path to go inside videos stream Urls ------------------------------------------
//            //JSONObject contents = (JSONObject) videoJsonDataObject.get("streamingData");
//            JSONObject contents = null;
//            try {
//                contents = (JSONObject) videoJsonDataObject.get("streamingData");
//            } catch (Exception e) {
//                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                System.out.println(videoJsonDataObject);
//                System.out.println("Input: " + jsonString);
//                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            }
//
//            //get the first video stream to play it: quality medium 360p
//            System.out.println("---------------------------------- First stream ----------------------------------");
//            JSONArray formats = (JSONArray) contents.get("formats");
//            JSONObject getFormatsData = (JSONObject) formats.get(0);
//
//            //get stream link quality statue
//            System.out.println(getFormatsData.get("quality"));
//
//            //get quality stage
//            System.out.println(getFormatsData.get("qualityLabel"));
//
//            /*I check if this JSON data have direct stream link or have a "signatureCipher"
//             * if it have "signatureCipher" I call a method that Decrypt "signatureCipher" and return
//             *  stream link in a string as a value */
//            if (getFormatsData.get("signatureCipher") != null) {
//                //get signatureCipher
//                String urlStream = getStreamLink((String) getFormatsData.get("signatureCipher"));
//
//                /*get stream url response state code before pass it
//                 * to see if we have to update cipher DecryptCipher code or not*/
//                System.out.println("Check -----------------------------");
//                if (!checkStremLink(urlStream)) {
//                    if (!checkStremLink(getStreamLink((String) getFormatsData.get("signatureCipher")))) {
//                        Log.e("getVideoStreamLink", "jsonData: I couldn't DecryptCipher Sig ");
//                        return;
//                    }
//                }
//            } else {
//                System.out.println(getFormatsData.get("url"));
//                setStreamLink((String) getFormatsData.get("url"));
//            }
//            System.out.println("-------------------------------------------------------------");
//
//            //get the rest of streaming links
//            //these links contain different quality and formats it's contain videos and audios
//            JSONArray getAdaptiveFormats = (JSONArray) contents.get("adaptiveFormats");
//            for (int i = 0; i < getAdaptiveFormats.size(); i++) {
//
//                JSONObject adaptiveFormats = (JSONObject) getAdaptiveFormats.get(i);
//
//                //get quality statue
//                System.out.println(adaptiveFormats.get("quality"));
//                quality.add((String) adaptiveFormats.get("quality"));
//
//                mimeType.add((String) adaptiveFormats.get("mimeType"));
//                long bit = (long) adaptiveFormats.get("bitrate");
//                bitrate.add(bit);
//
//                contentLength.add((long) adaptiveFormats.get("bitrate"));
//
//                //get quality type
//                /* check if format is a video or audio */
//                if (adaptiveFormats.get("qualityLabel") != null) {
//                    System.out.println(adaptiveFormats.get("qualityLabel"));
//                    qualityLabel.add((String) adaptiveFormats.get("qualityLabel"));
//                } else {
//                    System.out.println(adaptiveFormats.get("audioQuality"));
//                    qualityLabel.add((String) adaptiveFormats.get("audioQuality"));
//                    if (((String) adaptiveFormats.get("mimeType")).contains("audio/webm;") & adaptiveFormats.get("audioQuality").equals("AUDIO_QUALITY_MEDIUM")) {
//                        System.out.println("Boom");
//                        isStreamLinksReady = true;
//                    } else {
//                        System.out.println("--- not Boom ---");
//                        System.out.println("Type: " + adaptiveFormats.get("mimeType"));
//                    }
//                }
//
//
//                /*I check if this JSON data have direct stream link or have a "signatureCipher"
//                 * if it have "signatureCipher" I call a method that Decrypt "signatureCipher" and return
//                 *  stream link in a string as a value */
//                if (adaptiveFormats.get("signatureCipher") != null) {
//
//                    //get video Stream link from signatureCipher
//                    //String signatureCipher = getStreamLink((String) adaptiveFormats.get("signatureCipher")) ;
//                    //System.out.println("stream link or signature? "+signatureCipher);
//                    downloadLink.add((String) adaptiveFormats.get("signatureCipher"));
//                } else {
//                    System.out.println(adaptiveFormats.get("url"));
//                    downloadLink.add((String) adaptiveFormats.get("url"));
//                }
//
//                System.out.println("-------------------------------------------------------------");
//
//
//            }
//        } else {
//            System.out.println("Extract Stream link from Json data failed cause: I get null string!");
//        }
//
//    }

//    private static boolean checkStremLink(String urlStream) {
//        boolean[] flag = {true};
//        boolean[] value = {false};
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(urlStream)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                flag[0] = false;
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                System.out.println("++++++++++++++++++++++");
//                if (response.isSuccessful()) {
//                    System.out.println("++++++++++++++++++++++ Successful ++++++++++++++++++++++");
//                    System.out.println("url stream: " + urlStream);
//                    setStreamLink(urlStream);
//                    value[0] = true;
//                } else {
//                    System.out.println("++++++++++++++++++++++ Failed! ++++++++++++++++++++++");
//                    //updateJavascriptSourceLink(getYoutubeVideoUrl());
//                    // setStreamLink(null);
//                    updateCipherDecryptApi(youtubeVideoId);
//                }
//                flag[0] = false;
//            }
//        });
//
//        //threadKeepWorking(flag, "checkStremLink");
//        //flag = true;
//        while (flag[0]) {
//            try {
//                System.out.println("DecryptCipher" + " Says: I'm Waiting...");
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return value[0];
//    }

//    private static void updateCipherDecryptApi(String id) {
//        if (!id.isEmpty() && id != null) {
//            try {
//                String url = "https://www.youtube.com/embed/" + id;
//                Document doc = Jsoup.connect(url)
//                        .get();
//
//                String patternString = "/s/player/(.*?)/player_ias.vflset/(.*?)/base.js";
//                Pattern pattern = Pattern.compile(patternString);
//                Matcher matcher = pattern.matcher(doc.html());
//
//                if (matcher.find()) {
//                    String
//                            result = matcher.group(0);
//                    result = result.replace("/s/player/", "");
//                    result = result.replaceAll("/player_ias.vflset/(.*?)/base.js", "");
//
//                    //System.out.println("new js id: " + result);
//                    updateRequest(result);
//                }
//
//            } catch (IOException e) {
//                System.out.println("error in: url part");
//                e.printStackTrace();
//            }
//
//        } else {
//            System.out.println("id is null or empty");
//        }
//    }

//    private static void updateRequest(String id) {
//        if (!id.isEmpty() && id != null) {
//
//            boolean[] flag = {true};
//
//            String apiurl = "http://192.168.1.100:9070/api/update?d=" + id + "&key=" + generateKey(id, 1);
//
//            //System.out.println(apiurl);
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url(apiurl).build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    System.out.println("Bad Connection!");
//                    //flag[0] = false;
//                    flag[0] = false;
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    System.out.println(response.body().string());
//                    //flag[0] = false;
//                    flag[0] = false;
//
//                }
//            });
//
//            System.out.println("start ThreadKeepWorking");
//            //threadKeepWorking(false, "updateRequest");
//            //flag = true;
//            while (flag[0]) {
//                try {
//                    System.out.println("DecryptCipher" + " Says: I'm Waiting...");
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            //Waite api Database to update
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static String decodeLink(String url) {
//        url = url.replace("%3F", "?");
//        url = url.replace("%3D", "=");
//        url = url.replace("%26", "&");
//        url = url.replace("%25", "%");
//
//        //%25
//
//        //System.out.println(url);
//        return url;
//    }

//    public static void getData(String url, String result) {
//        //Jsoup Part -----------------------------------------------------------
//        if (!url.isEmpty() && url != null) {
//            try {
//                System.out.println("url: " + url);
//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0 (Windows NT x.y; Win64; x64; rv:10.0) Gecko/20100101 Firefox/10.0")
//                        .get();
//
//
//                //REGX Part --------------------------------------------------------
//                //This Regex Pattern part for extract stream json data from Html
//                String scriptStringPattern = "var\\sytplayer\\s=\\sytplayer(.*?)ytplayer.load.'\\);\\};";
//                //var\sytplayer\s=\sytplayer(.*?)ytplayer.load.'\);\};
//                Pattern scriptPattern = Pattern.compile(scriptStringPattern);
//                Matcher scriptMatcher = scriptPattern.matcher(doc.html());
//
//        /*the source code of youtube sometimes change
//        so, I check which regex pattern is work currently*/
//                if (scriptMatcher.find() != true) {
//                    scriptStringPattern = "var\\sytInitialPlayerResponse\\s=\\s\\{(.*?)\\};var";
//                    scriptPattern = Pattern.compile(scriptStringPattern);
//                    scriptMatcher = scriptPattern.matcher(doc.html());
//                    if (scriptMatcher.find()) {
//                        result = scriptMatcher.group(0);
//                        result = result.replace("var ytInitialPlayerResponse = ", "");
//                        result = result.replace(";var", "");
//                        Flag = true;
//                    } else {
//                        System.out.println("No Match in Stream link Json Data Regex Pattern");
//                    }
//
//                } else {
//                    result = scriptMatcher.group(0);
//                    result = result.replace("var ytplayer = ytplayer || {};ytplayer.config = {\"args\":{\"player_response\":\"", "");
//                    result = result.replace("var ytplayer = ytplayer || {};ytplayer.config = ", "");
//                    result = result.replace("\\\"", "\"");
//                    result = result.replace("\\\\", "\\");
//                    result = result.replace(";var meta = document.createElement('meta'); meta.name = 'referrer'; meta.content = 'origin-when-cross-origin'; document.getElementsByTagName('head')[0].appendChild(meta);", "");
//                }
//
//        /*I check if i have to do a extra json data extract or not,
//         it's depend on which regex pattern work in code above */
//                if (Flag == false) {
//                    //This Regex Pattern part for extract the important json part from the first regex result
//                    String falterStringPattern = "\"\\}\\};ytplayer.web_player_context_config(.*?)ytplayer.load.'\\);\\};";
//                    Pattern falterPattern = Pattern.compile(falterStringPattern);
//                    Matcher falterMatcher = falterPattern.matcher(result);
//                    if (falterMatcher.find()) {
//                        //System.out.println(falterMatcher.group(0));
//                        String replacer = falterMatcher.group(0);
//                        result = result.replace(replacer, "");
//
//                        setResult(result);
//                    } else {
//                        System.out.println("No Match in Second Stream link Json Data Regex Pattern");
//                    }
//                } else {
//                    setResult(result);
//                    System.out.println(result);
//                }
//            } catch (IOException e) {
//                System.out.println("error in: url part");
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("null URL");
//        }
//
//    }

//    public static String getStreamLink(String sig) {
//        sig = decodeLink(sig);
//        //System.out.println("After decode: \n "+ sig);
//
//        //Regex to get only signatureCipher
//        String sigStringPattern = "s=(.*?)&";
//        Pattern sigPattern = Pattern.compile(sigStringPattern);
//        Matcher sigMatcher = sigPattern.matcher(sig);
//        String sigResult = null;
//        if (sigMatcher.find()) {
//            sigResult = sigMatcher.group(0);
//            sigResult = sigResult.replace("s=", "");
//            sigResult = sigResult.replace("&", "");
//
//            sig = sig + "\"";
//        } else {
//            System.out.println("No match in get only signatureCipher regex pattern");
//        }
//
//
//        //Regex to get only url
//        String urlStringPattern = "url=(.*?)\"";
//        String urlResult = null;
//        Pattern urlPattern = Pattern.compile(urlStringPattern);
//        Matcher urlMatcher = urlPattern.matcher(sig);
//        if (urlMatcher.find()) {
//            urlResult = urlMatcher.group(0);
//            urlResult = urlResult.replace("url=", "");
//            urlResult = urlResult.replace("\"", "");
//        } else {
//            System.out.println("No match in get only url regex pattern");
//        }
//
//        /*Make object from "DecryptVideoSignature" Class that contain
//         methods that Decrypt "signatureCipher" and return the original value
//         */
//        if (sigResult != null) {
//
//            System.out.println("sigResult value: " + sigResult);
//            String decodeSig = decrypt(sigResult);
//            //flag = true;
//
//            //add Decrypted Signature to url and return the whole value into sig String
//            sig = urlResult.replace("&lsig=", "&sig=" + decodeSig + "&lsig=");
//            //System.out.println(sig);
//        }
//
//        return sig;
//
//
//    }

    //execute method
    //Calling the Api to DecryptCipher the Signature
//    public static String decrypt(String sig) {
//
//        final String[] value = {""};
//        boolean[] flag = {true};
//        String url = "http://192.168.1.100:9060/api/users?s=" + sig + "&cg=" + "" + "&key=" + generateKey(sig, 0);
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("Bad Response");
//                flag[0] = false;
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //System.out.println(response.body().string());
//                JSONObject jsonObject;
//                JSONParser jsonParser = new JSONParser();
//
//                try {
//                    jsonObject = (JSONObject) jsonParser.parse(response.body().string());
//                    //System.out.println(jsonObject.get("Response"));
//                    value[0] = (String) jsonObject.get("Response");
//                    flag[0] = false;
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        //threadKeepWorking(callB[0], "DecryptCipher");
//        while (flag[0]) {
//            try {
//                System.out.println("DecryptCipher" + " Says: I'm Waiting...");
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return value[0];
//    }

//    public static void updateCipherDecryptWay(String jsPath) {
//        System.out.println("----- updateCipherDecryptWay(); ----------");
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://www.youtube.com" + jsPath)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                try {
//                    extractNewCipherDecrypt(response.body().string());
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//        });
//    }

//    private static void extractNewCipherDecrypt(String jsSourceData) {
//
//        System.out.println("----- extractNewCipherDecrypt(); ----------");
//
//        String varPatternString = "var\\s[a-zA-Z]{2}=\\{[\\{\\}\\(\\)\\[\\]%=:;,.a-zA-Z0-9\\s\n]{0,200}\\}\\};";
//        String functionPatternString = "[a-zA-A]{0,4}=function(.*?)a.join\\(\"\"\\)\\};";
//
//        Pattern varPattern = Pattern.compile(varPatternString);
//        Pattern functionPattern = Pattern.compile(functionPatternString);
//
//        Matcher varMatcher = varPattern.matcher(jsSourceData);
//        Matcher functionMatcher = functionPattern.matcher(jsSourceData);
//
//        if (varMatcher.find() & functionMatcher.find()) {
//            if (varMatcher.group(0) != null & varMatcher.group(0).isEmpty() != true &
//                    functionMatcher.group(0) != null & functionMatcher.group(0).isEmpty() != true) {
//                //code
//                System.out.println("insert me!");
//                insertJsNewValue(functionMatcher.group(0), varMatcher.group(0), context);
//            } else {
//                System.out.println("Error to handel!! null or empty value passed in: " + getVideoStreamLink.class);
//            }
//
//        } else {
//            System.out.println("No matches!");
//        }
//    }

//    private static void insertJsNewValue(String func, String var, Context context) {
//
//        System.out.println("----- insertJsNewValue(); ----------");
//        //System.out.println("func: "+ func+"\n var: "+var);
//        shareP.insert(func, var, context);
//        System.out.println("Data insert successfully");
//    }

//    private static String musicCipherDecrypt(String sig) {
//        System.out.println("----- musicCipherDecrypt(); ----------");
//
//        shareP.read(context);
//        String value = null;
//
//        if (shareP.getFunc() != null & !shareP.getFunc().isEmpty() &
//                shareP.getVar() != null & !shareP.getVar().isEmpty()) {
//
//            System.out.println("--------------------------------------------------------");
//            System.out.println("--------------------inside if-----------------------");
//            System.out.println("--------------------------------------------------------");
//
//            String func = shareP.getFunc();
//            String var = shareP.getVar();
//            String decode = "";
//
//            try {
//                System.out.println("Original sig: " + sig);
//                decode = func.substring(0, 2) + "(\"" + sig + "\");";
//
//                V8 runtime = V8.createV8Runtime();
//                value = runtime.executeStringScript(func + " " + var + " " + decode);
//                runtime.release();
//
//            } catch (Exception e) {
//                System.out.println("Error in Javascript part");
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Null inputs! in musicCipherDecrypt()");
//        }
//
//        System.out.println("Value of js cipher DecryptCipher: " + value);
//        return value;
//    }

//    private static void updateJavascriptSourceLink(String youtubeVideoUrl) {
//
//        if (youtubeVideoUrl != null || !youtubeVideoUrl.isEmpty()) {
//
//            Document doc = null;
//            try {
//                doc = Jsoup.connect(youtubeVideoUrl)
//                        .get();
//            } catch (IOException e) {
//                System.out.println("error in: url part");
//                e.printStackTrace();
//            }
//
//            String patternString = "/s/player/(.*?)/player_ias.vflset/(.*?)/base.js";
//            Pattern pattern = Pattern.compile(patternString);
//            Matcher matcher = pattern.matcher(doc.html());
//
//            if (matcher.find()) {
//                System.out.println("new js url: " + matcher.group(0));
//                updateCipherDecryptWay(matcher.group(0));
//            }
//        } else {
//            System.out.println("Error: null or empty value passed to ==> updateJavascriptSourceLink()");
//        }
//    }

    //Generate key for api request
    public static String generateKey(String input, int type) {

        String key = "";
        if (type == 0) {
            key = input.substring(5, 25);
        } else {
            key = input;
        }


        HashMap<Character, Character> hashMap = new HashMap();
        hashMap.put('A', 'm');
        hashMap.put('B', 'l');
        hashMap.put('C', 'k');
        hashMap.put('D', 'j');
        hashMap.put('E', 'i');
        hashMap.put('F', 'h');
        hashMap.put('G', 'g');
        hashMap.put('H', 'f');
        hashMap.put('I', 'e');
        hashMap.put('J', 'd');
        hashMap.put('K', 'c');
        hashMap.put('L', 'b');
        hashMap.put('M', 'a');

        hashMap.put('N', 'z');
        hashMap.put('O', 'y');
        hashMap.put('P', 'x');
        hashMap.put('Q', 'w');
        hashMap.put('R', 'v');
        hashMap.put('S', 'u');
        hashMap.put('T', 't');
        hashMap.put('U', 's');
        hashMap.put('V', 'r');
        hashMap.put('W', 'q');
        hashMap.put('X', 'p');
        hashMap.put('Y', 'o');
        hashMap.put('Z', 'n');

        //Change Lower Case Letters
        hashMap.put('a', 'N');
        hashMap.put('b', 'O');
        hashMap.put('c', 'P');
        hashMap.put('d', 'Q');
        hashMap.put('e', 'R');
        hashMap.put('f', 'S');
        hashMap.put('g', 'T');
        hashMap.put('h', 'U');
        hashMap.put('i', 'V');
        hashMap.put('j', 'W');
        hashMap.put('k', 'X');
        hashMap.put('l', 'Y');
        hashMap.put('m', 'Z');

        hashMap.put('n', 'M');
        hashMap.put('o', 'L');
        hashMap.put('p', 'K');
        hashMap.put('q', 'J');
        hashMap.put('r', 'I');
        hashMap.put('s', 'H');
        hashMap.put('t', 'G');
        hashMap.put('u', 'F');
        hashMap.put('v', 'E');
        hashMap.put('w', 'D');
        hashMap.put('x', 'C');
        hashMap.put('y', 'B');
        hashMap.put('z', 'A');

        //Change Numbers
        hashMap.put('0', '5');
        hashMap.put('1', '6');
        hashMap.put('2', '7');
        hashMap.put('3', '8');
        hashMap.put('4', '9');

        hashMap.put('5', '0');
        hashMap.put('6', '1');
        hashMap.put('7', '2');
        hashMap.put('8', '3');
        hashMap.put('9', '4');

        StringBuilder result = new StringBuilder(key);
        for (int i = 0; i < key.length(); i++) {

            if (hashMap.get(key.charAt(i)) != null) {
                result.setCharAt(i, hashMap.get(key.charAt(i)));
            }

        }

        return result.toString();
    }

    public static String videoDataRequest(String url) {
        String[] json = {""};
        boolean[] flag = {true};

        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .addHeader("host", "www.youtube.com")
                .addHeader("x-youtube-client-name", "1")
                .addHeader("x-youtube-client-version", "2.20210304.08.01")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36")
                .url(url + "&pbj=1").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getVideoStreamLink", "Bad Connection");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    json[0] = response.body().string();
                    flag[0] = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while (flag[0]) {
            try {
                Log.i("getVideoStreamLink", "Sleep...");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return json[0];
    }

    public static void handelJsonData(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();

        jsonArray = (JSONArray) jsonParser.parse(json);
        JSONObject get_playerResponse = (JSONObject) jsonArray.get(2);
        JSONObject playerResponse = (JSONObject) get_playerResponse.get("playerResponse");
        JSONObject microformat = (JSONObject) playerResponse.get("microformat");
        JSONObject playerMicroformatRenderer = (JSONObject) microformat.get("playerMicroformatRenderer");

        //Category

//        if(playerMicroformatRenderer.get("category") != null){
//            System.out.println("Category: "+playerMicroformatRenderer.get("category"));
//            category.add((String) playerMicroformatRenderer.get("category"));
//        }else {
//            category.add("");
//        }

        JSONObject streamingData = (JSONObject) playerResponse.get("streamingData");
        JSONArray get_formats = (JSONArray) streamingData.get("formats");
        JSONObject formats = (JSONObject) get_formats.get(0);

        //Bitrate
//        System.out.println("Bitrate: " + formats.get("bitrate"));
//        long bit = (Long) formats.get("bitrate");
//        bitrate.add(bit);

        //ContentLength
//        System.out.println("ContentLength: " + formats.get("contentLength"));
//        //long contentL = Long.valueOf((Long) formats.get("contentLength"));
//        contentLength.add((long) formats.get("bitrate"));

        //quality
//        System.out.println("Quality: " + formats.get("quality"));
//        quality.add((String) formats.get("quality"));
//        //mimeType
//        mimeType.add((String) formats.get("mimeType"));
//
//        //-------------------------------------
//
//        //QualityLabel
//        System.out.println("QualityLabel: " + formats.get("qualityLabel"));
//        qualityLabel.add((String) formats.get("qualityLabel"));

        //-------------------------------------

        /*I check if this JSON data have direct stream link or have a "signatureCipher"
         * if it have "signatureCipher" I call a method that Decrypt "signatureCipher" and return
         *  stream link in a string as a value */
//        if (formats.get("signatureCipher") != null) {
//            //get signatureCipher
//            String urlStream = getStreamLink((String) formats.get("signatureCipher"));
//
//            /*get stream url response state code before pass it
//             * to see if we have to update cipher DecryptCipher code or not*/
//            System.out.println("Check -----------------------------");
//            if (!checkStremLink(urlStream)) {
//                if (!checkStremLink(getStreamLink((String) formats.get("signatureCipher")))) {
//                    Log.e("getVideoStreamLink", "jsonData: I couldn't DecryptCipher Sig ");
//                    return;
//                }
//            }
//        } else {
//            System.out.println(formats.get("url"));
//            setStreamLink((String) formats.get("url"));
//            downloadLink.add((String) formats.get("url"));
//        }


        JSONArray get_adaptiveFormats = (JSONArray) streamingData.get("adaptiveFormats");
        for (int i = 0; i < get_adaptiveFormats.size(); i++) {
            System.out.println("-------------------------------");
            JSONObject adaptiveFormats = (JSONObject) get_adaptiveFormats.get(i);

            //Bitrate
            System.out.println("Bitrate: " + adaptiveFormats.get("bitrate"));
            //bitrate.add((Long) adaptiveFormats.get("bitrate"));
            Long bitrate = (Long) adaptiveFormats.get("bitrate");

            //ContentLength
            System.out.println("ContentLength: " + adaptiveFormats.get("contentLength"));
            //contentLength.add((Long) adaptiveFormats.get("bitrate"));
            Long contentLength = Long.valueOf((String) adaptiveFormats.get("contentLength"));

            //quality
            System.out.println("Quality: " + adaptiveFormats.get("quality"));
            //quality.add((String) adaptiveFormats.get("quality"));
            String quality = (String) adaptiveFormats.get("quality");


            //mimeType
            //mimeType.add((String) adaptiveFormats.get("mimeType"));
            String mimeType = (String) adaptiveFormats.get("mimeType");

            //get quality type
            /* check if format is a video or audio */
            String qualityLabel;
            if (adaptiveFormats.get("qualityLabel") != null) {
                //QualityLabel
                System.out.println("QualityLabel: " + adaptiveFormats.get("qualityLabel"));
                //qualityLabel.add((String) adaptiveFormats.get("qualityLabel"));
                qualityLabel = (String) adaptiveFormats.get("qualityLabel");
            } else {
                System.out.println("AudioQuality: " + adaptiveFormats.get("audioQuality"));
//                qualityLabel.add((String) adaptiveFormats.get("audioQuality"));
                qualityLabel = (String) adaptiveFormats.get("audioQuality");

                if (((String) adaptiveFormats.get("mimeType")).contains("audio/webm;") & adaptiveFormats.get("audioQuality").equals("AUDIO_QUALITY_MEDIUM")) {
                    System.out.println("Boom");
                    isStreamLinksReady = true;
                } else {
                    System.out.println("--- not Boom ---");
                    System.out.println("Type: " + adaptiveFormats.get("mimeType"));
                }
            }
            /*I check if this JSON data have direct stream link or have a "signatureCipher"
             * if it have "signatureCipher" I call a method that Decrypt "signatureCipher" and return
             *  stream link in a string as a value */
            String downloadLink;
            if (adaptiveFormats.get("signatureCipher") != null) {

                //get video Stream link from signatureCipher
                //String signatureCipher = getStreamLink((String) adaptiveFormats.get("signatureCipher")) ;
                //System.out.println("stream link or signature? "+signatureCipher);
                //downloadLink.add((String) adaptiveFormats.get("signatureCipher"));
                downloadLink = (String) adaptiveFormats.get("signatureCipher");
            } else {
                System.out.println(adaptiveFormats.get("url"));
//                downloadLink.add((String) adaptiveFormats.get("url"));
                downloadLink = (String) adaptiveFormats.get("url");
            }
            videoStreamObjectList.add(new VideoStreamObject(mimeType, quality, qualityLabel, downloadLink, contentLength, bitrate));
            System.out.println("-------------------------------");

        }

        //streamObjectList.set(0,  videoStreamObjectList);
        fetchDownloadingLinksCallback.callback(videoStreamObjectList);
        videoStreamObjectList.clear();
    }

//    public static void threadKeepWorking(boolean flag, String methodName) {
//        while (flag) {
//            try {
//                System.out.println(methodName + " Says: I'm Waiting...");
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
