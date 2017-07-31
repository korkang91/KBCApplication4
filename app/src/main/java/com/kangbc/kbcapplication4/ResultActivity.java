package com.kangbc.kbcapplication4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by mac on 2017. 7. 5..
 */

public class ResultActivity extends AppCompatActivity {

    private String TAG = "KBC";
    ProgressDialog progressDialog;

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.height)
    TextView height;
    @Bind(R.id.width)
    TextView width;
    @Bind(R.id.percent)
    TextView percent;
    @Bind(R.id.count)
    TextView count;

    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.facecount)
    TextView facecount;
    @Bind(R.id.gender)
    TextView gender;
    @Bind(R.id.age)
    TextView age;
    @Bind(R.id.emotion)
    TextView emotion;
    @Bind(R.id.pose)
    TextView pose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        naverResult();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void naverResult(){
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

        for(int i=0;i<=length;i++){
            Log.e(TAG, "naverResult: for 안" + listFiles[i].getName());
        }

        if(listFiles[length].getName().endsWith(".jpg") || listFiles[length].getName().endsWith(".bmp")) {
            fileName = listFiles[length].getName();
            Log.e(TAG, "naverResult: if 안");
        }

        Log.e(TAG, "getNaverJsonBtn: "+fileName);

        File file = new File(dir, fileName);

        /** 완성된 이미지 보여주기  */
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(sdCard.getAbsolutePath() + "/DCIM/Facecheck/"+fileName, bfo);
        Bitmap resized = Bitmap.createScaledBitmap(bm, 320, 460, true);
        iv.setImageBitmap(resized);

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
//                        name.setText(String.valueOf("getFacecount : "+naverRepo.getInfo().getFacecount()));
                        height.setText(String.valueOf("Height : "+naverRepo.getInfo().getSize().getHeight()));
                        width.setText(String.valueOf("Width : "+naverRepo.getInfo().getSize().getWidth()));
                        percent.setText(String.valueOf("이름, 닮은% : "+naverRepo.getFaces()[0].getCelebrity().getValue()+" "+naverRepo.getFaces()[0].getCelebrity().getConfidence()*100+"%"));
                        count.setText(String.valueOf("getFaces().length : "+naverRepo.getFaces().length));
                    }else{ //닮은꼴 미존재
                        count.setText(String.valueOf("닮은 사람 수 : "+naverRepo.getInfo().getFacecount()));
                        height.setText(String.valueOf("Height : "+naverRepo.getInfo().getSize().getHeight()));
                        width.setText(String.valueOf("Width : "+naverRepo.getInfo().getSize().getWidth()));
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
                Toast toast = Toast.makeText(ResultActivity.this, "잘못 입력 하셨습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //얼굴감지
        Call<NaverRepo> call2 = service.naverRepo2(clientId,clientSecret,body);
        call2.enqueue(new Callback<NaverRepo>() {
            @Override
            public void onResponse(Call<NaverRepo> call, Response<NaverRepo> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: isSuccessful");
                    NaverRepo naverRepo2 = response.body();

                    if(naverRepo2.getInfo().getFacecount()!=0){ //닮은꼴 존재
//                        for(int i=0;i<naverRepo2.getInfo().getFacecount();i++){
//                            Log.e(TAG, "닮은사람"+i+" : "+naverRepo2.getFaces()[i].getCelebrity().getValue()+" "+naverRepo2.getFaces()[i].getCelebrity().getConfidence());
//                        }
                        Log.e(TAG, "onResponse: 닮은꼴 존재");
                        facecount.setText(String.valueOf("찾은 얼굴 개수 : "+naverRepo2.getInfo().getFacecount()));
                        gender.setText(String.valueOf("성별 및 일치율 : "+naverRepo2.getFaces()[0].getGender().getValue() + " / " + naverRepo2.getFaces()[0].getGender().getConfidence()*100+"%"));
                        age.setText(String.valueOf("나이 및 일치율 : "+naverRepo2.getFaces()[0].getAge().getValue() + " / " + naverRepo2.getFaces()[0].getAge().getConfidence()*100+"%"));
                        emotion.setText(String.valueOf("감정상태 : "+naverRepo2.getFaces()[0].getEmotion().getValue()+" / "+naverRepo2.getFaces()[0].getEmotion().getConfidence()*100+"%"));
                        pose.setText(String.valueOf("얼굴방향 : "+naverRepo2.getFaces()[0].getPose().getValue()+" / "+naverRepo2.getFaces()[0].getPose().getConfidence()*100+"%"));
//                        percent.setText(String.valueOf("이름, 닮은% : "+naverRepo.getFaces()[0].getCelebrity().getValue()+" "+naverRepo.getFaces()[0].getCelebrity().getConfidence()*100+"%"));
                    }else{ //닮은꼴 미존재
                        Log.e(TAG, "onResponse: 닮은꼴 미존재");
                        facecount.setText(String.valueOf("찾은 얼굴 개수 : "+naverRepo2.getInfo().getFacecount()));
//                        gender.setText(String.valueOf("Height : "+naverRepo2.getInfo().getSize().getHeight()));
//                        age.setText(String.valueOf("Width : "+naverRepo2.getInfo().getSize().getWidth()));
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
                Toast toast = Toast.makeText(ResultActivity.this, "잘못 입력 하셨습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}
