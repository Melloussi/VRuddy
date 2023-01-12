package com.app.vruddy.Models;

import com.app.vruddy.Models.Objects.VideoObject;

import java.util.List;

public interface TestCallBack {
    void trend(List<VideoObject> videoObjectList);
    void recommended(List<VideoObject> videoObjectList);
}
