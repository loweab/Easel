package com.example.alexlowe.easel;

import java.io.File;

public interface DrawingInterface {

    void showColorDialog();

    void setEraseFabBackground(int tintId);

    void showNewDialog();

    void showSaveDialog();

    //should all interfaces return void?
    String saveImage();

    void sendShareIntent(File file);

    void showToast(String toastString);
}
