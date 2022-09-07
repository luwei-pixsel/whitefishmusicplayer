package cn.il0ve.whitefishmusicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    public static int REQUEST_CODE=11;
    public static int RESULT_CODE=22;
    //更新seekbar播放进度
    public static Timer timer;
    public static TimerTask timerTask;
    public static Handler handler;
    public String musicFilePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取音乐文件路径，
//        musicFilePath=(String)savedInstanceState.get("musicfilepath");

        //Button对象获取Button安卓组件
        Button buttonFile=(Button) findViewById(R.id.button_file);
        Button buttonView=(Button) findViewById(R.id.button_view);
        Button buttonHelp=(Button) findViewById(R.id.button_help);
        Button buttonPrevious=(Button) findViewById(R.id.button_previous);
        Button buttonNext=(Button) findViewById(R.id.button_next);
        Button buttonPlay=(Button) findViewById(R.id.button_play);
        Button buttonPause=(Button) findViewById(R.id.button_pause);

        TextView textViewLyric=(TextView) findViewById(R.id.textView_lyric);
        textViewLyric.setText(MusicPlayerService.MUSIC_FOLDER);
        TextView textViewDuration=(TextView)findViewById(R.id.textView_duration);
        textViewDuration.setText("时长");

        SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar);

        //Button对象设置点击事件的监听
        buttonFile.setOnClickListener(new ButtonFileOnClickListener());
        buttonView.setOnClickListener(new ButtonViewOnClickListener());
        buttonHelp.setOnClickListener(new ButtonHelpOnClickListener());
        buttonPrevious.setOnClickListener(new ButtonPreviousOnClickListener());
        buttonNext.setOnClickListener(new ButtonNextOnClickListener());
        buttonPlay.setOnClickListener(new ButtonPlayOnClickListener());
        buttonPause.setOnClickListener(new ButtonPauseOnClickListener());

        seekBar.setOnSeekBarChangeListener(new SeekBarOnChangeListener());


        //接收定时器发送来的消息，更新seekbar的精度
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //接收定时器消息
                Bundle bundle=msg.getData();
                int duration=bundle.getInt("duration");
                int progress=bundle.getInt("currentposition");
                //更新seekbar的进度条
                seekBar.setMax(duration);
                seekBar.setProgress(progress);
                //更新textview显示音乐时长，单位：秒
                textViewDuration.setText(""+progress/1000+"/"+duration/1000);
                Log.i("progress", String.valueOf(progress));
            }
        };

        //加载音乐
        prepareMusic();




    }


    /**
     * 加载，准备音乐
     */
    public void prepareMusic(){
        //加载音乐信息
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
//        intent.putExtra("musicfilepath",musicFilePath);
        startService(intent);
        //加载seekbar
        setSeekBarProgress();
//        //加载歌词文本
//        setTextViewLyricText(textViewLyric);
    }


    /**
     * 加载，准备音乐
     */
    public void prepareMusic(String musicFilePath){
        //加载音乐信息
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
        intent.putExtra("musicfilepath",musicFilePath);
        startService(intent);
        //加载seekbar
        setSeekBarProgress();
//        //加载歌词文本
//        setTextViewLyricText(textViewLyric);
    }

    /**
     * 更新歌词
     */
    public void setTextViewLyricText(TextView textViewLyric){
        //歌词默认显示音乐路径 MusicPlayerService.musicPath
        textViewLyric.setText(MusicPlayerService.MUSIC_FOLDER);
    }

    /**
     *  更新seekbar组件的进度
     *  更新播放音乐的播放时间
     */
    public void setSeekBarProgress(){
        //定时任务，
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                //音乐的时长和当前播放位置
                int musicDuration=MusicPlayerService.getMusicDuration();
                int musicCurrentPosition=MusicPlayerService.getMusicCurrentPostion();

                //定时要发送消息
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("duration",musicDuration);
                bundle.putInt("currentposition",musicCurrentPosition);
                Log.i("currentposition", String.valueOf(musicCurrentPosition));
                message.setData(bundle);
                MainActivity.handler.sendMessage(message);

            }
        };
        //开始定时任务
        timer.schedule(timerTask,300,500);
    }

    /**
     * 拖动条设置
     */
    public class SeekBarOnChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //拖到的进度
            int progress=seekBar.getProgress();
            //拖动条
            Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
            intent.putExtra("playing",true);
            intent.putExtra("progress",(int)((float) progress/seekBar.getMax()*100));//音乐播放进度，x%
            startService(intent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_CODE) {

            //获取音乐文件path
            musicFilePath = String.valueOf(data.getStringExtra("musicfilepath"));
            //刷新播放
//            prepareMusic(musicFilePath);
            Log.i("musicFilePath", musicFilePath);
//            Log.i("str", data.getStringExtra("str"));
            prepareMusic(musicFilePath);
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonFileOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //跳转文件选择activity选择音乐文件
            Intent intent=new Intent(MainActivity.this,FileActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent,MainActivity.REQUEST_CODE);
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonViewOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonHelpOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
            Intent intent=new Intent(MainActivity.this,HelpActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonPreviousOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonNextOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonPlayOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //播放音乐
            Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
            intent.putExtra("playing",true);
            startService(intent);
        }
    }

    /**
     * Button对象的点击事件监听
     * 处理点击事件，实现按钮点击的功能
     */
    public class ButtonPauseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //暂停音乐
            Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
            intent.putExtra("playing",false);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //播放音乐
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
        intent.putExtra("playing",true);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
        stopService(intent);
        super.onDestroy();
    }
}