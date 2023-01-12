package com.app.vruddy.Views.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vruddy.Views.Adapter.MusicAdapter;
import com.app.vruddy.R;
import com.app.vruddy.Data.database.Music.Music;
import com.app.vruddy.Data.database.Music.MusicViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MusicFragment extends Fragment {
    private MusicViewModel viewModel;
    private RecyclerView recyclerView;
    private List<Music>musicList;
    private MusicAdapter adapter;
    //private HomeActivity testHome;
    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //testHome = new HomeActivity();
        adapter = new MusicAdapter();
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(MusicViewModel.class);
        String img = "https://image.freepik.com/free-vector/modern-youtube-thumbnail-with-comic-art-background_1361-2738.jpg";
        Music demoData = new Music(false, "Just For test :)", "Me", "4:20", img, "xhxh");
        //viewModel.insert(demoData);

        //this part of code is for making open file work
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_fragment, container, false);
        //
        recyclerView = view.findViewById(R.id.musicRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getAllMusic().observe(this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> music) {
                adapter.updateRecycler(music);
                musicList = music;
            }
        });
        adapter.setOnOptionClickListener(new MusicAdapter.onOptionClickListener() {
            @Override
            public void onOptionClick(int position, String video_id, int action) {
                System.out.println("Clicked");
                File folder;
                switch (action) {
                    case R.id.music_play:
                        folder = new File(Environment.getExternalStorageDirectory(), "/Vital/result/"+musicList.get(position).getTitle()+".mp3");
                        try {
                            openFile(getActivity(), folder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.music_open:
                        folder = new File(Environment.getExternalStorageDirectory(), "/Vital/result/");
                        try {
                            openFile(getActivity(), folder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.music_delete:
                        System.out.println("Action: Delete Music");
                        viewModel.deleteById(video_id);
                        break;
                }
            }
        });

        return view;
    }
    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file=url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if(url.toString().contains(".aac") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".mp4")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}