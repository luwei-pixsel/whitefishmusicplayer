package cn.il0ve.whitefishmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {

    //音乐存放文件夹路径
    public String fileFolder;
    public String[] fileStringList;
    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        fileFolder=getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();

        //获取按钮组件
        Button buttonFile2=(Button)findViewById(R.id.button_file2);
        Button buttonView2=(Button)findViewById(R.id.button_view2);
        Button buttonConfirmPath=(Button)findViewById(R.id.button_confirm_path);
        Button buttonCancelPath=(Button)findViewById(R.id.button_cancel_path);

        ListView listView=(ListView)findViewById(R.id.listView);

        TextView textViewPath=(TextView) findViewById(R.id.textView_path);
        textViewPath.setText(String.valueOf(fileFolder));

        //填写清单数据
        fileStringList=getMusicFileName();
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,fileStringList));
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //添加按钮点击事件
        buttonFile2.setOnClickListener(new ButtonFiel2OnClickListener());
        buttonView2.setOnClickListener(new ButtonView2OnClickListener());
        buttonConfirmPath.setOnClickListener(new ButtonConfirmPathOnClickListener());
        buttonCancelPath.setOnClickListener(new ButtonCancelPathOnClickListener());

        listView.setOnItemClickListener(new ListViewItemOnClickListener());

    }

    /**
     * Music文件夹中获取音乐文件名称
     * @return MP3文件的文件名
     */
    public String[] getMusicFileName() {
        ArrayList<String> fileArray=new ArrayList<String>();
        //Music文件夹不存在，创建文件夹
        File file = new File(fileFolder);
        if(file.isDirectory()) {
//            new File(file.getPath()+File.separator+"testFile").mkdir();
            //获取文件夹中的文件
            File[] files = file.listFiles();
            if(fileArray!=null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName=files[i].getName();
                    Log.i("fileName",fileName );
                    if(fileName.endsWith("mp3")){
                        fileArray.add(fileName);
                    }
                }
            }
        }

        String[] filesString=new String[fileArray.size()];
        for(int i=0;i<fileArray.size();i++){
            filesString[i]=fileArray.get(i);
        }
        return filesString;
    }

    public class ListViewItemOnClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //选择音乐，准备播放
            Toast.makeText(FileActivity.this,"您选择了"+i,Toast.LENGTH_SHORT).show();
            index=i;
        }
    }

    public class ButtonFiel2OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
        }
    }
    public class ButtonView2OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
            finish();
        }
    }
    public class ButtonConfirmPathOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //播放选择的音乐 音乐文件名fileStringList[index] 路径FileFolder.
            String musicFilePath=fileFolder.toString()+File.separator+fileStringList[index];
//
            setResult(MainActivity.RESULT_CODE,new Intent().putExtra("musicfilepath",musicFilePath));
            Log.i("musicFilePathFromFile",musicFilePath);
//            startActivity(intent);
            finish();
        }
    }
    public class ButtonCancelPathOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            finish();
        }
    }
}