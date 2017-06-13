package com.vinsofts.keyborad.utils;

import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Ha on 5/22/2017.
 */

public class AnimUtils {
    public static void addAnimRCV(RecyclerView rcv) {
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        animator.setAddDuration(222);
        rcv.setItemAnimator(animator);
//
//        LandingAnimator animator = new LandingAnimator(new OvershootInterpolator(2f));
//        rcv.setItemAnimator(animator);
    }
}
