package com.example.gameproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false; // default game is playing and not over
    private int screenX, screenY, score = 0; // default 0 score
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private List<Bullet> bullets;
    private Flight flight;
    private GameActivity activity;
    private Background background1, background2;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        this.screenX = screenX; // get the screen size
        this.screenY = screenY;
        screenRatioX = 1920f / screenX; //get the screen ratio
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());  // making a moving sky background with 2 background
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources()); // creating a new flight(airplane) class

        bullets = new ArrayList<>(); // creating bullet class

        background2.x = screenX; //set the x to be screen x to make sure the x is the same

        paint = new Paint(); //creating paint class for the canvas
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[4]; // creating bird by maximum of 5 birds

        for (int i = 0;i < 4;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;  //create birds

        }

        random = new Random(); // randomize

    }

    @Override
    public void run() {

        while (isPlaying) { //check is it playing

            update (); // update the screen
            draw (); // the game process
            sleep (); // end game

        }

    }

    private void update () {

        background1.x -= 10 * screenRatioX; // prevent the crash if the phone screen is 1920*1080
        background2.x -= 10 * screenRatioX; // prevent the crash if the phone screen is 1920*1080

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (flight.isGoingUp) //if the plane is going up
            flight.y -= 30 * screenRatioY;  // the plane is going up
        else
            flight.y += 30 * screenRatioY; // the plane will get down

        if (flight.y < 0) {
            flight.y = 0;}

        if (flight.y >= screenY - flight.height){
            flight.y = screenY - flight.height;}

        List<Bullet> trash = new ArrayList<>(); // create an array of bullet

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX; // bullet fly out of the plane

            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(), // if the bullet hit the rectangle box of the birds
                        bullet.getCollisionShape())) {

                    score++; //score +1
                    bird.x = -500; // and the birds will disappear
                    bullet.x = screenX + 500; // and the bullet will disappear
                    bird.wasShot = true; // set the bird wasShot boolean to true to nex step

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet); //remove the bullet when bullet is trash

        for (Bird bird : birds) {

            bird.x -= bird.speed; //the bird will fly toward left

            if (bird.x + bird.width < 0) { //when the bird arrive left

                if (!bird.wasShot) { //if the bird is not getting shoot
                    isGameOver = true; // game over
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound); //randomize the speed of the birds

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX); //let the speed propotional to the screen size

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height); // randomize the height of the birds

                bird.wasShot = false; //default not shot
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) { // if the birds hit the plane

                isGameOver = true; //game over
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint); //the background moving
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint); // the birds is moving

            canvas.drawText(score + "", screenX / 2f, 164, paint); // show the score

            if (isGameOver) {
                isPlaying = false; // set isPlaying to false
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint); // stop the plane
                getHolder().unlockCanvasAndPost(canvas); // hold the background
                saveIfHighScore(); // save if you get the high score
                waitBeforeExiting (); // pause before exit
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);  // hold the bullet

            getHolder().unlockCanvasAndPost(canvas);// hold the background

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000); //wait 3 sec
            activity.startActivity(new Intent(activity, MainActivity.class));//go back to the main menu
            activity.finish(); // and finish game view class
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {// if the score you get > high score
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score); // the highscore will be updated
            editor.apply();
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);// when you get out of the app to the phone screen, it will stop
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true; // when to users comeback from the phone menu and they are playing before
        thread = new Thread(this); // the game will resume
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false; //when you get out of the app to the phone screen
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) { //get the motion
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) { // when you touch the left side of the screen
                    flight.isGoingUp = true; // the plane goes up
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false; // when you are not touching the left side of the screen, the plane will get down
                if (event.getX() > screenX / 2) //when you touch the right side of the screen
                    flight.toShoot++; // the plane shoot the bullet
                break;
        }

        return true;
    }

    public void newBullet() {

        Bullet bullet = new Bullet(getResources()); // get the resource of the bullet
        bullet.x = flight.x + flight.width; // the bullet will shoot from the right side of the plane
        bullet.y = flight.y + (flight.height / 2); // the bullet will spawn at the center of the plane
        bullets.add(bullet);

    }
}
