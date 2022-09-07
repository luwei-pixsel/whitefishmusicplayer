package cn.il0ve.whitefishmusicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicPlayerService extends Service {

    private final static String TAG=MusicPlayerService.class.getSimpleName();
    //音乐播放类的实例
    private static MediaPlayer mediaPlayer;
    //音乐存放在手机中的文件夹路径
    public final static String MUSIC_FOLDER= "DIRECTORY_MUSIC";
    public String musicFilePath;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"OnCreate()...");
//      创建音乐播放实例
        mediaPlayer=MediaPlayer.create(this,R.raw.test);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"OnStart()...");
    }

    /**
     * 播放，拖动条播放，暂停
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"OnStartCommand()...");
        //播放
        boolean playing=intent.getBooleanExtra("playing",false);
        //进度条播放
        int progress=intent.getIntExtra("progress",0);//播放进度，x%
        //打开音乐文件path播放
        String musicFilePath=intent.getStringExtra("musicfilepath");

        if(playing) {//播放，进度条播放
            if(progress==0){//播放
                musicPlay();
            }else{//进度条播放
                musicSeekPlay(progress);
            }
        }else{//暂停
            if(mediaPlayer.isPlaying()){
                musicPause();
            }
        }

        if(musicFilePath!=null){//获取了音乐文件path
            musicFilePlay(musicFilePath);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
        Log.d(TAG,"OnDestroy ()...");
    }


    public void musicFilePlay(String musicFilePath){
        //
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(musicFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void musicPlay(){
        //音乐播放
        mediaPlayer.start();
    }

    public void musicSeekPlay(int progress){
        //音乐定时长播放
        int toPosition=(int)((float)mediaPlayer.getDuration()*progress/100);
        mediaPlayer.seekTo(toPosition);
        mediaPlayer.start();
    }

    public void musicPause(){
        //音乐播放暂停
        mediaPlayer.pause();
    }

    /**
     *  获取音乐时长，单位毫秒
     * @return
     */
    public static int getMusicDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 获取音乐播放的当前播放位置，单位毫秒
     * @return
     */
    public static int getMusicCurrentPostion(){
        return mediaPlayer.getCurrentPosition();
    }

}