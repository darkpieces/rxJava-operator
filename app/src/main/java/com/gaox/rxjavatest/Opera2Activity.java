package com.gaox.rxjavatest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.gaox.rxjavatest.Bean.Banner;
import com.gaox.rxjavatest.Bean.CityStore;
import com.gaox.rxjavatest.Bean.DistinctBean;
import com.gaox.rxjavatest.Bean.Member;
import com.gaox.rxjavatest.Bean.Salesman;
import com.gaox.rxjavatest.net.ApiService;
import com.gaox.rxjavatest.net.BaseDataResponse;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class Opera2Activity extends AppCompatActivity {

    private static final String TAG = "Opera2Activity";
    private List<Integer> mList = Arrays.asList(1, 2, 3, 4, 5);
    private Integer[] mArr = {1, 2, 3,};
    private List<CityStore> cityStores = new ArrayList<>();
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opera_two);
        testSwitch();
    }

    private void testSwitch() {

        Flowable<Flowable<Long>> flowable = Flowable.interval(500, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Flowable<Long>>() {
                    @Override
                    public Flowable<Long> apply(Long aLong) throws Exception {
                        Log.d(TAG, "====fu: "+aLong );
                        return Flowable.interval(0,200,TimeUnit.MILLISECONDS)
                                .map(new Function<Long, Long>() {
                                    @Override
                                    public Long apply(Long aLong) throws Exception {
                                        Log.d(TAG, "===zi: "+aLong );
                                        return aLong * 10;
                                    }
                                }).take(5);
                    }
                }).take(2);

        Flowable.switchOnNext(flowable)
                .subscribe(aLong -> Log.d(TAG, "onNext: SwitchOnNext  "+aLong));

//        ====fu: 0
//        ===zi: 0
//        onNext: SwitchOnNext  0
//        ===zi: 1
//        onNext: SwitchOnNext  10
//        ===zi: 2
//        onNext: SwitchOnNext  20
//        ====fu: 1
//        ===zi: 0
//        onNext: SwitchOnNext  0
//        ===zi: 1
//        onNext: SwitchOnNext  10
//        ===zi: 2
//        onNext: SwitchOnNext  20
//        ===zi: 3
//        onNext: SwitchOnNext  30
//        ===zi: 4
//        onNext: SwitchOnNext  40



    }

    private void testStartWith() {
        List<Integer> mList = Arrays.asList(1, 2, 3, 4, 5);
        Flowable.fromIterable(mList).startWith(100).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer.toString());
            }
        });
        //      100
        //      1
        //      2
        //      ...
        Flowable.fromIterable(mList).startWith(Arrays.asList(100, 200))
                .subscribe(integer -> Log.d(TAG, integer.toString()));
        //      100
        //      200
        //      1
        //      2
        //      ...

        Flowable.fromIterable(mList).startWith(Flowable.just(1000, 2000))
                .subscribe(integer -> Log.d(TAG, integer.toString()));
        //      1000
        //      2000
        //      1
        //      2
        //      ...
    }

    private void testJoin() {

        Flowable<Long> baseFlowable = Flowable.interval(1000, TimeUnit.MILLISECONDS);
        Flowable<String> flowable = Flowable.just("A", "B", "C", "D");
        baseFlowable.join(flowable, new Function<Long, Publisher<Long>>() {
            @Override
            public Publisher<Long> apply(Long aLong) throws Exception {
                Log.d(TAG, "===left:" + aLong);
                return Flowable.timer(2000, TimeUnit.MILLISECONDS);
            }
        }, new Function<String, Publisher<Long>>() {
            @Override
            public Publisher<Long> apply(String s) throws Exception {
                Log.d(TAG, "===right:" + s);
                return Flowable.timer(5000, TimeUnit.MILLISECONDS);
            }
        }, new BiFunction<Long, String, String>() {
            @Override
            public String apply(Long aLong, String s) throws Exception {
                return aLong + "----" + s;
            }
        }).subscribe(string -> Log.d(TAG, string));

        //        ===right:A
        //        ===right:B
        //        ===right:C
        //        ===right:D
        //        ==left:0
        //        0----A
        //        0----B
        //        0----C
        //        0----D
        //        ===left:1
        //        1----A
        //        1----B
        //        1----C
        //        1----D
        //        ...
        //        4----A
        //        4----B
        //        4----C
        //        4----D
        //        ===left:5
        //        ===left:6
        //        ===left:7
        //      ....


    }

    private void testCombineLates() {
        Flowable<Integer> flowable1 = Flowable.just(1, 2, 3, 4, 5);
        Flowable<String> flowable2 = Flowable.just("A", "B", "C");
        Flowable<String> flowable3 = Flowable.just("100", "200");
        Flowable.combineLatest(flowable1, flowable2, flowable3, new Function3<Integer, String, String, String>() {
            @Override
            public String apply(Integer integer, String s, String s2) throws Exception {
                return integer + ":" + s + ":" + s2;
            }
        }).subscribe(string -> Log.d(TAG, string));
        //        5,C,100
        //        5:C:200
    }


    private void textMergeAndZip() {

        Flowable.merge(Flowable.just("1"), Flowable.just("A", "B", "C"))
                .subscribe(s -> {
                    Log.d(TAG, s);
                    //1
                    //A
                    //B
                    //C
                });
        Flowable.zip(Flowable.just("1"), Flowable.just("A", "B", "C"),
                (s1, s2) -> s1 + "-----" + s2).subscribe(s -> Log.d(TAG, s));
        //1----A
    }

    private void testMerge() {
        Flowable<BaseDataResponse<List<Banner>>> bannerFlowable = ApiService.getbanner();
        Flowable<BaseDataResponse<List<Activity>>> activityFlowable = ApiService.getActivityInfo();
        Flowable<BaseDataResponse<Member>> memberFlowable = ApiService.getMemberInfo();
        Flowable.merge(bannerFlowable, activityFlowable, memberFlowable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseDataResponse<? extends Object>>() {
                    @Override
                    public void accept(BaseDataResponse<?> baseDataResponse) throws Exception {
                        //baseDataResponse->banner
                        //baseDataResponse->activity
                        //baseDataResponse->member
                    }
                });

    }

    private void testZip() {

        Flowable<BaseDataResponse<List<Banner>>> bannerFlowable = ApiService.getbanner();
        Flowable<BaseDataResponse<List<Activity>>> activityFlowable = ApiService.getActivityInfo();
        Flowable<BaseDataResponse<Member>> memberFlowable = ApiService.getMemberInfo();

        Flowable.zip(bannerFlowable, activityFlowable, memberFlowable,
                (bannerResponse, activityResponse, memberResponse) -> {
                    HashMap<String, Object> map = new HashMap<>(3);
                    map.put("HOME_BANNER", bannerResponse);
                    map.put("HOME_ACTIVITY", activityResponse);
                    map.put("HOME_MEMBER", memberResponse);
                    return map;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataMap -> {
                    BaseDataResponse<List<Banner>> BannerResponse = cast(dataMap.get("HOME_BANNER"));
                    if (BannerResponse != null) {
                        List<Banner> bannerList = BannerResponse.getData();
                        //                            mView.showBanner(bannerList);
                    }
                    //                    ..
                });


    }

    public static <T> T cast(Object object) {
        return (T) object;
    }


    private void testTake() {
        Flowable.fromIterable(mList).take(2)
                .subscribe(integer -> Log.d(TAG, integer.toString()));
        //   1
        //   2
        Flowable.fromIterable(mList).takeLast(2)
                .subscribe(integer -> Log.d(TAG, integer.toString()));
        //   4
        //   5
    }

    private void testSkipLast() {
        Flowable.fromIterable(mList).skipLast(2).subscribe(integer -> Log.d(TAG, integer.toString()));
        //   1
        //   2
        //   3
    }

    private void testSkip2() {
        Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .skip(3000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> Log.d(TAG, aLong.toString() + ",当前线程:"
                        + Thread.currentThread().getName()));
        //   3,当前线程:RxComputationThreadPool-2
        //   4,当前线程:RxComputationThreadPool-2
        //   5,当前线程:RxComputationThreadPool-2
        //   ...
    }

    private void testSkip() {
        Flowable.fromIterable(mList).skip(2).subscribe(integer -> Log.d(TAG, integer.toString()));
        //   3
        //   4
        //   5
    }

    private void testSample() {
        Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .sample(3000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> Log.d(TAG, aLong + ""));
        //        1
        //        4
        //        7
        //        10
        //        ..
    }

    private void testlast() {
        Flowable.fromIterable(mList)
                .last(1)
                .subscribe(integer -> Log.d(TAG, integer + ""));
        //  5
    }

    private void testFirst() {
        Flowable.fromIterable(mList)
                .first(1)
                .subscribe(integer -> Log.d(TAG, integer + ""));
        //   1
    }

    private void testElementAt() {
        Flowable.fromIterable(mList)
                .elementAt(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });
        //   4
    }

    private void testDistinct2() {
        List<DistinctBean> distinctBeans = new ArrayList<>();
        distinctBeans.add(new DistinctBean("aaaa"));
        distinctBeans.add(new DistinctBean("bbbb"));
        distinctBeans.add(new DistinctBean("aaaa"));
        distinctBeans.add(new DistinctBean("cccc"));
        distinctBeans.add(new DistinctBean("bbbb"));
        Flowable.fromIterable(distinctBeans)
                .distinct(new Function<DistinctBean, String>() {
                    @Override
                    public String apply(DistinctBean distinctBean) throws Exception {
                        return distinctBean.str;
                    }
                })
                .subscribe(distinctBean -> Log.d(TAG, distinctBean.toString()));
        //        DistinctBean{str='aaaa'}
        //        DistinctBean{str='bbbb'}
        //        DistinctBean{str='cccc'}

    }

    private void testDistinct() {
        List<Integer> distinctList = Arrays.asList(1, 1, 3, 3, 5);
        Flowable.fromIterable(distinctList)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });

    }

    private void testDebounce() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                emitter.onNext("张三");
                Thread.sleep(299);
                emitter.onNext("李四");
                Thread.sleep(300);
                emitter.onNext("王五");
                Thread.sleep(350);
                emitter.onNext("赵六");
                Thread.sleep(250);
                emitter.onNext("费七");
                Thread.sleep(100);
                emitter.onNext("陈八");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        Log.d(TAG, str);
                    }
                });
        //         李四
        //         王五
        //         陈八

    }

    private void testFilter() {
        getData();

        Flowable.fromIterable(cityStores)
                .concatMap((Function<CityStore, Publisher<Salesman>>) cityStore -> {

                    int delay = 0;
                    if (1 == cityStore.getCityCtoreId()) {
                        delay = 500;
                    }
                    return Flowable.fromIterable(cityStore.getSalesman()).delay(delay, TimeUnit.MILLISECONDS);
                })
                .filter(salesman -> salesman.getCityStoreId() == 2)
                .subscribe(salesman -> Log.d(TAG, salesman.getCityStoreId() + "店的"
                        + salesman.getSalesManId() + "号的业绩为:"
                        + salesman.getSalesPerformance() + "元"));

        //    2店的0号的业绩为:60元
        //    2店的1号的业绩为:27元
        //    2店的2号的业绩为:33元
    }

    private void getData() {
        for (int i = 0; i < 3; i++) {
            CityStore cityStore = new CityStore();
            List<Salesman> salesmen = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Salesman salesman = new Salesman();
                salesman.setCityStoreId(i + 1);
                salesman.setSalesManId(j);
                salesman.setSalesPerformance(new Random().nextInt(100) + i);
                salesmen.add(salesman);
            }
            cityStore.setCityCtoreId(i + 1);
            cityStore.setSalesman(salesmen);
            cityStores.add(cityStore);
        }
    }

}
