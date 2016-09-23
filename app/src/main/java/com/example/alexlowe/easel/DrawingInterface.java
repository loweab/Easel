package com.example.alexlowe.easel;

import android.graphics.Bitmap;

import java.io.File;

public interface DrawingInterface {

    void showColorDialog();

    void setEraseFabBackground(int tintId);

    void showNewDialog();

    void showSaveDialog();

    //should all interfaces return void? ANS: no they don't have to
    String saveImage();

    void sendShareIntent(File file);

    void showToast(String toastString);

    File getFileFromBitmap(Bitmap bitmap);
}
