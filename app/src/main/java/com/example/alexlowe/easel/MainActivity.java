package com.example.alexlowe.easel;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.drawing) DrawingView drawingView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_fine) FloatingActionButton fabFine;
    @BindView(R.id.fab_superfine) FloatingActionButton fabSuperfine;
    @BindView(R.id.fab_small) FloatingActionButton fabSmall;
    @BindView(R.id.fab_medium) FloatingActionButton fabMedium;
    @BindView(R.id.fab_large) FloatingActionButton fabLarge;
    @BindView(R.id.fab_color)
    FloatingActionButton fabColor;
    @BindView(R.id.fab_erase)
    FloatingActionButton fabErase;
    @BindView(R.id.fab_new)
    FloatingActionButton fabNew;

    private Animation fabOpen, fabClose;

    private Boolean isFabOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
    }


    @OnClick({R.id.fab, R.id.fab_color, R.id.fab_erase, R.id.fab_new, R.id.fab_large, R.id.fab_medium, R.id.fab_small,
            R.id.fab_fine, R.id.fab_superfine})
    public void pressFab(View view){
        float superfineBrush = getResources().getInteger(R.integer.superfine_size);
        float fineBrush = getResources().getInteger(R.integer.fine_size);
        float mediumBrush = getResources().getInteger(R.integer.medium_size);
        float largeBrush = getResources().getInteger(R.integer.large_size);
        float smallBrush = getResources().getInteger(R.integer.small_size);

        switch (view.getId()){
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
            case R.id.fab_superfine:
                clickBrush(superfineBrush);
                break;
            case R.id.fab_fine:
                clickBrush(fineBrush);
                break;
            case R.id.fab_small:
                clickBrush(smallBrush);
                break;
            case R.id.fab_medium:
                clickBrush(mediumBrush);
                break;
            case R.id.fab_large:
                clickBrush(largeBrush);
                break;
        }
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

    private void clickBrush(float brushSize){
        drawingView.setBrushSize(brushSize);
        animateFAB();
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
                    }
                })
                .create()
                .show(getSupportFragmentManager(), "dialog");
    }

    private void toggleErase() {
        drawingView.setErase(!drawingView.getErase());
        int eraseBackground = (drawingView.getErase()) ?
                getResources().getColor(R.color.colorErase) :
                getResources().getColor(R.color.colorFab);
        fabErase.setBackgroundTintList(ColorStateList.valueOf(eraseBackground));
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

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                layoutToolbar.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_UP:
                layoutToolbar.setVisibility(View.VISIBLE);
                break;
            default:
                return false;
        }
        return true;
    }*/

}
