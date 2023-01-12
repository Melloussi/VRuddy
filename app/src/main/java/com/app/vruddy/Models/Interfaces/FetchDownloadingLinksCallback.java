package com.app.vruddy.Models.Interfaces;

import com.app.vruddy.Models.Objects.VideoStreamObject;

import java.util.List;

public interface FetchDownloadingLinksCallback {
    void callback(List<VideoStreamObject> videoStreamObjectList);
}
