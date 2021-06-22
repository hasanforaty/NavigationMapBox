package com.hasan.foraty.mapboxtutorials.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.hasan.foraty.mapboxtutorials.R;
import com.mapbox.mapboxsdk.maps.Image;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

public class MapboxStyle {
    public static final String  MonochromeDarkSyle = "mapbox://styles/hasanforaty/ckq4zlheq5vln17n6se3g4qf2";
    private static final String DefaultStyle = "mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41";
    public static Style.Builder defaultSty() {
        return new Style.Builder().fromUri(DefaultStyle);
    }
    public static Style.Builder monochromeDarkSyle() {
        return new Style.Builder().fromUri(MonochromeDarkSyle);
    }

    public static Bitmap addPlusIcons(Resources res){
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.ic_add_location, null);
        return BitmapUtils.getBitmapFromDrawable(drawable);
    }



}
