package com.login.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements Camera2APIs.Camera2Interface, TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private Camera2APIs mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = (TextureView)findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        mCamera = new Camera2APIs(this);
    }

    protected void onResume() {
        super.onResume();

        if (mTextureView.isAvailable()) {
            openCamera();
        } else {
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    /* Surface Callbacks */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        openCamera();
        Log.d("test111","10");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d("test111","9");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.d("test111","8");
        return true;

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        Bitmap bmp = mTextureView.getBitmap();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Log.d("test111","7");
        save(bmp);
    }


    public void save(Bitmap bmp){
        long now = System.currentTimeMillis();
        String filename = now+".jpg";
        File file = new File(Environment.getExternalStorageDirectory().getPath(), filename);
        //껍데기 파일에 이미지 저장하기
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //사진 90도 돌리고 , 좌우 반전 돌려서 저장하기

        // bitmap 파일 jpeg파일로 만들기
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {
        CameraManager cameraManager = mCamera.CameraManager_1(this);
        String cameraId = mCamera.CameraCharacteristics_2(cameraManager);
        mCamera.CameraDevice_3(cameraManager, cameraId);
        Log.d("test111","6");
    }


    @Override
    public void onCameraDeviceOpened(CameraDevice cameraDevice, Size cameraSize) {
        Log.d("test111","3");
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(cameraSize.getWidth(), cameraSize.getHeight());
        Surface surface = new Surface(texture);

        mCamera.CaptureSession_4(cameraDevice, surface);
        mCamera.CaptureRequest_5(cameraDevice, surface);
    }
    private void closeCamera() {
        mCamera.closeCamera();
    }

    @Override
    protected void onPause() {
        closeCamera();
        super.onPause();
    }
}
