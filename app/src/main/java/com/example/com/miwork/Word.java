package com.example.com.miwork;
/**
 * {@Link Word} represents a Vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok Translation for that word.
 */
public class Word {
    /** Default Translation of the Word*/
    private String mDefaultTranslation;
    /** Miwok Translation of the Word*/
    private String mMiwokTranslation;
    /** Image Resource Id*/
    private int mImageResourceId = 0;
    /** sound file resource id (raw)*/
    private int mAudioResourceId;

    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceId)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    /**
     *  Gets the Default translation of the Word
     */
    public String getDefaultTranslation()
    {
        return mDefaultTranslation;
    }

    /**
     * Gets the Miwok Translation of the word
     */
    public String getMiwokTranslation()
    {
        return mMiwokTranslation;
    }

    public int getImageResourceID(){
        return mImageResourceId;
    }

    public int getAudioResourceId(){return mAudioResourceId;}

    public boolean hasImage(){
        return mImageResourceId != 0 ;
    }
}
