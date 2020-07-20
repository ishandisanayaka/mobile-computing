package net.progresstransformer.android.viewData.AudioPlayer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import net.progresstransformer.android.R;
import net.progresstransformer.android.viewData.Database.DBHelper;
import net.progresstransformer.android.viewData.VideoLoder.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;


public class AudioPlayer extends AppCompatActivity {

    static MediaPlayer mp;//assigning memory loc once or else multiple songs will play at once
    int position;
    SeekBar sb;
    //ArrayList<File> mySongs;
    Thread updateSeekBar;
    Button pause, next, previous;
    TextView songNameText;
    private DBHelper dbHelper;
    private Uri urlOfAudio;
    int currentPosition;
    private int lastPosition;
    private boolean isPause;

    String sname;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();
        position = intent.getIntExtra("uri", 0);
        urlOfAudio = Uri.fromFile(Constant.allaudioList.get(position));
        dbHelper = new DBHelper(this);

        songNameText = (TextView) findViewById(R.id.txtSongLabel);

        ArrayList<HashMap<String, String>> progressAttay1 = dbHelper.getAudioData(String.valueOf(urlOfAudio));
        //int lastPosition1=Integer.parseInt(progressAttay1.get(0).get("name"));
        //Log.e("aa", String.valueOf(progressAttay1.isEmpty()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start the audio at the begining?");
        builder.setCancelable(false);


        if (progressAttay1.isEmpty()) {
            lastPosition = 0;
            dbHelper.insertAudioData(Constant.allMediaList.get(position).getName(), String.valueOf(urlOfAudio), String.valueOf(lastPosition));
            //Constant.allSendToDB.add(String.valueOf(urlOfVideo));
        } else {
//            ArrayList<HashMap<String, String>> progressAttay=dbHelper.getAudioData(String.valueOf(urlOfAudio));
//            lastPosition=Integer.parseInt(progressAttay.get(0).get("progress"));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lastPosition = 0;
                    String urlOfVideoString = String.valueOf(urlOfAudio);
                    dbHelper.updateAudioProgress(String.valueOf(lastPosition), urlOfVideoString);
                    playAudio();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<HashMap<String, String>> progressAttay = dbHelper.getAudioData(String.valueOf(urlOfAudio));
                    lastPosition = Integer.parseInt(progressAttay.get(0).get("progress"));
                    playAudio();
                    dialog.cancel();
                }
            });
            builder.show();
        }
        playAudio();
