package com.welightworld.fadetop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.orhanobut.logger.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heyue on 2017/12/23.
 */
@RunWith(AndroidJUnit4.class)
public class RxTest {
    @Test
    public void Test() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Vibrator vibrator = (Vibrator)appContext.getSystemService(appContext.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(2000,100) );

        Observable.just(1)
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setMessage("xxxx")
                .setPositiveButton("xx", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        Logger.i("apply"+aLong.toString());
                        return aLong;
                    }
                })
                .take(5)

                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.i("onNext"+aLong.toString());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
