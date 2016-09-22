package com.example.alexlowe.easel;

import org.junit.Test;
import org.mockito.Mockito;

public class DrawingPresenterTest {

    @Test
    public void testOnFabEraseClick() throws Exception {
        DrawingView drawingView = Mockito.mock(DrawingView.class);
        DrawingInterface drawingInterface = Mockito.mock(DrawingInterface.class);
        DrawingPresenter presenter = new DrawingPresenter(drawingView, drawingInterface);

        Mockito.when(drawingView.getErase()).thenReturn(true);
        presenter.onFabEraseClick();
        Mockito.verify(drawingInterface).setEraseFabBackground(R.color.colorPressedFab);

        Mockito.when(drawingView.getErase()).thenReturn(false);
        presenter.onFabEraseClick();
        Mockito.verify(drawingInterface).setEraseFabBackground(R.color.colorFab);
    }
}