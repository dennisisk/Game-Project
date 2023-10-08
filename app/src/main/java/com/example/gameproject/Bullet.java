package com.example.gameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.gameproject.GameView.screenRatioX;
import static com.example.gameproject.GameView.screenRatioY;

public class Bullet {

    int x, y, width, height;
    Bitmap bullet;

    Bullet (Resources res) {

        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        width = bullet.getWidth(); //get the width and the height of the bullet
        height = bullet.getHeight();

        width /= 4; // divided by 4 as 1:4 ratio
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY); //get the scales height and width

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false); //create the scaled bitmap

    }

    Rect getCollisionShape () { return new Rect(x, y, x + width, y + height); }//set the collision Shape of the bullet

}
