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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FamilyActivity extends AppCompatActivity {
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
        words.add(new Word("Father", "Baba", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("Mother", "Iya", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("Sister", "Arabinrin", R.drawable.family_younger_sister, R.raw.family_sister));
        words.add(new Word("Brother", "Arakunrin", R.drawable.family_younger_brother, R.raw.family_brother));
        words.add(new Word("Uncle", "Arakunrin Aburo / Egbon Baba or Aburo / Egbon Iya ", R.drawable.family_older_brother, R.raw.family_uncle));
        words.add(new Word("Aunty", "Arabirin Aburo / Egbon Baba or Aburo / Egbon Iya", R.drawable.family_older_sister, R.raw.family_aunty));
        words.add(new Word("Grand Mother", "Iya Baba or Iya Iya", R.drawable.family_mother, R.raw.family_grandmother));
        words.add(new Word("Grand Father", "Baba Baba or Baba Iya", R.drawable.family_father, R.raw.family_grandfather));
        words.add(new Word("Nephew", "Arakunrin ọmọ Egbon tabi Aburo", R.drawable.family_younger_brother, R.raw.family_nephew));
        words.add(new Word("Niece", "Arabirin ọmọ Egbon tabi Aburo", R.drawable.family_younger_sister, R.raw.family_niece));

        WordAdapter ItemsAdapter = new WordAdapter(this, words, R.color.category_family);
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
                    player = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());
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

    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
            audioManager.abandonAudioFocus(focusListener);
        }
    }
}
