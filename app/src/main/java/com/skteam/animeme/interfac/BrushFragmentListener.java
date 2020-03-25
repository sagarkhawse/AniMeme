package com.skteam.animeme.interfac;

public interface BrushFragmentListener {
    void onBrushSizeChnagedListener(float size);
    void onBrushOpacityChangedListener(int opacity);
    void onBrushColorChnagedListener(int color);
    void onBrushStateChnagedListener(boolean isEraser);
}
