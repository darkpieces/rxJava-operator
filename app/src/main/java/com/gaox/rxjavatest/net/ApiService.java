package com.gaox.rxjavatest.net;

import android.app.Activity;

import com.gaox.rxjavatest.Bean.Banner;
import com.gaox.rxjavatest.Bean.Member;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author: gaox
 * @date: 2018/11/05 14:50
 *
 */
public class ApiService {

    public static Flowable<BaseDataResponse<List<Banner>>> getbanner() {
        //retrofit+rxjava 请求就可以
        return null;
    }

    public static Flowable<BaseDataResponse<List<Activity>>> getActivityInfo() {
        return null;
    }

    public static Flowable<BaseDataResponse<Member>> getMemberInfo() {
        return null;
    }
}