//
//       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
//      //  getSupportActionBar().setTitle("Now Playing");
//
//        pause = (Button)findViewById(R.id.pause);
//
//        previous = (Button)findViewById(R.id.previous);
//        next = (Button)findViewById(R.id.next);
//
//        sb=(SeekBar)findViewById(R.id.seekBar);
//
//
//        updateSeekBar=new Thread(){
//            @Override
//            public void run(){
//                int totalDuration = mp.getDuration();
//                //int currentPosition = 0;
//                while(lastPosition < totalDuration && !isPause){
//                    try{
//                        sleep(500);
//                        lastPosition=mp.getCurrentPosition();
//                        sb.setProgress(lastPosition);
//                        //dbHelper.updateProgress(String.valueOf(lastPosition), String.valueOf(currentPosition));
//                    }
//                    catch (InterruptedException e){
//
//                    }
//                }
//            }
//        };
//
//
//
//
//        if(mp != null){
//            mp.stop();
//            mp.release();
//        }
//        //Intent i = getIntent();
//        //Bundle b = i.getExtras();
//
//
//        //mySongs = (ArrayList) b.getParcelableArrayList("songs");
//
//        sname = Constant.allaudioList.get(position).getName();
//
//        //String SongName = i.getStringExtra("songname");
//       // songNameText.setText(SongName);
//        songNameText.setSelected(true);
//
//       // position = b.getInt("pos",0);
//        Uri u = urlOfAudio;
//
//        mp = MediaPlayer.create(getApplicationContext(),u);
//        mp.seekTo(lastPosition);
//        //mp.start();
//        sb.setMax(mp.getDuration());
//        updateSeekBar.start();
//        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
//        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//
//
//        sb.setOnSeekBarChangeListener(new
//                                              SeekBar.OnSeekBarChangeListener() {
//                                                  @Override
//                                                  public void onProgressChanged(SeekBar seekBar, int i,
//                                                                                boolean b) {
//                                                  }
//                                                  @Override
//                                                  public void onStartTrackingTouch(SeekBar seekBar) {
//                                                  }
//                                                  @Override
//                                                  public void onStopTrackingTouch(SeekBar seekBar) {
//                                                      mp.seekTo(seekBar.getProgress());
//
//                                                  }
//                                              });
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sb.setMax(mp.getDuration());
//                if(mp.isPlaying()){
//                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
//                    mp.pause();
//                    isPause=true;
//
//                }
//                else {
//                    pause.setBackgroundResource(R.drawable.pause);
//                    mp.start();
//                    isPause=false;
//                }
//            }
//        });
//
////        next.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mp.stop();
////                mp.release();
////                position=((position+1)%Constant.allaudioList.size());
////                Uri u = Uri.fromFile(Constant.allaudioList.get(position));
////                // songNameText.setText(getSongName);
////                mp = MediaPlayer.create(getApplicationContext(),u);
////
////                sname = Constant.allaudioList.get(position).getName().toString();
////                songNameText.setText(sname);
////
////                try{
////                    mp.start();
////                }catch(Exception e){}
////
////            }
////        });
////        previous.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                //songNameText.setText(getSongName);
////                mp.stop();
////                mp.release();
////
////                position=((position-1)<0)?(Constant.allaudioList.size()-1):(position-1);
////                Uri u = Uri.fromFile(Constant.allaudioList.get(position));
////                mp = MediaPlayer.create(getApplicationContext(),u);
////                sname = Constant.allaudioList.get(position).getName().toString();
////                songNameText.setText(sname);
////                mp.start();
////            }
////        });
//

    }

    private void playAudio() {

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  getSupportActionBar().setTitle("Now Playing");

        pause = (Button) findViewById(R.id.pause);
//
//        previous = (Button) findViewById(R.id.previous);
//        next = (Button) findViewById(R.id.next);

        sb = (SeekBar) findViewById(R.id.seekBar);


        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                //int currentPosition = 0;
                while (lastPosition < totalDuration && !isPause) {
                    try {
                        sleep(500);
                        lastPosition = mp.getCurrentPosition();
                        sb.setProgress(lastPosition);
                        //dbHelper.updateProgress(String.valueOf(lastPosition), String.valueOf(currentPosition));
                    } catch (InterruptedException e) {

                    }
                }
            }
        };


        if (mp != null) {
            mp.stop();
            mp.release();
        }
        //Intent i = getIntent();
        //Bundle b = i.getExtras();


        //mySongs = (ArrayList) b.getParcelableArrayList("songs");

        sname = Constant.allaudioList.get(position).getName();

        //String SongName = i.getStringExtra("songname");
        // songNameText.setText(SongName);
        songNameText.setSelected(true);

        // position = b.getInt("pos",0);
        Uri u = urlOfAudio;

        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.seekTo(lastPosition);
        //mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();
        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        sb.setOnSeekBarChangeListener(new
                                              SeekBar.OnSeekBarChangeListener() {
                                                  @Override
                                                  public void onProgressChanged(SeekBar seekBar, int i,
                                                                                boolean b) {
                                                  }

                                                  @Override
                                                  public void onStartTrackingTouch(SeekBar seekBar) {
                                                  }

                                                  @Override
                                                  public void onStopTrackingTouch(SeekBar seekBar) {
                                                      mp.seekTo(seekBar.getProgress());

                                                  }
                                              });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if (mp.isPlaying()) {
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    mp.pause();
                    isPause = true;

                } else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mp.start();
                    isPause = false;
                }
            }
        });

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mp.stop();
//                mp.release();
//                position=((position+1)%Constant.allaudioList.size());
//                Uri u = Uri.fromFile(Constant.allaudioList.get(position));
//                // songNameText.setText(getSongName);
//                mp = MediaPlayer.create(getApplicationContext(),u);
//
//                sname = Constant.allaudioList.get(position).getName().toString();
//                songNameText.setText(sname);
//
//                try{
//                    mp.start();
//                }catch(Exception e){}
//
//            }
//        });
//        previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //songNameText.setText(getSongName);
//                mp.stop();
//                mp.release();
//
//                position=((position-1)<0)?(Constant.allaudioList.size()-1):(position-1);
//                Uri u = Uri.fromFile(Constant.allaudioList.get(position));
//                mp = MediaPlayer.create(getApplicationContext(),u);
//                sname = Constant.allaudioList.get(position).getName().toString();
//                songNameText.setText(sname);
//                mp.start();
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isPause = true;
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        mp.pause();
        EventBus.getDefault().unregister(this);
        isPause = true;
        super.onStop();
    }

    @Override
    protected void onPause() {
        lastPosition = mp.getCurrentPosition();
        String urlOfVideoString = String.valueOf(urlOfAudio);
        dbHelper.updateAudioProgress(String.valueOf(lastPosition), urlOfVideoString);
        isPause = false;


        //dbHelper.insertContact(urlOfVideoString, String.valueOf(lastPosition));
        //dbHelper.updateContact(urlOfVideoString,100);
        //Cursor res=dbHelper.getCurrentPosition(String.valueOf(urlOfVideo));
        /// String a=res.getString(1);
        //  Log.e("aa", a);
        // Log.e("aa", urlOfVideoString);

        super.onPause();
    }
}