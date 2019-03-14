package gouv.cba533.cours2;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.farm);
        //mediaPlayer.setLooping(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SeekBar seekBar  = findViewById(R.id.seekBar_defilement);
                seekBar.setProgress(mediaPlayer.getCurrentPosition()*100/mediaPlayer.getDuration());
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        SeekBar seekBarVolume = findViewById(R.id.seekBar_volume);
        seekBarVolume.setProgress(audioManager.STREAM_MUSIC);
        setListener();
    }

    public void setListener(){
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMediaPlayer();
            }
        });

        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseMediaPlayer();
            }
        });
        setSeekBarListener();
    }

    private void setMediaListener(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast();
            }
        });
    }

    private void Toast(){
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    private void setSeekBarListener(){
        SeekBar seekBarMedia = findViewById(R.id.seekBar_defilement);
        seekBarMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seekInMedia(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar seekBarVolume = findViewById(R.id.seekBar_volume);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int MAX_VOLUME = 100;
                float volume=(float)(Math.log(MAX_VOLUME-progress)/Math.log(MAX_VOLUME));
                mediaPlayer.setVolume(1-volume, 1-volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setMediaListener();
    }

    private void PlayMediaPlayer(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    private void PauseMediaPlayer(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void seekInMedia(int progress) {
        mediaPlayer.seekTo(progress*mediaPlayer.getDuration()/100);
    }
}
