package com.app.vruddy.Models.DecryptCipher;


import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.app.vruddy.Data.database.Cipher.Cipher;
import com.app.vruddy.Data.database.Cipher.CipherViewModel;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DecryptCipher{
    private static ResultCallBack resultCallBack;
    private static CipherViewModel cipherViewModel;
    private static List<Cipher> ciphers = new ArrayList<>();
    private static String videoId;
    private static String cipher;
    private static String type;
    private static String value;
    private static String decode = "";

    public DecryptCipher(String id, FragmentActivity fragmentActivity, CipherViewModel cipherViewModel, ResultCallBack resultCallBack) {
        DecryptCipher.cipherViewModel = cipherViewModel;
        this.resultCallBack = resultCallBack;
        videoId = id;

        cipherViewModel.getGetAll().observe(fragmentActivity, new Observer<List<Cipher>>() {
            @Override
            public void onChanged(List<Cipher> localCiphers) {
                System.out.println("-------- Cipher Size: " + localCiphers.size());
                ciphers = localCiphers;
            }
        });

    }


    public void decrypt(String cipher, String type) {
        DecryptCipher.type = type;
        DecryptCipher.cipher = cipher;
        if (ciphers.size() == 1) {
            decodeLink(cipher);
        } else {
            updateCipherDecryption("https://www.youtube.com/embed/" + videoId);
        }
    }

    private void decodeLink(String url) {
        url = url.replace("%3F", "?");
        url = url.replace("%3D", "=");
        url = url.replace("%26", "&");
        url = url.replace("%25", "%");

        getStreamLink(url);
    }

    private void getStreamLink(String sig) {

        //Regex to get only signatureCipher
        String sigStringPattern = "s=(.*?)&";
        Pattern sigPattern = Pattern.compile(sigStringPattern);
        Matcher sigMatcher = sigPattern.matcher(sig);
        String sigResult = null;
        if (sigMatcher.find()) {
            sigResult = sigMatcher.group(0);
            sigResult = sigResult.replace("s=", "");
            sigResult = sigResult.replace("&", "");

            sig = sig + "\"";
        } else {
            System.out.println("No match in get only signatureCipher regex pattern");
        }


        //Regex to get only url
        String urlStringPattern = "url=(.*?)\"";
        String urlResult = null;
        Pattern urlPattern = Pattern.compile(urlStringPattern);
        Matcher urlMatcher = urlPattern.matcher(sig);
        if (urlMatcher.find()) {
            urlResult = urlMatcher.group(0);
            urlResult = urlResult.replace("url=", "");
            urlResult = urlResult.replace("\"", "");
        } else {
            System.out.println("No match in get only url regex pattern");
        }

        /*Make object from "DecryptVideoSignature" Class that contain
         methods that Decrypt "signatureCipher" and return the original value
         */
        if (sigResult != null) {

            System.out.println("sigResult value: " + sigResult);
            //String decodeSig = decrypt();
            chooseTheRightDecryptWay(sigResult, urlResult);
            //flag = true;

            //add Decrypted Signature to url and return the whole value into sig String
            //sig = urlResult.replace("&lsig=", "&sig=" + decodeSig + "&lsig=");
            //System.out.println(sig);
        }

//        return sig;


    }

    private void chooseTheRightDecryptWay(String sig, String restOfUrl) {
        //
        musicCipherDecrypt(sig, restOfUrl);
    }

    private void musicCipherDecrypt(String sig, String restOfUrl) {
        System.out.println("----- musicCipherDecrypt(); ----------");

        if (ciphers.size() != 0 && ciphers != null) {

            System.out.println("--------------------------------------------------------");
            System.out.println("--------------------inside if-----------------------");
            System.out.println("--------------------------------------------------------");

            String func = ciphers.get(0).getVarA();
            String var = ciphers.get(0).getVarB();

            System.out.println("--------------------------------------------------------");
            System.out.println("Func: " + func);
            System.out.println("Var: " + var);
            System.out.println("--------------------------------------------------------");

            try {
                System.out.println("Original sig: " + sig);
                int i = func.indexOf("=");
                decode = func.substring(0, i) + "(\"" + sig + "\");";

                String s = "var mainFunction=function(){ "+func+" "+var+" return "+decode+"}";
                value = JSExecutor.runScript(s);

                checkStreamLink(restOfUrl.replace("&lsig=", "&sig=" + value + "&lsig="));

            } catch (Exception e) {
                System.out.println("Error in Javascript part");
                e.printStackTrace();
            }
        } else {
            System.out.println("Null inputs! in musicCipherDecrypt()");
        }

        System.out.println("Value of js cipher DecryptCipher: " + value);
    }

    private void videoCipherDecrypt(String sig) {
        System.out.println("----- musicCipherDecrypt(); ----------");

        String value = null;

        if (ciphers.size() != 0 && ciphers != null) {

            System.out.println("--------------------------------------------------------");
            System.out.println("--------------------inside if-----------------------");
            System.out.println("--------------------------------------------------------");

            String func = ciphers.get(1).getVarA();
            String var = ciphers.get(1).getVarB();
            String decode = "";

            try {
               //

            } catch (Exception e) {
                System.out.println("Error in Javascript part");
                e.printStackTrace();
            }
        } else {
            System.out.println("Null inputs! in musicCipherDecrypt()");
        }

        System.out.println("Value of js cipher DecryptCipher: " + value);
    }

    private void checkStreamLink(String urlStream) {

        System.out.println("--------------------------------------------------------");
        System.out.println("Check This Url Stream: " + urlStream);
        System.out.println("--------------------------------------------------------");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlStream)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    System.out.println("++++++++++++++++++++++ Successful ++++++++++++++++++++++");

                    if (type != null && type.equals("F")) {
                        resultCallBack.firstStream(urlStream);
                    }
                    if(type != null && type.equals("W")){
                        resultCallBack.watchUrl(urlStream);
                    }
                    else {
                        resultCallBack.secStream(urlStream);
                    }
                } else {
                    System.out.println("++++++++++++++++++++++ Failed! ++++++++++++++++++++++");
                    updateCipherDecryption("https://www.youtube.com/embed/" + videoId);
                }
            }
        });
    }

    private void updateCipherDecryption(String youtubeVideoUrl) {
        if (youtubeVideoUrl != null || !youtubeVideoUrl.isEmpty()) {

//            Document doc = null;
//            try {
//                doc = Jsoup.connect(youtubeVideoUrl)
//                        .get();
//            } catch (IOException e) {
//                System.out.println("error in: url part");
//                e.printStackTrace();
//            }

            OkHttpClient okHttpClient = new OkHttpClient();
            Request builder = new Request.Builder()
                    .url(youtubeVideoUrl)
                    .build();
            okHttpClient
                    .newCall(builder)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //
                            //System.out.println(response.body().string());
                            String patternString = "/s/player/(.*?)/player_ias.vflset/(.*?)/base.js";
                            Pattern pattern = Pattern.compile(patternString);
                            Matcher matcher = pattern.matcher(response.body().string());

                            if (matcher.find()) {
                                System.out.println("new js url: " + matcher.group(0));
                                updateCipherDecryptWay(matcher.group(0));
                            }
                        }
                    });
        } else {
            System.out.println("Error: null or empty value passed to ==> updateCipherDecryption()");
        }
    }

    private void updateCipherDecryptWay(String jsPath) {
        System.out.println("----- updateCipherDecryptWay(); ----------");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.youtube.com" + jsPath)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    extractNewCipherDecrypt(response.body().string());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

    private void extractNewCipherDecrypt(String jsSourceData) {

        System.out.println("----- extractNewCipherDecrypt(); ----------");

        String varPatternString = "var\\s[a-zA-Z]{2}=\\{[\\{\\}\\(\\)\\[\\]%=:;,.a-zA-Z0-9\\s\n]{0,200}\\}\\};";
        String functionPatternString = "[a-zA-A]{0,4}=function(.*?)a.join\\(\"\"\\)\\};";

        Pattern varPattern = Pattern.compile(varPatternString);
        Pattern functionPattern = Pattern.compile(functionPatternString);

        Matcher varMatcher = varPattern.matcher(jsSourceData);
        Matcher functionMatcher = functionPattern.matcher(jsSourceData);

        if (varMatcher.find() & functionMatcher.find()) {
            if (varMatcher.group(0) != null & varMatcher.group(0).isEmpty() != true &
                    functionMatcher.group(0) != null & functionMatcher.group(0).isEmpty() != true) {
                //code
                System.out.println("insert me!");
                //insertJsNewValue(functionMatcher.group(0), varMatcher.group(0), context);
                if (ciphers.size() == 0) {
                    cipherViewModel.insert(new Cipher(functionMatcher.group(0), varMatcher.group(0)));
                } else {
                    cipherViewModel.update(functionMatcher.group(0), varMatcher.group(0));
                }

                decrypt(cipher, type);
            } else {
                System.out.println("Error to handel!! null or empty value passed in: extractNewCipherDecrypt(String jsSourceData)");
            }

        } else {
            System.out.println("No matches!");
        }
    }
}
