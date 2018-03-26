package com.bzh.dytt.task;


import android.text.TextUtils;
import android.util.Log;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.AppDatabase;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FetchVideoDetailTask implements Runnable {

    private static final String TAG = "FetchVideoDetailTask";

    private final CategoryMap mCategoryMap;
    private final DyttService mService;
    private VideoDetailPageParser mParser;
    private AppDatabase mDatabase;

    public FetchVideoDetailTask(CategoryMap categoryMap, AppDatabase database, DyttService service, VideoDetailPageParser parser) {
        mCategoryMap = categoryMap;
        mService = service;
        mParser = parser;
        mDatabase = database;
    }

    @Override
    public void run() {
        try {
            Response<ResponseBody> response = mService.getVideoDetailNew(mCategoryMap.getLink()).execute();
            ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);

            if (apiResponse.isSuccessful()) {
                VideoDetail videoDetail = mParser.parseVideoDetail(new String(apiResponse.body.bytes(), "GB2312"));
                if (TextUtils.isEmpty(videoDetail.getName())) {
                    videoDetail.setValidVideoItem(false);
                } else {
                    videoDetail.setValidVideoItem(true);
                }
                videoDetail.setDetailLink(mCategoryMap.getLink());
                mDatabase.videoDetailDAO().updateVideoDetail(videoDetail);
                mCategoryMap.setIsParsed(true);
                mDatabase.categoryMapDAO().updateCategory(mCategoryMap);
            }
        } catch (IOException e) {
            Log.e("FetchVideoDetailTask", "Something wrong when fetch video detail " + e.getMessage());
        }
    }
}