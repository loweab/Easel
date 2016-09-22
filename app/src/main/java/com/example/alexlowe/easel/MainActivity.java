package com.example.alexlowe.easel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawing)
    DrawingView drawingView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab_fine)
    FloatingActionButton fabFine;
    @BindView(R.id.fab_superfine)
    FloatingActionButton fabSuperfine;
    @BindView(R.id.fab_small)
    FloatingActionButton fabSmall;
    @BindView(R.id.fab_medium)
    FloatingActionButton fabMedium;
    @BindView(R.id.fab_large)
    FloatingActionButton fabLarge;
    @BindView(R.id.fab_color)
    FloatingActionButton fabColor;
    @BindView(R.id.fab_erase)
    FloatingActionButton fabErase;
    @BindView(R.id.fab_new)
    FloatingActionButton fabNew;

    private Animation fabOpen, fabClose;

    private Boolean isFabOpen = false;

    private FloatingActionButton pressedFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
    }


    @OnClick({R.id.fab, R.id.fab_color, R.id.fab_erase, R.id.fab_new, R.id.fab_large, R.id.fab_medium, R.id.fab_small,
            R.id.fab_fine, R.id.fab_superfine})
    public void pressFabToolbar(View view) {
        switch (view.getId()) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab_color:
                showColorPickerDialog();
                break;
            case R.id.fab_erase:
                toggleErase();
                break;
            case R.id.fab_new:
                showNewDialog();
                break;
        }
    }

    @OnClick({R.id.fab_large, R.id.fab_medium, R.id.fab_small, R.id.fab_fine, R.id.fab_superfine})
    public void pressBrush(View view) {
        float superfineBrush = getResources().getInteger(R.integer.superfine_size);
        float fineBrush = getResources().getInteger(R.integer.fine_size);
        float mediumBrush = getResources().getInteger(R.integer.medium_size);
        float largeBrush = getResources().getInteger(R.integer.large_size);
        float smallBrush = getResources().getInteger(R.integer.small_size);

        switch (view.getId()) {
            case R.id.fab_superfine:
                clickBrush(superfineBrush, view);
                break;
            case R.id.fab_fine:
                clickBrush(fineBrush, view);
                break;
            case R.id.fab_small:
                clickBrush(smallBrush, view);
                break;
            case R.id.fab_medium:
                clickBrush(mediumBrush, view);
                break;
            case R.id.fab_large:
                clickBrush(largeBrush, view);
                break;
        }
    }

    private void clickBrush(float brushSize, View view) {
        drawingView.setBrushSize(brushSize);
        if (pressedFab != null) {
            pressedFab.setBackgroundTintList(ColorStateList.valueOf(getResources()
                    .getColor(R.color.colorFab)));
        }
        pressedFab = (FloatingActionButton) view;
        pressedFab.setBackgroundTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.colorPressedFab)));
        animateFAB();
    }

    private void toggleErase() {
        drawingView.setErase(!drawingView.getErase());
        int eraseBackground = (drawingView.getErase()) ?
                getResources().getColor(R.color.colorPressedFab) :
                getResources().getColor(R.color.colorFab);
        fabErase.setBackgroundTintList(ColorStateList.valueOf(eraseBackground));
    }

    public void animateFAB() {
        if (isFabOpen) {

            fabSuperfine.startAnimation(fabClose);
            fabFine.startAnimation(fabClose);
            fabSmall.startAnimation(fabClose);
            fabMedium.startAnimation(fabClose);
            fabLarge.startAnimation(fabClose);
            fabSuperfine.setClickable(false);
            fabFine.setClickable(false);
            fabSmall.setClickable(false);
            fabMedium.setClickable(false);
            fabLarge.setClickable(false);
            isFabOpen = false;
        } else {

            fabSuperfine.startAnimation(fabOpen);
            fabFine.startAnimation(fabOpen);
            fabSmall.startAnimation(fabOpen);
            fabMedium.startAnimation(fabOpen);
            fabLarge.startAnimation(fabOpen);
            fabSuperfine.setClickable(true);
            fabFine.setClickable(true);
            fabSmall.setClickable(true);
            fabMedium.setClickable(true);
            fabLarge.setClickable(true);
            isFabOpen = true;
        }
    }

    private void showColorPickerDialog() {
        new ChromaDialog.Builder()
                .initialColor(drawingView.getColor())
                .colorMode(ColorMode.RGB)
                .indicatorMode(IndicatorMode.HEX)
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int newColor) {
                        drawingView.setColor(newColor);
                        drawingView.setErase(drawingView.getErase());
                    }
                })
                .create()
                .show(getSupportFragmentManager(), "dialog");
    }


    private void showNewDialog() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New Drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawingView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", null);
        newDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            case R.id.action_save:
                saveDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        drawingView.setDrawingCacheEnabled(true);
        Bitmap bitmap = drawingView.getDrawingCache();

        try {
            File file = new File(getApplicationContext().getCacheDir(), "shareImg.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image"));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Share Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void saveDialog() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?(must have external storage availible)");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawingView.setDrawingCacheEnabled(true);
                String savedImage = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        drawingView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "Paint Drawing");

                if (savedImage != null) {
                    Toast.makeText(getApplicationContext(), "Drawing saved to gallery!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! Image could not be Saved",
                            Toast.LENGTH_SHORT).show();
                }
                drawingView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", null);
        saveDialog.show();
    }
}