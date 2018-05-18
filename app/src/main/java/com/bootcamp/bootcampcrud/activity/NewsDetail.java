package com.bootcamp.bootcampcrud.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bootcamp.bootcampcrud.APIService.APIClient;
import com.bootcamp.bootcampcrud.APIService.APIInterfacesRest;
import com.bootcamp.bootcampcrud.R;
import com.bootcamp.bootcampcrud.servicemodel.newsmodel.Bootcampnews;
import com.bootcamp.bootcampcrud.servicemodel.updatemodel.UpdateOrder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsDetail extends AppCompatActivity {

    EditText txtTitle, txtContent, txtAuthor;
    Button btnCapture, btnSubmit;
    ImageView imgPhoto;

    Bitmap bitmap;
    static  byte[] bitmapx;




    public Uri fileUri;
    private int CAMERA_REQUEST = 100;
    private int Gallary_REQUEST = 101;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    Bootcampnews data;

    String type ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        data  = (Bootcampnews) getIntent().getExtras().getSerializable("data");
        type  =  getIntent().getExtras().getString("type");


        setContentView(R.layout.activity_news_detail);

        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtAuthor = (EditText)findViewById(R.id.txtAuthor);
        txtContent = (EditText)findViewById(R.id.txtContent);

        btnCapture = (Button)findViewById(R.id.btnCapture);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        imgPhoto = (ImageView)findViewById(R.id.imgPhoto);


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equalsIgnoreCase("update")) {
                    save();
                }
            }
        });

        if (data!=null && type.equalsIgnoreCase("update")){

            txtTitle.setText(data.getTitle().toString());
            txtAuthor.setText(data.getAuthor().toString());
            txtContent.setText(data.getContent().toString());


        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imgPhoto.setImageBitmap(bitmap);


            bitmapx = baos.toByteArray();

        } else if (requestCode == Gallary_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(picturePath);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage(bitmap, 180);
                    break;
                // etc.
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

            imgPhoto.setImageBitmap(bitmap);
            bitmapx = baos.toByteArray();

        }


    }

    private void Camerapermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(NewsDetail.this
                ,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewsDetail.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(NewsDetail.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    void openCamera() {
        File directory = new File(
                Environment.getExternalStorageDirectory(), "iCollector" + "/" + getPackageName());
        try {
            SimpleDateFormat sdfPic = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateandTime = sdfPic.format(new Date()).replace(" ", "");
            File imagesFolder = new File(directory.getAbsolutePath());
            imagesFolder.mkdirs();

            String fname = "IMAGE_" + currentDateandTime + ".jpg";
            File file = new File(imagesFolder, fname);
            fileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }


   public MultipartBody.Part toBodyImage (byte[] byteArray,String filename) {
       RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);

       // MultipartBody.Part is used to send also the actual file name
       MultipartBody.Part body =
               MultipartBody.Part.createFormData("image", filename + ".jpg", requestFile);

       return body;

   }

    ProgressDialog progressDialog;

    public void save() {




        APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bitmapx);

        progressDialog = new ProgressDialog(NewsDetail.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        Call<UpdateOrder> updateService = apiInterface.updateData(toRequestBody(data.getId()),toRequestBody(txtTitle.getText().toString()),toRequestBody(txtContent.getText().toString()),toRequestBody(txtAuthor.getText().toString()),toBodyImage(bitmapx,data.getTitle()+data.getId()));


        updateService.enqueue(new Callback<UpdateOrder>() {
            @Override
            public void onResponse(Call<UpdateOrder> call, Response<UpdateOrder> response) {
                progressDialog.dismiss();
                if (response != null) {
                    Log.d("Test", response.message());

                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                //   Toast.makeText(getApplicationContext(), "Nothing happen", Toast.LENGTH_LONG).show();
                // resultNya = false;
            }

            @Override
            public void onFailure(Call<UpdateOrder> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();


            }
        });
    }



}
