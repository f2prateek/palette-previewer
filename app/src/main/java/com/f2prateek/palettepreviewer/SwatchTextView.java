package com.f2prateek.palettepreviewer;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.widget.TextView;

public class SwatchTextView extends TextView {
  public SwatchTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setSwatch(Palette.Swatch swatch) {
    if (swatch != null) {
      setBackgroundColor(swatch.getRgb());
      setTextColor(swatch.getTitleTextColor());
    }
  }
}
