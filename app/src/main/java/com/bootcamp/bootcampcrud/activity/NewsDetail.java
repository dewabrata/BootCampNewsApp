package com.bootcamp.bootcampcrud.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bootcamp.bootcampcrud.R;
import com.bootcamp.bootcampcrud.servicemodel.newsmodel.Bootcampnews;


public class NewsDetail extends AppCompatActivity {

    EditText txtTitle, txtContent, txtAuthor;
    Button btnCapture, btnSubmit;
    ImageView imgPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bootcampnews data  = (Bootcampnews) getIntent().getExtras().getSerializable("data");



        setContentView(R.layout.activity_news_detail);

        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtAuthor = (EditText)findViewById(R.id.txtAuthor);
        txtContent = (EditText)findViewById(R.id.txtContent);

        btnCapture = (Button)findViewById(R.id.btnCapture);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        imgPhoto = (ImageView)findViewById(R.id.imgPhoto);


        if (data!=null){

            txtTitle.setText(data.getTitle().toString());
            txtAuthor.setText(data.getAuthor().toString());
            txtContent.setText(data.getContent().toString());


        }


    }
}
