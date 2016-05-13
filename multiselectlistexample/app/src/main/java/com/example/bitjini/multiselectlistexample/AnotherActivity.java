package com.example.bitjini.multiselectlistexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {

    Button back;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        TextView textView=(TextView)findViewById(R.id.name);
        String str=getIntent().getExtras().getString("name");
        textView.setText(str);

        imageView=(ImageView)findViewById(R.id.imagess);
        Bundle extras=getIntent().getExtras();
        Bitmap bmp=extras.getParcelable("photo");
//        bmp.setWidth(280);
        imageView.setImageBitmap(bmp);

        TextView textView1=(TextView)findViewById(R.id.number);
        String str1=getIntent().getExtras().getString("number");
        textView1.setText(str1);

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getBaseContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                AnotherActivity.this.startActivity(i);
                finish();
            }
        });
    }
}



