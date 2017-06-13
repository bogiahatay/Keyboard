package com.vinsofts.keyborad.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vinsofts.keyborad.R;

public class ItemGift extends FrameLayout {

    private int resImageId;

    private String name;

    private ValueAnimator animator;

    private ImageView ivGift;

    private TextView tvName;

    private Animation animGift;

    public ItemGift(Context context, int resImageId, String name) {
        super(context);
        this.resImageId = resImageId;
        this.name = name;
        View view = inflate(context, R.layout.item_animation_gift, this);
        ivGift = (ImageView) view.findViewById(R.id.ivGift);
        tvName = (TextView) view.findViewById(R.id.tvName);
        init();
    }

    private void init() {

        int maxDeltaY = getResources().getDimensionPixelSize(R.dimen.dimen_7x);
        int deltaY = (int) (Math.random() * maxDeltaY);

        LayoutParams params =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                        , Gravity.TOP);

        params.setMargins(0, deltaY, 0, 0);
        setLayoutParams(params);

        setVisibility(INVISIBLE);

        ivGift.setImageResource(resImageId);
        tvName.setText(name);

        new Handler().post(() -> runAnim());
    }

    private void runAnim() {
        int deltaY = getResources().getDimensionPixelSize(R.dimen.dimen_3x);

        animator = ValueAnimator.ofFloat(0, deltaY);
        animator.addUpdateListener(valueAnimator -> setTranslationY((float) valueAnimator.getAnimatedValue()));
        animator.setDuration(2000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setCurrentPlayTime((long) (Math.random() * animator.getDuration()));

        animator.start();

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.gift_slide);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewParent parent = getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(ItemGift.this);
                }
                animator.end();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startAnimation(anim);

        animGift = AnimationUtils.loadAnimation(getContext(), R.anim.animation_gift);
        animGift.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivGift.startAnimation(animGift);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivGift.startAnimation(animGift);

    }
}