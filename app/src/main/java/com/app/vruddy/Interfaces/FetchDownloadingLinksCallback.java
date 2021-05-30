package com.app.vruddy.Interfaces;

import com.app.vruddy.Objects.VideoStreamObject;

import java.util.List;

public interface FetchDownloadingLinksCallback {
    void callback(List<VideoStreamObject> videoStreamObjectList);
}
