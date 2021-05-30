package com.app.vruddy.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vruddy.AsyncTask.getVideoStreamLink;
import com.app.vruddy.DecryptCipher.DecryptCipher;
import com.app.vruddy.DecryptCipher.ResultCallBack;
import com.app.vruddy.HomeActivity;
import com.app.vruddy.Interfaces.FetchDownloadingLinksCallback;
import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.Objects.VideoStreamObject;
import com.app.vruddy.R;
import com.app.vruddy.Adapter.SuggestionAdapter;
import com.app.vruddy.AsyncTask.SuggestionsRequest;
import com.app.vruddy.AsyncTask.getSearchVideo;
import com.app.vruddy.Adapter.searchVideoAdapter;
import com.app.vruddy.database.Cipher.CipherViewModel;
import com.app.vruddy.database.Favorite.Favorite;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.app.vruddy.database.Favorite.FavoriteViewModel;
import com.app.vruddy.testService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static RecyclerView recyclerView;
    private static LinearLayoutManager linearLayoutManager;
    private static SuggestionAdapter suggestionAdapter;
    private static RelativeLayout search_history_layout;
    private static LinearLayout search_suggestion_keywords_result_recycleView_layout;
    private static FrameLayout search_feed_result_layout;

    private List<VideoObject> videoObjectList = new ArrayList<>();

    private static int SEARCH_RESULT = 2;
    private static int NEW_SUGGESTIONS = 1;
    private static int SUGGESTIONS_HISTORY = 0;
    private static boolean isMoreVideoLoaded = false;
    private static boolean isSearchLayoutVisible = false;
    private static boolean testFlag = true;
    private static getSearchVideo searchVideo;
    private static HomeFragment homeFragment;
    private static SearchView searchView;
    private static HomeActivity homeActivity;
    private ImageView backImg;
    private static boolean isPermissionResponse = false;
    private static boolean isPermissionGranted = false;
    private static FragmentActivity activity;
    private static CipherViewModel cipherViewModel;
    private static DecryptCipher decryptCipher;

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
    private static boolean sleep = true;

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

    private static RelativeLayout tiny;
    private static RelativeLayout small;
    private static RelativeLayout medium;
    private static RelativeLayout large;
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

    private static FavoriteViewModel favoriteViewModel;
    private static FavoriteIndex favoriteIndex;
    private static searchVideoAdapter searchVideoAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //I Set Value true cause when back button pressed
        //I check Which Fragment is opened to close it
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setSearchFragmentOpen(true);
        activity = getActivity();
        downloadWindow = new Dialog(activity);

        cipherViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity()
                        .getApplication()))
                .get(CipherViewModel.class);

        favoriteViewModel = new ViewModelProvider(
                this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(FavoriteViewModel.class);

        favoriteIndex = FavoriteIndex.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        //There's an FrameLayout above the searchView, So I disable it
        FrameLayout frameLayout = view.findViewById(R.id.search_frame_for_click);
        frameLayout.setVisibility(View.GONE);

        //handel Back Image in Tool bar
        backImg = view.findViewById(R.id.backImg);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
            }
        });

        //Set Focus to SearchView
        searchView = view.findViewById(R.id.home_search);
        searchView.requestFocus();

        //make keyboard show up automatically
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //searchView event Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                if (keyword != null && keyword.length() != 0) {
                    //System.out.println("Keyword Entered: " + s);
                    testFlag = false;
                    layoutsVisibility(view, SEARCH_RESULT);
                    search(view, keyword);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String letter) {
                SuggestionsRequest suggestionsRequest = new SuggestionsRequest();
                System.out.println("------------ " + letter);
                //when enter new letter
                if (letter != null && letter.length() != 0) {
                    testFlag = true;
                    suggestionsRequest.doInBackground(letter);
                    //Send GET Request to get suggestions from google api
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (suggestionsRequest.getKeyword().size() != 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (testFlag) {
                                            layoutsVisibility(view, NEW_SUGGESTIONS);
                                            suggestionRecyclerView(view, suggestionsRequest.getKeyword());
                                            System.out.println("------------ " + letter);
                                        }
                                    }
                                });
                                timer.cancel();
                            }
                        }
                    }, 0, 100);
                }
                //When remove all text from search view
                if (letter.isEmpty()) {
                    for (int i = 0; i < suggestionsRequest.getKeyword().size(); i++) {
                        recyclerView.removeViewAt(i);
                    }
                    suggestionAdapter.notifyDataSetChanged();
                    suggestionsRequest.getKeyword().clear();
                    layoutsVisibility(view, SUGGESTIONS_HISTORY);
                }
                return false;
            }
        });

        favoriteData();

        return view;
    }

    private void favoriteData() {
        favoriteViewModel.getAllFavorites().observe(activity, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                favoriteIndex.passIds(favorites);
                if (searchVideoAdapter != null) {
                    searchVideoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addToFavorite(VideoObject videoObject) {
        if (favoriteIndex.Id(videoObject.getVideoId())) {
            deleteFromFavorite(videoObject);
        } else {
            favoriteViewModel.insert(new Favorite(videoObject));
        }

        Toast toast = Toast.makeText(activity, "Item Added to Favor List", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteFromFavorite(VideoObject videoObject) {
        favoriteIndex.removeId(videoObject.getVideoId());
        favoriteViewModel.deleteById(videoObject.getVideoId());

        Toast toast = Toast.makeText(activity, "Item deleted from Favorite List", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void suggestionRecyclerView(View view, ArrayList<String> keywords) {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.suggestion_keywords_recycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        suggestionAdapter = new SuggestionAdapter(keywords);
        recyclerView.setAdapter(suggestionAdapter);

        suggestionAdapter.setOnItemClickListener(new SuggestionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //System.out.println(keywords.get(position));

                searchView.setQuery(keywords.get(position), false);
                layoutsVisibility(view, SEARCH_RESULT);
                testFlag = false;
                search(view, keywords.get(position));
            }
        });
    }

    public void layoutsVisibility(View view, int layout) {
        search_history_layout = view.findViewById(R.id.search_history_layout);
        search_suggestion_keywords_result_recycleView_layout = view.findViewById(R.id.search_suggestion_keywords_result_recycleView_layout);
        search_feed_result_layout = view.findViewById(R.id.search_feed_result_layout);


        switch (layout) {
            case 0:
                search_suggestion_keywords_result_recycleView_layout.setVisibility(View.GONE);
                search_feed_result_layout.setVisibility(View.GONE);
                search_history_layout.setVisibility(View.VISIBLE);
                break;
            case 1:
                search_history_layout.setVisibility(View.GONE);
                search_feed_result_layout.setVisibility(View.GONE);
                search_suggestion_keywords_result_recycleView_layout.setVisibility(View.VISIBLE);
                break;
            case 2:
                search_suggestion_keywords_result_recycleView_layout.setVisibility(View.GONE);
                search_history_layout.setVisibility(View.GONE);
                search_feed_result_layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void search(View view, String keyword) {
        System.out.println("++++ Search  start: " + keyword);

        RecyclerView searchr_recyclerView = view.findViewById(R.id.search_result_recycle);
        ShimmerFrameLayout home_vide_shimmer =
                (ShimmerFrameLayout) view.findViewById(R.id.homeVideoShimmer);

        searchr_recyclerView.setVisibility(View.GONE);
        home_vide_shimmer.setVisibility(View.VISIBLE);
        home_vide_shimmer.startShimmer();

        System.out.println("before execute -----------------");
        searchVideo = new getSearchVideo();
        searchVideo.execute(keyword);
        System.out.println("after execute -----------------");

//        searchVideo.getVideoId().clear(); searchVideo.getThumbnailUrl().clear(); searchVideo.getVideoTitle().clear();
//        searchVideo.getViewsInfo().clear(); searchVideo.getShowTime().clear();
//        getSearchVideo.getDateInfo().clear(); searchVideo.getChannelName().clear();
//        searchVideo.getChannelImgUrl().clear(); searchVideo.getHeartIcon().clear();
//        searchVideo.getDownloadIcon().clear(); searchVideo.getBadgeIcon().clear();searchVideo.getIsLive().clear();

        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Home video data checking...");
                if (searchVideo.getVideoId().size() > 1) {
                    System.out.println("search video data arrive");
                    System.out.println("Data: -------> " + searchVideo.getVideoTitle());
                    System.out.println("id size: " + searchVideo.getVideoId().size());
                    System.out.println("title size: " + searchVideo.getVideoTitle().size());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //code
                            home_vide_shimmer.stopShimmer();
                            home_vide_shimmer.setVisibility(View.GONE);
                            searchr_recyclerView.setVisibility(View.VISIBLE);


                            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            NestedScrollView nestedScrollView = view.findViewById(R.id.search_video_nested);
                            ProgressBar spinner = view.findViewById(R.id.search_video_spinner);
                            searchr_recyclerView.setLayoutManager(linearLayoutManager);

                            //convert data to one object List
                            for (int index = 0; index < searchVideo.getVideoId().size(); index++) {
                                videoObjectList.add(new VideoObject(searchVideo.getVideoId().get(index), searchVideo.getVideoTitle().get(index), searchVideo.getChannelName().get(index)
                                        , searchVideo.getShowTime().get(index), searchVideo.getDateInfo().get(index)
                                        , searchVideo.getViewsInfo().get(index), false, searchVideo.getThumbnailUrl().get(index), searchVideo.getChannelImgUrl().get(index)));
                            }

                            searchVideoAdapter = new searchVideoAdapter(videoObjectList, searchVideo.getIsLive());
                            searchr_recyclerView.setAdapter(searchVideoAdapter);

                            searchVideoAdapter.setOnItemClickListener(new com.app.vruddy.Adapter.searchVideoAdapter.onItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //open watch activity
                                    openWatchFragment(position);

                                }

                                @Override
                                public void onDownloadClick(int position) {
                                    if (HomeActivity.isPermission) {
                                        getVideoStreamLink downloadUrls = new getVideoStreamLink(activity, new FetchDownloadingLinksCallback() {
                                            @Override
                                            public void callback(List<VideoStreamObject> videoStreamObjectList) {
                                                //
                                                System.out.println("------------ stream size: " + videoStreamObjectList.size() + " ------------");
                                                generateDownloadingOption(position, videoStreamObjectList);
                                            }
                                        });
                                        downloadUrls.execute("https://www.youtube.com/watch?v=" + getSearchVideo.getVideoId().get(position));
                                        createDownloadDialog(position);
                                    } else {
                                        Toast.makeText(getContext(), R.string.StorageMsg, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onHeartClick(int position) {
                                    addToFavorite(videoObjectList.get(position));
                                }

                            });
                            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() & isMoreVideoLoaded != true) {

                                        System.out.println("77777777777777777777777777777777777");
                                        System.out.println("I'm in the last index");
                                        System.out.println("77777777777777777777777777777777777");
                                        spinner.setVisibility(View.VISIBLE);
                                        isMoreVideoLoaded = true;

                                        int listSize = searchVideo.getChannelImgUrl().size();
                                        searchVideo.moreData();

                                        java.util.Timer loadMoreDataTimer = new Timer();
                                        loadMoreDataTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                System.out.println("note yet");
                                                if (listSize < searchVideo.getBadgeIcon().size()) {

                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // Stuff that updates the UI
                                                            System.out.println("I get data");
                                                            spinner.setVisibility(View.GONE);
                                                            //System.out.println("orignal list size: "+listSize);
                                                            //System.out.println("current list size: "+getData.getChannelImgUrl().size());
                                                            int rr = searchVideo.getChannelImgUrl().size() - listSize;
                                                            //System.out.println("how many data added: "+ rr);


                                                            //relatedVideoAdapter.notifyDataSetChanged();
                                                            //notifyItemRangeInserted(insertIndex, items.size());


                                                            searchVideoAdapter.notifyItemRangeInserted(searchVideo.getVideoId().size(), searchVideo.getVideoId().size() - listSize);
                                                            isMoreVideoLoaded = false;
                                                        }
                                                    });
                                                    loadMoreDataTimer.cancel();
                                                }
                                            }
                                        }, 0, 1000);
                                    }
                                }


                            });
                        }
                    });
