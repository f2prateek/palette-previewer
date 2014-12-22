package com.f2prateek.palettepreviewer;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import com.squareup.picasso.Transformation;

public final class PaletteTransformation implements Transformation {
  private static final String PALETTE_KEY = "palette";
  private Palette palette;

  public Palette getPalette() {
    if (palette == null) {
      throw new IllegalStateException("Transformation was not run.");
    }
    return palette;
  }

  @Override public Bitmap transform(Bitmap source) {
    palette = Palette.generate(source);
    return source;
  }

  @Override public String key() {
    return PALETTE_KEY;
  }
}