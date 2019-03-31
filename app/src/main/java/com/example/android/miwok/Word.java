package com.example.android.miwok;

public class Word {

    /**
     * Default translation for the word
     */
    private String mDefaultTranslation;

    /**
     * Miwok translation for the word
     */
    private String mMiwokTranslation;

    /*Image for a word*/
    private int mImageID = NO_IMAGE;

    public static final int NO_IMAGE = -1;

    private int mAudioID;

    public Word(String mDefaultTranslation, String mMiwokTranslation, int mImageID, int mAudioID) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMiwokTranslation = mMiwokTranslation;
        this.mImageID = mImageID;
        this.mAudioID = mAudioID;
    }

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     */
    public Word(String defaultTranslation, String miwokTranslation, int mAudioID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        this.mAudioID = mAudioID;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public  String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getmImageID() {
        return mImageID;
    }

    public int getmAudioID() {
        return mAudioID;
    }


}