//
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    private void sendData(Fragment fragment, String id, String thumbnail, String title, String views, String date, String time, String channelName, String channelPicture) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "HomeFeed");
        bundle.putString("video_id", id);
        bundle.putString("video_thumbnail", thumbnail);
        bundle.putString("video_title", title);
        bundle.putString("video_views", views);
        bundle.putString("video_date", date);
        bundle.putString("video_time", time);
        bundle.putString("channel_name", channelName);
        bundle.putString("channel_pic", channelPicture);
        fragment.setArguments(bundle);
    }

    private void openWatchFragment(int position) {
        homeFragment = new HomeFragment();
        //Open Watch Fragment
        if (homeFragment.isFirstTime()) {

            WatchFragment watchFragment = new WatchFragment();
            //Send Data to watch Fragment before added it
            sendData(watchFragment
                    , searchVideo.getVideoId().get(position), searchVideo.getThumbnailUrl().get(position)
                    , searchVideo.getVideoTitle().get(position), searchVideo.getViewsInfo().get(position)
                    , searchVideo.getDateInfo().get(position), searchVideo.getShowTime().get(position)
                    , searchVideo.getChannelName().get(position), searchVideo.getChannelImgUrl().get(position));


            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.watchFragment, watchFragment);
            fragmentTransaction.commit();
            homeFragment.setIsFirstTime(false);
        } else {
            WatchFragment watchFragment = new WatchFragment();
            //Send Data to watch Fragment before replace it
            sendData(watchFragment
                    , searchVideo.getVideoId().get(position), searchVideo.getThumbnailUrl().get(position)
                    , searchVideo.getVideoTitle().get(position), searchVideo.getViewsInfo().get(position)
                    , searchVideo.getDateInfo().get(position), searchVideo.getShowTime().get(position)
                    , searchVideo.getChannelName().get(position), searchVideo.getChannelImgUrl().get(position));


            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.watchFragment, watchFragment);
            fragmentTransaction.commit();
            ((HomeActivity) getActivity()).frameLayoutSwitch();
        }
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.searchFragment);
        if (fragment != null) {
            homeActivity.setSearchFragmentOpen(false);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
        } else {
            isPermissionGranted = true;
            //sleep = false;
            isPermissionResponse = true;
            //permissionChecker.run(execute);
        }
    }

    public void askForPermission() {
        // You can directly ask for the permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void createDownloadDialog(int position) {
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

        tiny = downloadWindow.findViewById(R.id.tiny);
        small = downloadWindow.findViewById(R.id.small);
        medium = downloadWindow.findViewById(R.id.medium);
        large = downloadWindow.findViewById(R.id.large);

        tinyMbSize = downloadWindow.findViewById(R.id.tinyMbSize);
        smallMbSize = downloadWindow.findViewById(R.id.smallMbSize);
        mediumMbSize = downloadWindow.findViewById(R.id.mediumMbSize);
        largeMbSize = downloadWindow.findViewById(R.id.largeMbSize);

        tinyBitrateSize = downloadWindow.findViewById(R.id.tinyBiterateSize);
        smallBitrateSize = downloadWindow.findViewById(R.id.smallBiterateSize);
        mediumBitrateSize = downloadWindow.findViewById(R.id.mediumBiterateSize);
        largeBitrateSize = downloadWindow.findViewById(R.id.largeBitrateSize);


        Picasso.get()
                .load(getSearchVideo.getThumbnailUrl().get(position))
                .centerCrop()
                .fit()
                .into(downloadWindowThumbnail);
        downloadWindowTitle.setText(getSearchVideo.getVideoTitle().get(position));
        downloadWindowTime.setText(getSearchVideo.getShowTime().get(position));

        downloadWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        downloadWindow.show();
    }

    public void generateDownloadingOption(int position, List<VideoStreamObject> videoStreamObjectList) {
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
                optionButtonsListener(position);
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
                    if (!isTinyFound & audioType.contains("audio")) {
                        tinyDownloadLink = downloadLink;
                        isTinyFound = true;
                        tinyMbSize.setText(String.format("%.2f", fileSize) + "MB");
                        tinyBitrateSize.setText("MP3 " + String.format("%.0f", bitRate) + "K");
                    }
                }
            }
        }


    }

    private void optionButtonsListener(int position) {
        waiting_download_links_spinner_layout.setVisibility(View.GONE);
        download_links_layout.setVisibility(View.VISIBLE);

        //Video part
        k4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on 4K Card");
                if (k4DownloadLink != null) {
                    if (k4DownloadLink.startsWith("https://")) {
                        startDownload(position, "video", k4DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", k4DownloadLink, aacDownloadLink);
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
                if (p1080DownloadLink != null) {//                                                                              MyIntentService myIntentService = new MyIntentService();
//                                                                              myIntentService.setIsConverted(false);
//
//
//                                                                              //send data to service to start download file
                    //try this part of code
                    //DownloadService ds = new DownloadService();
//                    System.out.println("download_video_link: " + p240DownloadLink);
//                    System.out.println("download_audio_link: " + aacDownloadLink);

                    //Check links if need to DecryptCipher
                    if (p1080DownloadLink.startsWith("https://")) {
                        startDownload(position, "video", p1080DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p1080DownloadLink, aacDownloadLink);
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
                        startDownload(position, "video", p1440DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p1440DownloadLink, aacDownloadLink);
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
                        startDownload(position, "video", p720DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p720DownloadLink, aacDownloadLink);
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
                        startDownload(position, "video", p480DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p480DownloadLink, aacDownloadLink);
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
                        startDownload(position, "video", p360DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p360DownloadLink, aacDownloadLink);
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
                if (p240DownloadLink != null) {//                                                                              MyIntentService myIntentService = new MyIntentService();
//                                                                              myIntentService.setIsConverted(false);
//
//
//                                                                              //send data to service to start download file
                    //try this part of code
                    //DownloadService ds = new DownloadService();
                    System.out.println("download_video_link: " + p240DownloadLink);
                    System.out.println("download_audio_link: " + aacDownloadLink);

                    //Check links if need to DecryptCipher
                    if (p240DownloadLink.startsWith("https://")) {
                        startDownload(position, "video", p240DownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
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
                                startDownload(position, "video", p240DownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        decryptCipher.decrypt(p240DownloadLink, "F");
//                        decryptCipher.decrypt(aacDownloadLink, "S");

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
                        startDownload(position, "audio", largeDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");


                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                largeDownloadLink = stream;
                                startDownload(position, "audio", largeDownloadLink, aacDownloadLink);
                            }

                            @Override
                            public void secStream(String stream) {
                            }

                            @Override
                            public void watchUrl(String url) {

                            }
                        });
                        //decryptCipher.execute(largeDownloadLink, "F");
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
                        startDownload(position, "audio", mediumDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                mediumDownloadLink = stream;
                                startDownload(position, "audio", mediumDownloadLink, aacDownloadLink);
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
                        startDownload(position, "audio", smallDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                smallDownloadLink = stream;
                                startDownload(position, "audio", smallDownloadLink, aacDownloadLink);
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
                        startDownload(position, "audio", tinyDownloadLink, aacDownloadLink);
                    } else {
                        Log.e("DownloadingPart", "Url Need To Decrypt");

                        decryptCipher = new DecryptCipher(getSearchVideo.getVideoId().get(position), activity, cipherViewModel, new ResultCallBack() {
                            @Override
                            public void firstStream(String stream) {
                                System.out.println("------- firstStream: " + stream);
                                tinyDownloadLink = stream;
                                startDownload(position, "audio", tinyDownloadLink, aacDownloadLink);
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

    private void decryptAudio() {
        decryptCipher.decrypt(aacDownloadLink, "S");
    }

    private void startDownload(int position, String type, String videoLink, String audioLink) {
        Intent i = new Intent(activity, testService.class);
        i.putExtra("video_id", getSearchVideo.getVideoId().get(position));

        if (type.equals("video")) {
            i.putExtra("download_type", "video");
        } else {
            i.putExtra("download_type", "audio");
        }

        i.putExtra("download_video_link", videoLink);
        i.putExtra("download_audio_link", audioLink);
        i.putExtra("file_name", getSearchVideo.getVideoTitle().get(position));
        i.putExtra("thumbnail", getSearchVideo.getThumbnailUrl().get(position));
        i.putExtra("by", getSearchVideo.getChannelName().get(position));
        i.putExtra("time_line", getSearchVideo.getShowTime().get(position));

        activity.startService(i);
    }

}