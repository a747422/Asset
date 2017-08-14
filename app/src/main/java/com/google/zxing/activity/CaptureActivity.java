package com.google.zxing.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.assetsapp.BaseActivity;
import com.example.administrator.assetsapp.Bean.LabelBean;
import com.example.administrator.assetsapp.Bean.MyTestApiService;
import com.example.administrator.assetsapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.view.ViewfinderView;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Initial the camera
 * 二维码扫描。
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    //	private Button cancelScanButton;
    public static final String TAG = "Captu2";
    public static final String API_BASE_URL = "http://112.74.212.95/php/";
    public String resp = "";
    private Camera.Parameters parameter = null;
    private Camera camera = null;
    boolean flag = false;
    Camera m_Camera;
    @BindView(R.id.scanner_toolbar_back)
    ImageView imgBack;
    @BindView(R.id.IB)
    ImageButton IB;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
//		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        //添加toolbar
        addToolbar();
        IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!flag) {
                    IB.setBackground(getResources().getDrawable(R.drawable.light_on));
                    try{
                        camera.startPreview();
                        parameter = camera.getParameters();
                        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameter);
                    } catch(Exception ex){}
                    flag =true;
                } else {
                    IB.setBackground(getResources().getDrawable(R.drawable.light_off));
                    try{
                        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameter);
                        camera.release();

                    } catch(Exception ex){}
                    flag = false;
                }
            }
        });

    }

//    @OnClick(R.id.IB)
//    public void OnClick() {
//        if(IB.isSelected()){
//            IB.setImageDrawable(getResources().getDrawable(R.drawable.light_on));
//        }else {
//            IB.setImageDrawable(getResources().getDrawable(R.drawable.light_off));
//        }
//    }

    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */

    public void handleDecode(Result result, Bitmap barcode) {
        final String[] CardName = {""};
        final String[] placeName = {""};
        final String[] inout = {""};
        final String[] MASKSYNCV2 = {""};
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        Log.d(TAG, "扫码结果 " + resultString);
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    //在build.gradle添加
                    // compile 'com.squareup.retrofit2:converter-scalars:2.1.0+'
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //设置OKHttpClient,如果不设置会提供一个默认的
                    .client(new OkHttpClient())
                    .build();
            MyTestApiService myTestApiService = retrofit.create(MyTestApiService.class);
            Call<String> doubanCall = myTestApiService.postScan(resultString);
            doubanCall.enqueue(new retrofit2.Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {

                        Log.d(TAG, "服务器返回：" + response.body().toString());
                        resp = response.body().toString();
                        Gson gson = new Gson();
                        List<LabelBean> ressult = gson.fromJson(resp, new TypeToken<List<LabelBean>>() {
                        }.getType());
                        for (LabelBean res : ressult) {
                            Log.d(TAG, "getMASKSYNCV2 is " + res.getMASKSYNCV2());
                            Log.d(TAG, "getCardName is " + res.getCardName());
                            CardName[0] = res.getCardName();
                            placeName[0] = res.getPlaceName();
                            inout[0] = res.getInout();
                            MASKSYNCV2[0] = res.getMASKSYNCV2();
                        }
                        if (resp.length() > 5) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CaptureActivity.this);
//                        dialog.setIcon(R.drawable.check_bg)
                            dialog.setTitle("提示");
                            Log.d(TAG, "标签名" + CardName[0]);
                            dialog.setMessage("ID：" + resultString + "\n设备名：" + CardName[0] + "\n入库时间：" + MASKSYNCV2[0]);
                            dialog.setPositiveButton("确定", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //  finish();
                                        }
                                    });

                            dialog.show();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CaptureActivity.this);
//                        dialog.setIcon(R.drawable.check_bg)
                            dialog.setTitle("提示");
                            dialog.setMessage("没有该库存！");
                            dialog.setPositiveButton("确定", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // finish();
                                        }
                                    });
                            dialog.show();
                        }
                    } else {
                        Log.d(TAG, "超时");
                    }
                    mHandler.postDelayed(runnable, 3000);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "网络错误");
                }

            });
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", resultString);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
        }
//        CaptureActivity.this.finish();

    }

    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (handler != null)
                    mHandler.postDelayed(runnable, 3000);
                handler.restartPreviewAndDecode();  //实现多次扫描
                System.out.println("do...");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @OnClick(R.id.scanner_toolbar_back)
    void imgBack() {
        this.finish();
    }
}