package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class FamilyFragment extends Fragment {


    public FamilyFragment() {
        // Required empty public constructor
    }

    ArrayList<Word> family;
    MediaPlayer player;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        family = new ArrayList<>();

        family.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        family.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        family.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        family.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        family.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        family.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        family.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        family.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        family.add(new Word("grandmother ", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        family.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        listView = (ListView) v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), family, getResources().getColor(R.color.category_family));

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
                    player = MediaPlayer.create(getActivity(), family.get(position).getmAudioID());
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

        if(player != null)
        {
            player.stop();
            player.release();
            player = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

        super.onStop();
    }
}
