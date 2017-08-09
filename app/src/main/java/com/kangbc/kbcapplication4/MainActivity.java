package com.kangbc.kbcapplication4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    @Bind(R.id.iv)
    ImageView iv;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private static final int RESULT_FROM_CROP = 4;

    private Uri photoUri;
    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;

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
                Log.e(TAG, "onResponse: " + response.headers() );
                Log.e(TAG, "onResponse: " + response.code() );
                Log.e(TAG, "onResponse: " + response.message() );
                Log.e(TAG, "onResponse: " + response.errorBody() );
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
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                takePhoto();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
//        goToAlbum();
    }

    public void albumBtn(View v){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                goToAlbum();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
//
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.kangbc.kbcapplication4.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String getTime = sdf.format(date);
        String imageFileName = getTime;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Facecheck/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(MainActivity.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            iv.setImageURI(null);
            iv.setImageURI(photoUri);
        } else if (requestCode == RESULT_FROM_CROP) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(intent);;
        }
    }

    //Android N crop image
    public void cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/Facecheck/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.kangbc.kbcapplication4.fileprovider", tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            startActivityForResult(i, CROP_FROM_CAMERA);
            startActivityForResult(i, RESULT_FROM_CROP);
        }
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