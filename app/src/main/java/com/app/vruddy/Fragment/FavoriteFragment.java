package com.app.vruddy.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vruddy.Adapter.FavoriteAdapter;
import com.app.vruddy.AsyncTask.getVideoStreamLink;
import com.app.vruddy.DecryptCipher.DecryptCipher;
import com.app.vruddy.DecryptCipher.ResultCallBack;
import com.app.vruddy.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.HomeActivity;
import com.app.vruddy.database.Cipher.CipherViewModel;
import com.app.vruddy.database.Favorite.Favorite;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.app.vruddy.database.Favorite.FavoriteViewModel;
import com.app.vruddy.testService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private FavoriteViewModel favoriteViewModel;
    private FloatingActionButton fab;
    private static FavoriteIndex favoriteIndex;
    private static List<VideoObject> videoObjectsList = new ArrayList<>();

    private static boolean is4KFound = false;
    private static boolean is1440Found = false;
    private static boolean is1080Found = false;
    private static boolean is720Found = false;
    private static boolean is480Found = false;
    private static boolean is360Found = false;
    private static boolean is240Found = false;
    private static boolean isTinyFound = false;
    private static boolean isSmallFound = false;
    private static boolean isMediumFound = false;
    private static boolean isLargeFound = false;
    private static boolean isPermissionGranted = false;
    private static boolean sleep = true;
    private static boolean isPermissionResponse = false;


    private static String p240DownloadLink;
    private static String p360DownloadLink;
    private static String p480DownloadLink;
    private static String p720DownloadLink;
    private static String p1080DownloadLink;
    private static String p1440DownloadLink;
    private static String k4DownloadLink;
    private static String aacDownloadLink;
    private static String tinyDownloadLink;
    private static String smallDownloadLink;
    private static String mediumDownloadLink;
    private static String largeDownloadLink;

    private static Dialog downloadWindow;
    private static TextView downloadWindowTitle;
    private static TextView downloadWindowTime;
    private static ImageView downloadWindowThumbnail;
    private static LinearLayout download_links_layout;
    private static RelativeLayout waiting_download_links_spinner_layout;
    private static RelativeLayout p240;
    private static RelativeLayout p360;
    private static RelativeLayout p480;
    private static RelativeLayout p720;
    private static RelativeLayout p1080;
    private static RelativeLayout p1440;
    private static RelativeLayout k4;
    private static TextView p240MbSize;
    private static TextView p360MbSize;
    private static TextView p480MbSize;
    private static TextView p720MbSize;
    private static TextView p1080MbSize;
    private static TextView p1440MbSize;
    private static TextView k4MbSize;

    private static TextView tinyMbSize;
    private static TextView smallMbSize;
    private static TextView mediumMbSize;
    private static TextView largeMbSize;
    private static TextView tinyBitrateSize;
    private static TextView smallBitrateSize;
    private static TextView mediumBitrateSize;
    private static TextView largeBitrateSize;

    private static CardView k4Card;
    private static CardView p1440Card;
    private static CardView p1080Card;
    private static CardView p720Card;
    private static CardView p480Card;
    private static CardView p360Card;
    private static CardView p240Card;

    private static CardView largeCard;
    private static CardView mediumCard;
    private static CardView smallCard;
    private static CardView tinyCard;

    private static boolean isFirstTime = true;

    private static CipherViewModel cipherViewModel;
    private static DecryptCipher decryptCipher;
    private static Activity activity;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteIndex = FavoriteIndex.getInstance();

        cipherViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity()
                        .getApplication()))
                .get(CipherViewModel.class);

        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);

        downloadWindow = new Dialog(activity);

        RecyclerView recyclerView = view.findViewById(R.id.favorite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
        recyclerView.setAdapter(favoriteAdapter);

        favoriteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(FavoriteViewModel.class);
        favoriteViewModel.getAllFavorites().observe(getActivity(), new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                Collections.reverse(favorites);
                favoriteAdapter.setFavorites(favorites);

                //convert favorite Object List to Video Object List to use it in other Function
                if (favorites.size() != 0) {
                    for (int index = 0; index < favorites.size(); index++) {
                        VideoObject mVideoObject = new VideoObject(favorites.get(index).getVideo_id(), favorites.get(index).getTitle(), favorites.get(index).getBy(), favorites.get(index).getTime_line(), favorites.get(index).getDate(), favorites.get(index).getViews(), false, favorites.get(index).getThumbnail(), favorites.get(index).getChannel_image());
                        if (!videoObjectsList.contains(mVideoObject)) {
                            videoObjectsList.add(mVideoObject);
                        }
                    }
                }
            }
        });
        favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                //delete element
                String video_id = favoriteAdapter.getFavorites().get(position).getVideo_id();
                favoriteIndex.removeId(video_id);
                favoriteViewModel.deleteById(video_id);
            }

            @Override
            public void onDownloadClick(int position) {

                if (HomeActivity.isPermission) {
                    VideoObject mVideoObject = videoObjectsList.get(position);
                    getVideoStreamLink downloadUrls = new getVideoStreamLink(activity, new FetchDownloadingLinksCallback() {
                        @Override
                        public void callback(List<VideoStreamObject> videoStreamObjectList) {
                            //
                            generateDownloadingOption(mVideoObject, videoStreamObjectList);
                            System.out.println("------- Top Called");
                        }
                    });
                    downloadUrls.execute("https://www.youtube.com/watch?v=" + mVideoObject.getVideoId());
                    createDownloadDialog(mVideoObject);
                } else {
                    Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemClick(int position) {
                watchIt(position);
            }
        });

        //Delete All Element
        fab = view.findViewById(R.id.fab);
        deleteAllConfirmation();


        return view;
    }

    private void watchIt(int position) {
        WatchFragment watchFragment = new WatchFragment();
        sendData(watchFragment, videoObjectsList.get(position));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isFirstTime) {
            fragmentTransaction.add(R.id.watchFragment, watchFragment);
            isFirstTime = false;
        } else {
            fragmentTransaction.replace(R.id.watchFragment, watchFragment);
            ((HomeActivity) activity).frameLayoutSwitch();
        }
        fragmentTransaction.commit();
    }

    private void sendData(Fragment fragment, VideoObject videoObject) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "HomeFeed");
        bundle.putString("video_id", videoObject.getVideoId());
        bundle.putString("video_thumbnail", videoObject.getThumbnail());
        bundle.putString("video_title", videoObject.getTitle());
        bundle.putString("video_views", videoObject.getViews());
        bundle.putString("video_date", videoObject.getDate());
        bundle.putString("video_time", videoObject.getTime());
        bundle.putString("channel_name", videoObject.getBy());
        bundle.putString("channel_pic", videoObject.getChannelThumbnail());
        fragment.setArguments(bundle);
    }

    private void deleteAllConfirmation() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        System.out.println("Yes");
                        favoriteIndex.removeAll();
                        favoriteViewModel.deleteAll();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        System.out.println("No");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setMessage("Do You Want To Delete All Your Favorite Videos?");
                dialog.show();
            }
        });
    }

    //downloading part
    private void createDownloadDialog(VideoObject videoList) {
        downloadWindow.setContentView(R.layout.my_dialog);
        downloadWindowThumbnail = downloadWindow.findViewById(R.id.thumbnailRoundedImageView);
        downloadWindowTitle = downloadWindow.findViewById(R.id.titleOfDownloadFile);
        downloadWindowTime = downloadWindow.findViewById(R.id.downloadTime);

        waiting_download_links_spinner_layout = downloadWindow.findViewById(R.id.waiting_download_links_spinner_layout);
        download_links_layout = downloadWindow.findViewById(R.id.download_links_layout);

        p240Card = downloadWindow.findViewById(R.id.p240Card);
        p360Card = downloadWindow.findViewById(R.id.p360Card);
        p480Card = downloadWindow.findViewById(R.id.p480Card);
        p720Card = downloadWindow.findViewById(R.id.p720Card);
        p1080Card = downloadWindow.findViewById(R.id.p1080Card);
        p1440Card = downloadWindow.findViewById(R.id.p1440Card);
        k4Card = downloadWindow.findViewById(R.id.k4Card);

        p240 = downloadWindow.findViewById(R.id.p240);
        p360 = downloadWindow.findViewById(R.id.p360);
        p480 = downloadWindow.findViewById(R.id.p480);
        p720 = downloadWindow.findViewById(R.id.p720);
        p1080 = downloadWindow.findViewById(R.id.p1080);
        p1440 = downloadWindow.findViewById(R.id.p1440);
        k4 = downloadWindow.findViewById(R.id.k4);

        p240MbSize = downloadWindow.findViewById(R.id.p240MbSize);
        p360MbSize = downloadWindow.findViewById(R.id.p360MbSize);
        p480MbSize = downloadWindow.findViewById(R.id.p480MbSize);
        p720MbSize = downloadWindow.findViewById(R.id.p720MSize);
        p1080MbSize = downloadWindow.findViewById(R.id.p1080MbSize);
        p1440MbSize = downloadWindow.findViewById(R.id.p1440MbSize);
        k4MbSize = downloadWindow.findViewById(R.id.k4MbSize);

        tinyCard = downloadWindow.findViewById(R.id.tinyCard);
        smallCard = downloadWindow.findViewById(R.id.smallCard);
        mediumCard = downloadWindow.findViewById(R.id.mediumCard);
        largeCard = downloadWindow.findViewById(R.id.largCard);

        tinyMbSize = downloadWindow.findViewById(R.id.tinyMbSize);
        smallMbSize = downloadWindow.findViewById(R.id.smallMbSize);
        mediumMbSize = downloadWindow.findViewById(R.id.mediumMbSize);
        largeMbSize = downloadWindow.findViewById(R.id.largeMbSize);

        tinyBitrateSize = downloadWindow.findViewById(R.id.tinyBiterateSize);
        smallBitrateSize = downloadWindow.findViewById(R.id.smallBiterateSize);
        mediumBitrateSize = downloadWindow.findViewById(R.id.mediumBiterateSize);
        largeBitrateSize = downloadWindow.findViewById(R.id.largeBitrateSize);


        Picasso.get()
                .load(videoList.getThumbnail())
                .centerCrop()
                .fit()
                .into(downloadWindowThumbnail);
        downloadWindowTitle.setText(videoList.getTitle());
        downloadWindowTime.setText(videoList.getTime());

        downloadWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        downloadWindow.show();
    }

    public void generateDownloadingOption(VideoObject videoObject, List<VideoStreamObject> videoStreamObjectList) {
        //downloadUrls.isStreamLinksReady()

        is4KFound = false;
        is1080Found = false;
        is720Found = false;
        is480Found = false;
        is360Found = false;
        is240Found = false;
        isLargeFound = false;
        isMediumFound = false;
        isSmallFound = false;
        isTinyFound = false;
        //downloadUrls.setIsStreamLinksReady(false);

        //
        for (int c = 0; c < videoStreamObjectList.size(); c++) {
            //Calculate InProgressFile Size
            //1048576
            try {
                double mbUnite = 14857;
                double kbUnite = 1000;
                DecimalFormat df = new DecimalFormat("#.##");

                double result = videoStreamObjectList.get(c).getContentLength() / (1024 * 1024);
                //downloadUrls.getContentLength().get(c) / mbUnite;
                double bitRate = videoStreamObjectList.get(c).getBitrate() / kbUnite;
                //downloadUrls.getBitrate().get(c) / kbUnite;

                //falter data
                falterQuality(videoStreamObjectList.get(c).getQuality(), /*Double.valueOf(df.format(result))*/result, videoStreamObjectList.get(c).getMimeType(), bitRate, videoStreamObjectList.get(c).getDownloadLink());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("At index: [" + c + "]");
            }

        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                optionButtonsListener(videoObject);
            }
        });
    }

    public void falterQuality(String quality, double fileSize, String audioType, double bitRate, String downloadLink) {

        System.out.println("Faltering .... " + "Quality: " + quality);
        //hd2160
        if (is4KFound != true) {
            k4DownloadLink = downloadLink;
            if (quality.equals("hd2160")) {
                k4.setVisibility(View.VISIBLE);
                is4KFound = true;
                k4MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                k4.setVisibility(View.GONE);
            }
        }
        //hd1440
        if (is1440Found != true) {
            if (quality.equals("hd1440")) {
                p1440DownloadLink = downloadLink;
                p1440.setVisibility(View.VISIBLE);
                is1440Found = true;
                p1440MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1440.setVisibility(View.GONE);
            }
        }
        //hd1080
        if (is1080Found != true) {
            if (quality.equals("hd1080")) {
                p1080DownloadLink = downloadLink;
                p1080.setVisibility(View.VISIBLE);
                is1080Found = true;
                p1080MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p1080.setVisibility(View.GONE);
            }
        }
        //hd720
        if (is720Found != true) {
            if (quality.equals("hd720")) {
                p720DownloadLink = downloadLink;
                p720.setVisibility(View.VISIBLE);
                is720Found = true;
                p720MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p720.setVisibility(View.GONE);
            }
        }
        //480p
        if (is480Found != true) {
            if (quality.equals("large")) {
                p480DownloadLink = downloadLink;
                p480.setVisibility(View.VISIBLE);
                is480Found = true;
                p480MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p480.setVisibility(View.GONE);
            }
        }
        //360p
        if (is360Found != true) {
            if (quality.equals("medium")) {
                p360DownloadLink = downloadLink;
                p360.setVisibility(View.VISIBLE);
                is360Found = true;
                p360MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p360.setVisibility(View.GONE);
            }
        }
        //240p
        if (is240Found != true) {
            if (quality.equals("small")) {
                p240DownloadLink = downloadLink;
                p240.setVisibility(View.VISIBLE);
                is240Found = true;
                p240MbSize.setText(String.format("%.2f", fileSize) + "MB");
                return;
            } else {
                p240.setVisibility(View.GONE);
            }
        }

        //Audio part
        //find mp4a audio
        //we will need this audio format if we want to download almost any video
        if (audioType.equals("audio/mp4; codecs=\"mp4a.40.2\"")) {
            System.out.println("I found mp4 audio");
            aacDownloadLink = downloadLink;
        }
        //Large
        if (isLargeFound != true & audioType.contains("audio")) {
            largeDownloadLink = downloadLink;
            isLargeFound = true;
            largeMbSize.setText(String.format("%.2f", fileSize) + "MB");
            largeBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
        } else {
            //Medium
            if (isMediumFound != true & audioType.contains("audio")) {
                mediumDownloadLink = downloadLink;
                isMediumFound = true;
                mediumMbSize.setText(String.format("%.2f", fileSize) + "MB");
                mediumBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
            } else {
                //Small
                if (isSmallFound != true & audioType.contains("audio")) {
                    smallDownloadLink = downloadLink;
                    isSmallFound = true;
                    smallMbSize.setText(String.format("%.2f", fileSize) + "MB");
                    smallBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                } else {
                    //Tiny
                    if (isTinyFound != true & audioType.contains("audio")) {
                        tinyDownloadLink = downloadLink;
                        isTinyFound = true;
                        tinyMbSize.setText(String.format("%.2f", fileSize) + "MB");
                        tinyBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                    }
                }
            }
        }


    }

    private void optionButtonsListener(VideoObject videoObject) {
        waiting_download_links_spinner_layout.setVisibility(View.GONE);
        download_links_layout.setVisibility(View.VISIBLE);


        //Video part
        k4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (k4DownloadLink != null) {
                    if (k4DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", k4DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                k4DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", k4DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(k4DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p1080Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1080DownloadLink != null) {
                    if (p1080DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p1080DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p1080DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p1080DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p1080DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p1440Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p1440DownloadLink != null) {
                    if (p1440DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p1440DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p1440DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p1440DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p1440DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p720Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p720DownloadLink != null) {
                    if (p720DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p720DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p720DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p720DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p720DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p480Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p480DownloadLink != null) {
                    if (p480DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p480DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p480DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p480DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p480DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p360Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (p360DownloadLink != null) {
                    if (p360DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p360DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p360DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p360DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p360DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        p240Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p240DownloadLink != null) {
                    if (p240DownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "video", p240DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                p240DownloadLink = stream;
                                decryptAudio();
                            }

                            @Override
                            public void secStream(String stream) {
                                System.out.println("------- secStream: " + stream);
                                aacDownloadLink = stream;
                                startDownload(videoObject, "video", p240DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p240DownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });

        //Audio Part
        largeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (largeDownloadLink != null) {
                    if (largeDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", largeDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                largeDownloadLink = stream;
                                startDownload(videoObject, "audio", largeDownloadLink, aacDownloadLink);

                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(largeDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        mediumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediumDownloadLink != null) {
                    if (mediumDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", mediumDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                mediumDownloadLink = stream;
                                startDownload(videoObject, "audio", mediumDownloadLink, aacDownloadLink);

                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(mediumDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        smallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smallDownloadLink != null) {
                    if (smallDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", smallDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                smallDownloadLink = stream;
                                startDownload(videoObject, "audio", smallDownloadLink, aacDownloadLink);

                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(smallDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });
        //
        tinyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDownloadLink != null) {
                    if (tinyDownloadLink.startsWith("https://")) {
                        startDownload(videoObject, "audio", tinyDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(videoObject.getVideoId(), (FragmentActivity) activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                tinyDownloadLink = stream;
                                startDownload(videoObject, "audio", tinyDownloadLink, aacDownloadLink);

                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(tinyDownloadLink, "F");
                    }
                    downloadWindow.cancel();
                }
            }
        });

    }

    private void startDownload(VideoObject videoObject, String type, String videoLink, String audioLink) {
        Intent i = new Intent(activity, testService.class);
        i.putExtra("video_id", videoObject.getVideoId());

        if (type.equals("video")) {
            i.putExtra("download_type", "video");
        } else {
            i.putExtra("download_type", "audio");
        }

        i.putExtra("download_video_link", videoLink);
        i.putExtra("download_audio_link", audioLink);
        i.putExtra("file_name", videoObject.getTitle());
        i.putExtra("thumbnail", videoObject.getThumbnail());
        i.putExtra("by", videoObject.getBy());
        i.putExtra("time_line", videoObject.getTime());
        activity.startService(i);
    }

    private void decryptAudio() {
        decryptCipher.decrypt(aacDownloadLink, "S");
    }
}