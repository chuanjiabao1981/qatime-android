package cn.qatime.player.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import cn.qatime.player.interpolator.DecelerateAccelerateInterpolator;

public class AnimationHelper {
    /**
     * 创建平移动画
     */
    public static Animation createTranslateAnim(Context context, int fromX, int toX) {
        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        //自动计算时间
        long duration = (long) (Math.abs(toX - fromX) * 1.0f / ScreenUtils.getScreenWidth(context) * 4000);
        tlAnim.setDuration(duration);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setFillAfter(true);

        return tlAnim;
    }
}
