package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {


    public NumbersFragment() {
        // Required empty public constructor
    }

    ArrayList<Word> num;
    MediaPlayer player;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        num = new ArrayList<>();

        num.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        num.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        num.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        num.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        num.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        num.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        num.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        num.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        num.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        num.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        listView = (ListView) v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Log.e("Numbers", "Numbers activity");


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), num, getResources().getColor(R.color.category_numbers));

        listView.setAdapter(itemsAdapter);

        mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

                if(player != null) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                        // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                        // our app is allowed to continue playing sound but at a lower volume. We'll treat
                        // both cases the same way because our app is playing short sound files.

                        // Pause playback and reset player to the start of the file. That way, we can
                        // play the word from the beginning when we resume playback.
                        player.pause();
                        player.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                        player.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback and clean up resources
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        };

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(player != null){
                    player.stop();
                    player.release();
                    player = null;
                }

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    player = MediaPlayer.create(getActivity(), num.get(position).getmAudioID());
                    player.start();

                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onStop() {

        if(player != null){
            player.stop();
            player.release();
            player = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

        super.onStop();
    }
}
