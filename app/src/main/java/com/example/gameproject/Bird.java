package com.example.gameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.gameproject.GameView.screenRatioX;
import static com.example.gameproject.GameView.screenRatioY;

public class Bird {

    public int speed = 20; //set the default speed of the bird be 20
    public boolean wasShot = true; //default true
    int x = 0, y, width, height, birdCounter = 1; //default bird counter = 1
    Bitmap bird1, bird2, bird3, bird4;

    Bird (Resources res) {

        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4);

        width = bird1.getWidth();
        height = bird1.getHeight(); //get the original height and width of the image

        width /= 6; // divided by 6 as 1:6 ratio
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY); //get the scales height and width

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false); //create the scaled bitmap and make them like an animation
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);

        y = -height;
    }

    Bitmap getBird () { //

        if (birdCounter == 1) {
            birdCounter++;
            return bird1; //if bird counter = 1, the image will be bird1
        }

        if (birdCounter == 2) {
            birdCounter++;
            return bird2; //if bird counter = 2, the image will be bird2
        }

        if (birdCounter == 3) {
            birdCounter++;
            return bird3; //if bird counter = 3, the image will be bird3
        }

        birdCounter = 1; // set the bird counter to 1

        return bird4; //and show the bird 4 image
    }

    Rect getCollisionShape () { return new Rect(x, y, x + width, y + height); } //set the collision Shape of the bird

}
