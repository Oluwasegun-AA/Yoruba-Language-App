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

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer player;
    //requesting audiofocus :  first setting an instance of the audio manager
    AudioManager audioManager;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.word_list);

        /** ArrayList<String> words = new ArrayList<>(); */
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("One","Eni", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("Two", "Eji", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("Three", "Eta", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("Four", "Erin", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("Five", "Arun", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("Six", "Efa", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("Seven", "Eje", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("Eight", "Ejo", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("Nine", "Esan", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("Ten", "Ewa", R.drawable.number_ten, R.raw.number_ten));
     /**  LinearLayout Number_parent = findViewById(R.id.rootview);
        for(int i = 0; i < words.size(); i++) {
            TextView view = new TextView(this);
            view.setText(words.get(i));
            Number_parent.addView(view);

      ArrayAdapter<String> ItemsAdapter = new ArrayAdapter <>(this, android.R.layout.simple_list_item_1, words);
      ListView display = findViewById(R.id.rootview);
      display.setAdapter(ItemsAdapter);
    */

        WordAdapter ItemsAdapter = new WordAdapter(this, words, R.color.category_numbers);
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
                    player = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());
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