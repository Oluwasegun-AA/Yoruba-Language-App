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

public class PhrasesActivity extends AppCompatActivity {
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
        words.add(new Word("Come over to my house","Wa si Ile mi", R.raw.phrases_comeover));
        words.add(new Word("Go straight home","Lo si ile taara", R.raw.phrases_gostraight));
        words.add(new Word("Sit down on the chair","Joko si ori aga", R.raw.phrases_sitdown));
        words.add(new Word("Stand up ", "Dide duro", R.raw.phrases_standup));
        words.add(new Word("Eat the food Fast", "Je ounje naa ni kiakia ", R.raw.phrases_eatthe));
        words.add(new Word("Run at full speed", "Sare tefetefe", R.raw.phrases_runat));
        words.add(new Word("up on the hill", "lori Oke", R.raw.phrases_upon));
        words.add(new Word("down in the ant hole", "ninu iho Eera", R.raw.phrases_downin));
        words.add(new Word("Jump up", "Fo Soke", R.raw.phrases_jumpup));
        words.add(new Word("Sing loud", "Ko Orin ni ohun oke", R.raw.phrases_singloud));

        WordAdapter ItemsAdapter = new WordAdapter(this, words, R.color.category_phrases);
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
                    player = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());
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

    //for mermory optimization, release() is use to free the memory used to save the resources when the media is not needed
    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
            audioManager.abandonAudioFocus(focusListener);
        }
    }
}