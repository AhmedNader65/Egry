package mrerror.egry;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    TinyDB tiny;
    //function which returns the unique resource ID.

    @Override
    protected void onStart() {
        super.onStart();
    }

    startserv startserv;
    TextView timer;
    TextView distance;
    ArrayList<File> mySongs;
    ImageButton imageButton;
    ImageButton imageButton2;
    ImageButton imageButton3;
    TextView songView;
    int[] m = {R.raw.teseen,R.raw.nesrelhorya, R.raw.fekra};
    String[] n = {"90 Minutes - UA07", "Nesr El horya - UA07", "Fekra w gam3etna - UA07"};

    int pos = 0;
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();
    boolean appmusic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tiny = new TinyDB(this);
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
        setContentView(R.layout.activity_fullscreen);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/aokay.ttf");
        distance = (TextView) findViewById(R.id.dist_val);
        distance.setTypeface(type);
        timer = (TextView) findViewById(R.id.ty);
        startserv = new startserv(this, getWindow().getDecorView().getRootView());
        startserv.startStepService();
        startserv.bindStepService();
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.dist_val);

        songView = (TextView) findViewById(R.id.songName);

        mySongs = findSongs(Environment.getExternalStorageDirectory());
        imageButton = (ImageButton) findViewById(R.id.start);
        imageButton2 = (ImageButton) findViewById(R.id.next);
        imageButton3 = (ImageButton) findViewById(R.id.previous);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    imageButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

                } else {
                    imageButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    mp.start();
                }
            }
        });
        if (!appmusic) {
            imageButton2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.stop();
                        mp.reset();
                        pos = (pos + 1) % mySongs.size();
                        Uri uri = Uri.parse(mySongs.get(pos).toString());
                        songView.setText(mySongs.get(pos).getName().toString());
                        try {
                            mp.setDataSource(getApplication(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    mp.start();
                }
            });
            imageButton3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.stop();
                        mp.reset();
                        pos = (pos - 1 < 0) ? mySongs.size() - 1 : pos - 1;
                        Uri uri = Uri.parse(mySongs.get(pos).toString());
                        songView.setText(mySongs.get(pos).getName().toString());
                        try {
                            mp.setDataSource(getApplication(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    mp.start();
                }
            });
            Collections.shuffle(mySongs);
            Uri uri;
                uri = Uri.parse(mySongs.get(pos).toString());
                songView.setText(mySongs.get(pos).getName().toString());
                try {
                    mp.setDataSource(this, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("hi", "completed");
                    mp.stop();
                    Uri uri;
                        mp.reset();
                        pos = (pos + 1) % mySongs.size();
                        uri = Uri.parse(mySongs.get(pos).toString());
                        songView.setText(mySongs.get(pos).getName().toString());
                        try {
                            mp.setDataSource(getApplication(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    mp.start();
                }
            });
        } else {
            imageButton2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   playNext();
                }
            });
            imageButton3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.reset();
                    pos = (pos - 1 < 0) ? m.length - 1 : pos - 1;
                    songView.setText(String.valueOf(m[pos]));
                    songView.setText(n[pos]);
                    mp = MediaPlayer.create(getApplicationContext(), m[pos]);
                    mp.start();
                }
            });
            Uri uri;
            songView.setText(String.valueOf(m[pos]));
            mp = MediaPlayer.create(getApplicationContext(), m[pos]);
            songView.setText(n[pos]);


            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("hi2", "completed");
                    playNext();

                }
            });
        }
        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    int minutes;
    public Runnable updateTimerMethod = new Runnable() {

        public void run() {
            if (!pause) {
                timeInMillies = SystemClock.uptimeMillis() - startTime;
                finalTime = timeSwap + timeInMillies;

                int seconds = (int) (finalTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                int milliseconds = (int) (finalTime % 1000);
                String s = "" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
                timer.setText(s);
            }
            myHandler.postDelayed(this, 0);


        }

    };
    boolean pause = false;
    public void playNext(){
        mp.reset();
        pos = (pos + 1) % m.length;
        songView.setText(String.valueOf(m[pos]));
        songView.setText(n[pos]);
        mp = MediaPlayer.create(getApplicationContext(), m[pos]);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("hi2", "completed");
                playNext();

            }
        });
    }
    public void pause(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdialog);
        dialog.setTitle("PAUSED !!");
        mp.pause();
        // set the custom dialog components - text, image and button

        Button dialogButton = (Button) dialog.findViewById(R.id.Resume);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.Exit);
        dialogButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float dis = (float) Float.valueOf(distance.getText().toString());
                tiny.putFloat("dis", (dis + tiny.getFloat("dis")));
                //int time =(int) Integer.valueOf(timer.getText().toString());
                tiny.putInt("time", (minutes + tiny.getInt("time")));
                int cal = (int) tiny.getInt("cal1");
                tiny.putInt("cal2", (cal + tiny.getInt("cal2")));
                int steps = (int) tiny.getInt("steps1");
                tiny.putInt("steps2", (steps + tiny.getInt("steps2")));
                tiny.putBoolean("finished", true);
                mp.stop();
                finish();
            }
        });
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startserv.startStepService();
                startserv.bindStepService();
                pause = false;
                dialog.dismiss();
                mp.start();

            }
        });
        dialog.show();
        startserv.stopStepService();
        startserv.unbindStepService();
        pause = true;

    }

    ////////////// get music
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File simpleFile : files) {
            if (simpleFile.isDirectory()) {
                al.addAll(findSongs(simpleFile));
            } else {
                if (simpleFile.getName().endsWith(".mp3")) {
                    al.add(simpleFile);
                }
            }
        }
        return al;
    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */

}
