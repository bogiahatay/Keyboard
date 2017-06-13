package com.vinsofts.keyborad.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.vinsofts.keyborad.R;

import java.util.Random;

/**
 * Created by TanNV on 5/30/2017.
 */

public class HeartFlyAnimation {

    private static double getRandomDouble(double[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static void add(final RelativeLayout relativeLayout, ImageView imageView) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) relativeLayout.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        final double[] arr = {100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, -150, -145, -140, -135, -130, -125, -120, -115, -110, -105, -100};

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(5000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        imageView.setAnimation(animation);

        imageView.setScaleX(0.7f);
        imageView.setScaleY(0.7f);
        imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(5000).start();

        final ImageView view = new ImageView(relativeLayout.getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                imageView.getLayoutParams().width, imageView.getLayoutParams().height);
        params.setMargins(0, 0, convertDpToPixel(relativeLayout.getContext(), 18), 0);
//        params.addRule(RelativeLayout.ALIGN_BASELINE, imageView.getId());
        params.addRule(RelativeLayout.ALIGN_LEFT, imageView.getId());
        params.addRule(RelativeLayout.ALIGN_BOTTOM, imageView.getId());
        params.addRule(RelativeLayout.ALIGN_TOP, imageView.getId());
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        view.setImageResource(R.drawable.ic_tym);
        animator.setDuration(4000);
        final double a = getRandomDouble(arr);
        animator.addUpdateListener(animation1 -> {
            float incrementalValue = animation1.getAnimatedFraction();
            view.setTranslationX((float) (a * Math.sin(incrementalValue * Math.PI * 2)));
        });
        view.setLayoutParams(params);
        relativeLayout.addView(view);
        view.setScaleX(0.7f);
        view.setScaleY(0.7f);
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(1000).start();

        new Handler().postDelayed(() -> {
            AnimationSet animationSet = new AnimationSet(true);

            final Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setStartOffset(3000);
            fadeOut.setDuration(1000);

            final TranslateAnimation animation12 = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(height * 2 / 3));
            animation12.setDuration(5000);
            animation12.setFillAfter(true);
            animator.start();

            animationSet.addAnimation(animation12);
            animationSet.addAnimation(fadeOut);
            view.startAnimation(animationSet);
            view.animate().scaleX(0.0f).scaleY(0.0f).setDuration(2000).setStartDelay(3000).start();
        }, 1000);
    }

    public static void add(RelativeLayout relativeLayout) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) relativeLayout.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        final double[] arr = {100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, -150, -145, -140, -135, -130, -125, -120, -115, -110, -105, -100};

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(5000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        final ImageView view = new ImageView(relativeLayout.getContext());
        int w = convertDpToPixel(relativeLayout.getContext(),50);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, w);


        params.setMargins(0, 0, convertDpToPixel(relativeLayout.getContext(), 100), 40);
//        params.addRule(RelativeLayout.ALIGN_BASELINE, imageView.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        view.setImageResource(R.drawable.ic_tym);
        animator.setDuration(4000);
        final double a = getRandomDouble(arr);
        animator.addUpdateListener(animation1 -> {
            float incrementalValue = animation1.getAnimatedFraction();
            view.setTranslationX((float) (a * Math.sin(incrementalValue * Math.PI * 2)));
        });
        view.setLayoutParams(params);
        relativeLayout.addView(view);
        view.setScaleX(0.7f);
        view.setScaleY(0.7f);
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).start();

        AnimationSet animationSet = new AnimationSet(true);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(2500);
        fadeOut.setDuration(1200);

        final TranslateAnimation animation12 = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(height * 2 / 3));
        animation12.setDuration(4000);
        animation12.setFillAfter(true);
        animator.start();

        animationSet.addAnimation(animation12);
        animationSet.addAnimation(fadeOut);
        view.startAnimation(animationSet);
        view.animate().scaleX(0.0f).scaleY(0.0f).setDuration(1000).setStartDelay(3000).start();
    }

    public static void addScreenIdol(RelativeLayout relativeLayout) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) relativeLayout.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        final double[] arr = {100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, -150, -145, -140, -135, -130, -125, -120, -115, -110, -105, -100};

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(5000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        final ImageView view = new ImageView(relativeLayout.getContext());
        int w = convertDpToPixel(relativeLayout.getContext(),50);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, w);


        params.setMargins(0, 0, convertDpToPixel(relativeLayout.getContext(), 30), convertDpToPixel(relativeLayout.getContext(), 110));
//        params.addRule(RelativeLayout.ALIGN_BASELINE, imageView.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        view.setImageResource(R.drawable.ic_tym);
        animator.setDuration(4000);
        final double a = getRandomDouble(arr);
        animator.addUpdateListener(animation1 -> {
            float incrementalValue = animation1.getAnimatedFraction();
            view.setTranslationX((float) (a * Math.sin(incrementalValue * Math.PI * 2)));
        });
        view.setLayoutParams(params);
        relativeLayout.addView(view);
        view.setScaleX(0.7f);
        view.setScaleY(0.7f);
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).start();

        AnimationSet animationSet = new AnimationSet(true);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(2500);
        fadeOut.setDuration(1200);

        final TranslateAnimation animation12 = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(height * 2 / 3));
        animation12.setDuration(4000);
        animation12.setFillAfter(true);
        animator.start();

        animationSet.addAnimation(animation12);
        animationSet.addAnimation(fadeOut);
        view.startAnimation(animationSet);
        view.animate().scaleX(0.0f).scaleY(0.0f).setDuration(1000).setStartDelay(3000).start();
    }

    private static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
