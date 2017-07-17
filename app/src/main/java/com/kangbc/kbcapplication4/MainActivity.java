package com.kangbc.kbcapplication4;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;    //뒤로가기 클래스

    @Bind(R.id.tem)
    TextView tem;
    @Bind(R.id.getWeatherBtn)
    Button getWeatherBtn;
    @Bind(R.id.tvLatitude)
    TextView tvLatitude;
    @Bind(R.id.tvLongtitude)
    TextView tvLongtitude;
    @Bind(R.id.lat)
    EditText mlat;
    @Bind(R.id.lon)
    EditText mlon;
    @Bind(R.id.spd)
    TextView spd;
    @Bind(R.id.crdlat)
    TextView crdlat;
    @Bind(R.id.crdlon)
    TextView crdlon;
    @Bind(R.id.basest)
    TextView basest;
    @Bind(R.id.weather)
    TextView weather;

    String TAG = "kbc";
    ProgressDialog progressDialog;


    @OnClick(R.id.getWeatherBtn)
    public void setGetWeatherBtn(View view){
        String lat= mlat.getText().toString();
        String lot = mlon.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        Retrofit client = new Retrofit.Builder().baseUrl("http://api.openweathermap.org").addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface service = client.create(ApiInterface.class);

        Call<Repo> call = service.repo("b11103fdc1bd9e2472109021419a3693", Double.valueOf(lat), Double.valueOf(lot)); //684b98e21b4f35b7d52abe9ff6279349 b11103fdc1bd9e2472109021419a3693

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Log.e(TAG, "onResponse: 1");
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: 2");
                    Repo repo = response.body();
                    tem.setText(String.valueOf(repo.getMain().getTemp()));
                    spd.setText(String.valueOf(repo.getWind().getSpeed()));
                    crdlat.setText(String.valueOf(repo.getCoord().getLat()));
                    crdlon.setText(String.valueOf(repo.getCoord().getLon()));
                    basest.setText(String.valueOf(repo.getBase()));
                    weather.setText(String.valueOf(repo.getWeather().get(0).getId()) + ", " +
                                    String.valueOf(repo.getWeather().get(0).getMain()) + ", " +
                                    String.valueOf(repo.getWeather().get(0).getDescription()) + ", " +
                                    String.valueOf(repo.getWeather().get(0).getIcon()));

                    progressDialog.dismiss();
                } else {
                    Log.e(TAG, "onResponse else : "+tem);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(MainActivity.this, "잘못 입력 하셨습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void getNaverJsonBtn(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        String clientId = "xMQxGnmq5M4quaNqoMIX";
        String clientSecret = "XxMqlp_G0i";

        Retrofit client = new Retrofit.Builder()
                                      .baseUrl("https://openapi.naver.com")
                                      .addConverterFactory(GsonConverterFactory.create())
                                      .build();

        NaverApiInterface service = client.create(NaverApiInterface.class);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/DCIM/Facecheck");
        String fileName = "Test1.jpg";

        File[] listFiles = (new File(sdCard.getAbsolutePath()+"/DCIM/Facecheck/").listFiles());
        int length = listFiles.length-1;

        if(listFiles[length].getName().endsWith(".jpg") || listFiles[length].getName().endsWith(".bmp")) {
            fileName = listFiles[length].getName();
        }

        Log.e(TAG, "getNaverJsonBtn: "+fileName);

        File file = new File(dir, fileName);
        RequestBody reBody = RequestBody.create(MediaType.parse("image/jpeg"), file);  // ok
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reBody);
        Call<NaverRepo> call = service.naverRepo(clientId,clientSecret,body);
        call.enqueue(new Callback<NaverRepo>() {
            @Override
            public void onResponse(Call<NaverRepo> call, Response<NaverRepo> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: isSuccessful");
                    NaverRepo naverRepo = response.body();

                    if(naverRepo.getInfo().getFacecount()!=0){ //닮은꼴 존재
                        for(int i=0;i<naverRepo.getInfo().getFacecount();i++){
                            Log.e(TAG, "닮은사람"+i+" : "+naverRepo.getFaces()[i].getCelebrity().getValue()+" "+naverRepo.getFaces()[i].getCelebrity().getConfidence());
                        }
                        tem.setText(String.valueOf("닮은 사람 수 : "+naverRepo.getInfo().getFacecount()));
                        spd.setText(String.valueOf("Height : "+naverRepo.getInfo().getSize().getHeight()));
                        crdlat.setText(String.valueOf("Width : "+naverRepo.getInfo().getSize().getWidth()));
                        crdlon.setText(String.valueOf("이름, 닮은% : "+naverRepo.getFaces()[0].getCelebrity().getValue()+" "+naverRepo.getFaces()[0].getCelebrity().getConfidence()*100+"%"));
//                        basest.setText(String.valueOf("이름, 닮은% : "+naverRepo.getFaces()[1].getCelebrity().getValue()+" "+naverRepo.getFaces()[1].getCelebrity().getConfidence()));
                        weather.setText(String.valueOf("닮은 사람 수 : "+naverRepo.getFaces().length));
                    }else{ //닮은꼴 미존재
                        tem.setText(String.valueOf("닮은 사람 수 : "+naverRepo.getInfo().getFacecount()));
                        spd.setText(String.valueOf("Height : "+naverRepo.getInfo().getSize().getHeight()));
                        crdlat.setText(String.valueOf("Width : "+naverRepo.getInfo().getSize().getWidth()));
                    }
                    progressDialog.dismiss();
                } else {
                    Log.e(TAG, "onResponse: !isSuccessful");
                    Log.e(TAG, "onResponse else : response.code = "+response.code());
                    Log.e(TAG, "onResponse else : response.message = "+response.message().toString());
                    Log.e(TAG, "onResponse else : response.headers =\n"+response.headers().toString());
                    Log.e(TAG, "onResponse else : response.raw = "+response.raw().toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<NaverRepo> call, Throwable t) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(MainActivity.this, "잘못 입력 하셨습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void getCameraBtn(View v){
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.contentProviderBtn)
    public void contentProviderBtn(View v){
//        ContentResolver cr = getContentResolver();
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
//        int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME);
//        Log.e(TAG, "contentProviderBtn: " + nameidx);

        Intent intent = new Intent(getApplicationContext(), ImageList.class);
        startActivity(intent);
    }

    public void cropBtn(View v){
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}