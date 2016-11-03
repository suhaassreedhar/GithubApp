package com.example.githubapp.utils;

import android.content.ActivityNotFoundException;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



public class TextUtil {
    private static final String TAG = TextUtil.class.getName();

    public static final int CONTENT_READY = 0;
    public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";

    public static final LinkMovementMethod CHECKING_LINK_METHOD = new LinkMovementMethod() {
        @Override
        public boolean onTouchEvent(@NonNull TextView widget,
                                    @NonNull Spannable buffer, @NonNull MotionEvent event) {
            try {
                return super.onTouchEvent(widget, buffer, event);
            } catch (ActivityNotFoundException e) {
                return true;
            }
        }
    };

    public static final String timeConverter(String time) {
        return time.substring(0, 4) + "/" + time.substring(5, 7) + "/" + time.substring(8, 10);
    }

    public static void showReadMe(final TextView textView, final String content, final Html.ImageGetter imageGetter) {
        Observable.just(content)
                .map(new Func1<String, Spanned>() {
                    @Override
                    public Spanned call(String s) {
                        s = HtmlUtil.format(s);
                        Spanned spanned = Html.fromHtml(s, imageGetter,
                                HtmlUtil.getTagHandler());
                        return spanned;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Spanned>() {
                    @Override
                    public void call(Spanned spanned) {
                        textView.setText(spanned);
                        textView.setMovementMethod(CHECKING_LINK_METHOD);
                    }
                });
    }
}
