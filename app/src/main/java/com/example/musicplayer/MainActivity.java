package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private String nameAudio = "Kai Angel - Trivial";
    private final String link = "https://www.dropbox.com/s/blgri0r6fcz06z9/Kai%20Angel%2C%209mice%20-%20TRIVIAL_%28sparkmusic.ru%29.mp3?dl=0";

    private MediaPlayer mp;
    private AudioManager audioManager;

    private TextView nameTrack;
    private Switch loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        nameTrack = findViewById(R.id.textOut);
        loop = findViewById(R.id.switchLoop);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        loop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mp != null) {
                    mp.setLooping(isChecked);
                    nameTrack.setText(nameAudio + "\n(проигрывание " + mp.isPlaying() + ", время " + mp.getCurrentPosition()
                            + ",\nповтор " + mp.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
                }
            }
        });
    }

    public void onClickSource(View view) throws IOException {

        releaseMediaPlayer();

        try {
            switch (view.getId()) {
                case R.id.btnStream:
                    Toast.makeText(this, "Запущен поток аудио", Toast.LENGTH_SHORT).show();

                    mp = new MediaPlayer();
                    mp.setDataSource(link);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setOnPreparedListener(this);
                    mp.prepareAsync();
                    nameAudio = "Бархатные Тяги";
                    nameTrack.setText(nameAudio + "\n(проигрывание " + mp.isPlaying() + ", время " + mp.getCurrentPosition()
                            + ",\nповтор " + mp.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");

                    break;
                case R.id.btnRAW:
                    Toast.makeText(this, "Запущен аудио-файл с памяти телефона", Toast.LENGTH_SHORT).show();

                    mp = MediaPlayer.create(this, R.raw.tyagi);
                    mp.start();

                    nameAudio = "Бархатные Тяги";
                    nameTrack.setText(nameAudio + "\n(проигрывание " + mp.isPlaying() + ", время " + mp.getCurrentPosition()
                            + ",\nповтор " + mp.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");

                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT).show();
        }

        if (mp == null) return;

        mp.setLooping(loop.isChecked());
        mp.setOnCompletionListener(this);
    }

    public void onClick(View view) {
        if (mp == null) return;
        switch (view.getId()) {
            case R.id.btnResume:
                if (!mp.isPlaying()) {
                    mp.start();
                }
                break;
            case R.id.btnPause:
                if (mp.isPlaying()) {
                    mp.pause();
                }
                break;
            case R.id.btnStop:
                mp.stop();
                break;
            case R.id.btnForward:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.btnBack:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
        }
        nameTrack.setText(nameAudio + "\n(проигрывание " + mp.isPlaying() + ", время " + mp.getCurrentPosition()
                + ",\nповтор " + mp.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}