package com.example.dwi.cctv_view.cctvpolda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dwi.cctv_view.R;
import com.example.dwi.cctv_view.cctvpolda.ListCctvPolda;
import com.example.dwi.cctv_view.cctvpolda.PlayCctvPolda;
import com.example.dwi.cctv_view.cctvpolresta.ListCctvPolresta;

import java.nio.ByteBuffer;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;

public class PlayCctvPolda extends AppCompatActivity implements MediaPlayer.MediaPlayerCallback {

    String stream_url;
    MediaPlayer mediaPlayer;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cctv_polda);

        mediaPlayer = (MediaPlayer) findViewById(R.id.mediaPlayer);

        Intent intent = getIntent();
        String judul   = intent.getStringExtra("judul");
        String id      = intent.getStringExtra("id");
        stream_url      = intent.getStringExtra("ip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(judul);
        mediaPlayer.getSurfaceView().setZOrderOnTop(true);

        mDialog = new ProgressDialog(PlayCctvPolda.this);
        mDialog.setMessage("Memutar video mohon tunggu..");
        mDialog.show();


        if(mediaPlayer != null){
            mediaPlayer.getConfig().setConnectionUrl(stream_url);
            if(mediaPlayer.getConfig().getConnectionUrl().isEmpty())
                return;

            MediaPlayerConfig config = new MediaPlayerConfig();
            config.setConnectionUrl(mediaPlayer.getConfig().getConnectionUrl());
            config.setConnectionNetworkProtocol(-1);
            config.setConnectionDetectionTime(2000);
            config.setConnectionBufferingTime(500);
            config.setDecodingType(1);
            config.setRendererType(1);
            config.setSynchroEnable(1);
            config.setSynchroNeedDropVideoFrames(1);
            config.setEnableColorVideo(1);
            config.setDataReceiveTimeout(30000);
            config.setNumberOfCPUCores(0);

            mediaPlayer.Open(config, PlayCctvPolda.this);
        }
    }

    //Fungsi Kembali
    @Override
    public void onBackPressed() {
        Intent a = new Intent(PlayCctvPolda.this, ListCctvPolda.class);
        a.putExtra("kategori","polda");
        startActivity(a);
        finish();
    }


    // fungsi kembali
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public int Status(int i) {
        if(i == MediaPlayer.PlayerNotifyCodes.PLP_PLAY_SUCCESSFUL.ordinal())
            mDialog.dismiss();
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }
  }
