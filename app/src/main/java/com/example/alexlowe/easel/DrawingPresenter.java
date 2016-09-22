package com.example.alexlowe.easel;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileOutputStream;

public class DrawingPresenter {

    private DrawingView drawingView;
    private DrawingInterface drawingInterface;


    public DrawingPresenter(DrawingView view, DrawingInterface drawingInterface) {
        this.drawingView = view;
        this.drawingInterface = drawingInterface;
    }


    public void onBrushClicked(float brushSize) {
        drawingView.setBrushSize(brushSize);
    }


    public void onFabColorClick() {
        drawingInterface.showColorDialog();
    }

    public void onColorSelected(int newColor) {
        drawingView.setColor(newColor);
        if (drawingView.getErase()) {
            drawingView.setPaintColor(Color.WHITE);
        }
    }


    public void onFabEraseClick() {
        drawingView.setErase(!drawingView.getErase());
        int tintId = drawingView.getErase() ? R.color.colorPressedFab : R.color.colorFab;
        drawingInterface.setEraseFabBackground(tintId);
    }


    public void onFabNewClick() {
        drawingInterface.showNewDialog();
    }

    public void onNewDialogClick() {
        drawingView.startNew();
    }


    public void onClickSave() {
        drawingInterface.showSaveDialog();
    }

    public void onSaveDialogClick() {
        drawingView.setDrawingCacheEnabled(true);
        String savedImage = drawingInterface.saveImage();

        if (savedImage != null) {
            drawingInterface.showToast("Drawing saved to gallery!");
        } else {
            drawingInterface.showToast("Oops! Image could not be Saved");
        }
        drawingView.destroyDrawingCache();
    }


    public void onClickShare() {
        drawingView.setDrawingCacheEnabled(true);
        Bitmap bitmap = drawingView.getDrawingCache();

        try {
            File file = new File(drawingView.getContext().getCacheDir(), "shareImg.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            drawingInterface.sendShareIntent(file);
        } catch (Exception e) {
            drawingInterface.showToast("Share Failed");
            e.printStackTrace();
        }
    }

}
