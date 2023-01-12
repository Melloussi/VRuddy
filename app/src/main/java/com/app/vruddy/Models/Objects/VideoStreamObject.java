package com.app.vruddy.Models.Objects;

public class VideoStreamObject {
    private String mimeType;
    private String quality;
    private String qualityLabel;
    private String downloadLink;
    private Long contentLength;
    private Long bitrate;

    public VideoStreamObject(String mimeType, String quality, String qualityLabel, String downloadLink, Long contentLength, Long bitrate) {
        this.mimeType = mimeType;
        this.quality = quality;
        this.qualityLabel = qualityLabel;
        this.downloadLink = downloadLink;
        this.contentLength = contentLength;
        this.bitrate = bitrate;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getQuality() {
        return quality;
    }

    public String getQualityLabel() {
        return qualityLabel;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public Long getBitrate() {
        return bitrate;
    }
}
