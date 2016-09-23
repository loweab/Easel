package com.example.alexlowe.easel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DrawingInterface {

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

    private Animation fabOpen;
    private Animation fabClose;
    private Boolean isFabOpen = false;
    private FloatingActionButton pressedFab;
    DrawingPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new DrawingPresenter(drawingView, this);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
    }


    @OnClick(R.id.fab)
    public void pressFab() {
        animateFAB();
    }

    @OnClick(R.id.fab_large)
    public void pressLarge(View view) {
        float largeBrush = getResources().getInteger(R.integer.large_size);
        presenter.onBrushClicked(largeBrush);
        setBrushSelection(view);
    }

    @OnClick(R.id.fab_medium)
    public void pressMedium(View view) {
        float mediumBrush = getResources().getInteger(R.integer.medium_size);
        presenter.onBrushClicked(mediumBrush);
        setBrushSelection(view);
    }

    @OnClick(R.id.fab_small)
    public void pressSmall(View view) {
        float smallBrush = getResources().getInteger(R.integer.small_size);
        presenter.onBrushClicked(smallBrush);
        setBrushSelection(view);
    }

    @OnClick(R.id.fab_fine)
    public void pressFine(View view) {
        float fineBrush = getResources().getInteger(R.integer.fine_size);
        presenter.onBrushClicked(fineBrush);
        setBrushSelection(view);
    }

    @OnClick(R.id.fab_superfine)
    public void pressSuperfine(View view) {
        float superfineBrush = getResources().getInteger(R.integer.superfine_size);
        presenter.onBrushClicked(superfineBrush);
        setBrushSelection(view);
    }


    @OnClick(R.id.fab_color)
    public void pressColor() {
        presenter.onFabColorClick();
    }

    @OnClick(R.id.fab_erase)
    public void pressErase() {
        presenter.onFabEraseClick();
    }

    @OnClick(R.id.fab_new)
    public void pressNew() {
        presenter.onFabNewClick();
    }


    public void setBrushSelection(View view) {
        if (pressedFab != null) {
            pressedFab.setBackgroundTintList(ColorStateList.valueOf(getResources()
                    .getColor(R.color.colorFab)));
        }
        pressedFab = (FloatingActionButton) view;
        pressedFab.setBackgroundTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.colorPressedFab)));
        animateFAB();
    }

    @Override
    public void showColorDialog() {
        new ChromaDialog.Builder()
                .initialColor(presenter.getColor())
                .colorMode(ColorMode.RGB)
                .indicatorMode(IndicatorMode.HEX)
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int newColor) {
                        presenter.onColorSelected(newColor);
                    }
                })
                .create()
                .show(getSupportFragmentManager(), getString(R.string.color_dialog_tag));
    }

    @Override
    public void setEraseFabBackground(int tintId) {
        int tintColor = ContextCompat.getColor(this, tintId);
        fabErase.setBackgroundTintList(ColorStateList.valueOf(tintColor));
    }

    @Override
    public void showNewDialog() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle(R.string.new_drawing);
        newDialog.setMessage(R.string.new_draw_msg);
        newDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onNewDialogClick();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton(R.string.cancel, null);
        newDialog.show();
    }

    @Override
    public void showSaveDialog() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle(R.string.save_drawing);
        saveDialog.setMessage(R.string.save_draw_msg);
        saveDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.onSaveDialogClick();
            }
        });
        saveDialog.setNegativeButton(R.string.cancel, null);
        saveDialog.show();
    }

    @Override
    public String saveImage() {
        //Does this need to be done on background thread?
        return MediaStore.Images.Media.insertImage(
                getContentResolver(),
                presenter.getDrawingBitmap(),
                UUID.randomUUID().toString() + ".png", getString(R.string.save_image_desc));
    }

    @Override
    public File getFileFromBitmap(Bitmap bitmap) {
        File file = new File(this.getCacheDir(), "shareImg.png");
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Share Failed");
        }

        return file;
    }

    @Override
    public void sendShareIntent(File file) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("image/png");
        this.startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    @Override
    public void showToast(String toastString) {
        Toast.makeText(this, toastString,
                Toast.LENGTH_SHORT).show();
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
                presenter.onClickShare();
                return true;
            case R.id.action_save:
                presenter.onClickSave();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //is there a better way to implement this animation?
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

}

