package com.gaox.rxjavatest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.gaox.rxjavatest.Bean.CityStore;
import com.gaox.rxjavatest.Bean.NumberTest;
import com.gaox.rxjavatest.Bean.Salesman;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@SuppressLint("CheckResult")
public class Opera1Activity extends AppCompatActivity {

    private static final String TAG = "Opera1Activity";
    private List<Integer> mList = Arrays.asList(1, 2, 3, 4, 5);
    private Integer[] mArr = {1, 2, 3,};
    private List<CityStore> cityStores = new ArrayList<>();
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opera_one);
        testScan();


    }

    private void testScan() {
        Flowable.fromIterable(mList).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.d(TAG, "integer:" + integer);
                Log.d(TAG, "integer2:" + integer2);
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer.toString());
            }
        });
//        1
//        integer:1
//        integer2:2
//        3
//        integer:3
//        integer2:3
//        6
//        integer:6
//        integer2:4
//        10
//        integer:10
//        integer2:5
//        15
    }

    private void testWindow() {
        Flowable.fromIterable(mList).window(3)
                .subscribe(new Consumer<Flowable<Integer>>() {
                    @Override
                    public void accept(Flowable<Integer> integerFlowable) throws Exception {
                        integerFlowable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, integer.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "onError");

                            }
                        }, new Action() {

                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "onComplete");
                            }
                        });
                    }
                });
        //        1
        //        2
        //        3
        //        onComplete
        //        4
        //        5
        //        onComplete

    }

    private void testBuffer() {
        Flowable.fromIterable(mList).buffer(3)
                .subscribe(integers -> Log.d(TAG, integers.toString()));
        //        [1, 2, 3]
        //        [4, 5]
        Flowable.fromIterable(mList).buffer(3, 2)
                .subscribe(integers -> Log.d(TAG, integers.toString()));
        //    [1, 2, 3]
        //    [3, 4, 5]
        //    [5]
    }

    private void testGroupBy() {
        getData();
        Flowable.fromIterable(cityStores).groupBy(new Function<CityStore, Boolean>() {
            @Override
            public Boolean apply(CityStore cityStore) throws Exception {
                return cityStore.getCityCtoreId() == 1;
            }
        }).subscribe(new Consumer<GroupedFlowable<Boolean, CityStore>>() {
            @Override
            public void accept(GroupedFlowable<Boolean, CityStore> objectCityStoreGroupedFlowable) throws Exception {
                Boolean key = objectCityStoreGroupedFlowable.getKey();
                Log.d(TAG, key + "");
                objectCityStoreGroupedFlowable.toList().subscribe(new Consumer<List<CityStore>>() {
                    @Override
                    public void accept(List<CityStore> cityStores) throws Exception {
                        Log.d(TAG, cityStores.toString());
                    }
                });
            }
        });

    }

    private void testSwitchMap() {
        getData();
        Flowable.fromIterable(cityStores)
                .switchMap((Function<CityStore, Publisher<Salesman>>) cityStore -> {

                    int delay = 0;
                    if (1 == cityStore.getCityCtoreId()) {
                        delay = 500;
                    }
                    Log.d(TAG, "=====switchMap:" + cityStore.getCityCtoreId() + "|" + Thread.currentThread().getName());
                    return Flowable.fromIterable(cityStore.getSalesman()).delay(delay, TimeUnit.MILLISECONDS);
                })
                .subscribe(salesman -> Log.d(TAG, salesman.getCityStoreId() + "店的"
                        + salesman.getSalesManId() + "号的业绩为:"
                        + salesman.getSalesPerformance() + "元|" + Thread.currentThread().getName()));


    }

    private void startInterVal() {
        tv = findViewById(R.id.tv);
        tv.setOnClickListener(v -> startInterVal());
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(60)
                .map(aLong -> 60 - aLong)
                .doOnSubscribe(subscription -> tv.setClickable(false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    tv.setText(aLong + "s");
                });
    }

    private void textInterval() {
        Flowable.interval(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "Timer:" + aLong + "=====当前线程为:"
                        + Thread.currentThread().getName());
            }
        });
        // Timer:0=====当前线程为:RxComputationThreadPool-1
        // Timer:1=====当前线程为:RxComputationThreadPool-1
        // Timer:2=====当前线程为:RxComputationThreadPool-1
        // Timer:3=====当前线程为:RxComputationThreadPool-1
        // Timer:4=====当前线程为:RxComputationThreadPool-1
    }

    private void textTimer() {
        Flowable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "Timer:" + aLong + "=====当前线程为:"
                                + Thread.currentThread().getName());
                    }
                });
    }

    private void textRepeat() {
        Flowable.just(mList).repeat(2).subscribe(numbers -> Log.d(TAG, numbers.toString()));
    }

    private void textDefer() {
        StringBuilder deferMsg = new StringBuilder();
        deferMsg.append("张三发来贺电");
        Flowable<String> defer = Flowable.defer((Callable<Publisher<String>>) () ->
                Flowable.just(deferMsg.toString()));
        deferMsg.append(",李四发来贺电");
        defer.subscribe(s -> Log.d(TAG, s));

        //        Flowable<String> justDefer = Flowable.just(deferMsg.toString());
        //        deferMsg.append(",李四发来贺电");
        //        justDefer.subscribe(s -> Log.d(TAG, s));

    }


    private void testConcatMap() {
        getData();

        Flowable.fromIterable(cityStores)
                .concatMap((Function<CityStore, Publisher<Salesman>>) cityStore -> {

                    int delay = 0;
                    if (1 == cityStore.getCityCtoreId()) {
                        delay = 500;
                    }
                    return Flowable.fromIterable(cityStore.getSalesman()).delay(delay, TimeUnit.MILLISECONDS);
                })
                .subscribe(salesman -> Log.d(TAG, salesman.getCityStoreId() + "店的"
                        + salesman.getSalesManId() + "号的业绩为:"
                        + salesman.getSalesPerformance() + "元"));


    }


    private void testFlatMap2() {

        getData();

        Flowable.fromIterable(cityStores)
                .flatMap((Function<CityStore, Publisher<Salesman>>) cityStore -> {
                    return Flowable.fromIterable(cityStore.getSalesman());
                })
                .subscribe(salesman -> Log.d(TAG, salesman.getCityStoreId() + "店的"
                        + salesman.getSalesManId() + "号的业绩为:"
                        + salesman.getSalesPerformance() + "元"));

    }

    private void textFlatMap3() {
        //                goToRegister()
        //                        .subscribeOn(Schedulers.io())
        //                        .observeOn(AndroidSchedulers.mainThread())
        //                        .doOnNext((Consumer<RegisterBean>) registerBean -> {
        //                            //注册完成
        //                        })
        //                        .observeOn(Schedulers.io())
        //                        .flatMap((Function<RegisterBean, Publisher<LoginBean>>) registerBean -> Api.goToLogin(account, pwd))
        //                        .observeOn(AndroidSchedulers.mainThread())
        //                        .subscribe((Consumer<LoginBean>) loginBean -> {
        //                            //登录成功
        //                        });
    }

    private void testFlatMap1() {
        Flowable.just(mList).flatMap((Function<List<Integer>, Publisher<List<String>>>) mList -> {
            List<String> newList = new ArrayList<>();
            for (Integer number : mList) {
                newList.add(number + "string");
            }
            return Flowable.just(newList);
        }).subscribe((Consumer<List<String>>) newList -> Log.d(TAG, newList.toString()));
    }


    //map
    private void testMap1() {
        Flowable.just(mList).map((Function<List<Integer>, List<String>>) mList -> {
            List<String> newList = new ArrayList<>();
            for (Integer number : mList) {
                newList.add(number + "string");
            }
            return newList;
        }).subscribe((Consumer<List<String>>) newList -> Log.d(TAG, newList.toString()));
    }


    private void testMap2() {
        Flowable.just(mList).map((Function<List<Integer>, NumberTest>) mList -> {
            NumberTest numberTest = new NumberTest();
            numberTest.setNumber(mList.get(mList.size() - 1));
            return numberTest;
        }).subscribe((Consumer<NumberTest>) numberTest -> Log.d(TAG, numberTest.toString()));

    }


    private Flowable goToRegister() {
        return Flowable.just(mList);
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
