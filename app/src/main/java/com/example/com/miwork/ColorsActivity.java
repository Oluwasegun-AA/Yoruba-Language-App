package com.example.com.miwork;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer player;
    //requesting audiofocus :  first setting an instance of the audio manager
    AudioManager audioManager;

    Context mContext = this;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Set the content of the activity to use the activity_main.xml layout file
            setContentView(R.layout.word_list);

            final ArrayList<Word> words = new ArrayList<>();
            words.add(new Word("Black","Dúdú", R.drawable.color_black, R.raw.color_black));
            words.add(new Word("White", "Funfun", R.drawable.color_white, R.raw.color_white));
            words.add(new Word("Red", "Pupa", R.drawable.color_red, R.raw.color_red));
            words.add(new Word("Blue", "Búlù", R.drawable.color_blue, R.raw.color_blue));
            words.add(new Word("Yellow", "Yélò", R.drawable.color_mustard_yellow, R.raw.color_yellow));
            words.add(new Word("Grey", "Awọ Eérú", R.drawable.color_gray, R.raw.color_gray));
            words.add(new Word("Brown ", "Awọ Ara", R.drawable.color_brown, R.raw.color_brown));
            words.add(new Word("Green", "Awọ Ewé", R.drawable.color_green, R.raw.color_green));
            words.add(new Word("orange", "Awọ osan", R.drawable.color_orange, R.raw.color_orange));

            WordAdapter ItemsAdapter = new WordAdapter(this, words, R.color.category_colors);
            ListView display = findViewById(R.id.root_view);
            display.setAdapter(ItemsAdapter);

            display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Word word = words.get(position);
                    releaseMediaPlayer();

                    // Request audio focus for playback
                    audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    int result = audioManager.requestAudioFocus(focusListener,
                            // Use the music stream type.
                            AudioManager.STREAM_MUSIC,
                            // Request permanent focus.
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        // Start playback
                        player = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceId());
                        player.start();
                    }
                }
            });
        }
    // pauses the activity when the app is out of focus
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    AudioManager.OnAudioFocusChangeListener focusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus
                // Pause playback immediately
                player.stop();
                player.prepareAsync();
            }
            else if ((focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)||(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)) {
                // Stop playback and clean resources (memory)
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                player.start();
            }
        }
    };

    //for mermory optimization, release() is used to free the memory used to save the resources when the media is not needed
    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
            audioManager.abandonAudioFocus(focusListener);
        }
    }
    }