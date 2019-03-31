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
public class PhrasesFragment extends Fragment {


    public PhrasesFragment() {
        // Required empty public constructor
    }

    ArrayList<Word> phrase;
    MediaPlayer player;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.word_list, container, false);

        phrase = new ArrayList<>();

        phrase.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        phrase.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        phrase.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        phrase.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        phrase.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        phrase.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        phrase.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        phrase.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        phrase.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        phrase.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        listView = (ListView) v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), phrase, getResources().getColor(R.color.category_phrases));

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

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
                    player = MediaPlayer.create(getActivity(), phrase.get(position).getmAudioID());
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

        if(player !=null){
            player.stop();
            player.release();
            player = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

        super.onStop();
    }
}
