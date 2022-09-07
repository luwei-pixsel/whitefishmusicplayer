package cn.il0ve.whitefishmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button buttonFile3=(Button) findViewById(R.id.button_file3);
        Button buttonView3=(Button) findViewById(R.id.button_view3);
        Button buttonHelp3=(Button) findViewById(R.id.button_help3);

        buttonFile3.setOnClickListener(new ButtonFile3OnClickListener());
        buttonView3.setOnClickListener(new ButtonView3OnClickListener());
        buttonHelp3.setOnClickListener(new ButtonHelp3OnClickListener());
    }

    public class ButtonFile3OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
            Intent intent=new Intent(HelpActivity.this,FileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class ButtonView3OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
            Intent intent=new Intent(HelpActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class ButtonHelp3OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //
        }
    }
}