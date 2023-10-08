package com.example.gameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.gameproject.GameView.screenRatioX;
import static com.example.gameproject.GameView.screenRatioY;

public class Flight {

    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;

    Flight (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1); // get the resource of the plane (image)
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

        width = flight1.getWidth(); //get the width and the height of the plane
        height = flight1.getHeight();

        width /= 4;// divided by 4 as 1:4 ratio
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY); //get the scales height and width

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);  //create the scaled bitmap and make them like an animation

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);// get the resource of the bullet (image)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false); //create the scaled bitmap and make them like an animation when the plane is shooting
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false); //create the scaled bitmap and make them like an animation when the plane was shot

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getFlight () {

        if (toShoot != 0) {

            if (shootCounter == 1) { //the counter mean that which frame of the animation
                shootCounter++; // counter +1
                return shoot1; //return the image
            }

            if (shootCounter == 2) { //the counter mean that which frame of the animation
                shootCounter++; // counter +1
                return shoot2; //return the image
            }

            if (shootCounter == 3) { //the counter mean that which frame of the animation
                shootCounter++; // counter +1
                return shoot3; //return the image
            }

            if (shootCounter == 4) { //the counter mean that which frame of the animation
                shootCounter++; // counter +1
                return shoot4; //return the image
            }

            shootCounter = 1; //reset the counter to 1
            toShoot--;  // toshoot = -1
            gameView.newBullet(); //shoot a new bullet

            return shoot5; // return the image
        }

        if (wingCounter == 0) {
            wingCounter++; //the counter mean that which frame of the animation
            return flight1; //return frame 1
        }
        wingCounter--;

        return flight2; //return frame2
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    } //get the collision shape of the plane

    Bitmap getDead () {
        return dead;
    } // if the collision occur mean dead and game over

}
