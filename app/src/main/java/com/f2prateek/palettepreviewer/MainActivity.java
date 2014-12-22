package com.f2prateek.palettepreviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

public class MainActivity extends Activity {
  private static final int REQUEST_SELECT_PICTURE = 1;

  @InjectView(R.id.heroImage) ImageView heroImage;
  @InjectView(R.id.vibrantText) SwatchTextView vibrantText;
  @InjectView(R.id.vibrantDarkText) SwatchTextView vibrantDarkText;
  @InjectView(R.id.vibrantLightText) SwatchTextView vibrantLightText;
  @InjectView(R.id.mutedText) SwatchTextView mutedText;
  @InjectView(R.id.mutedDarkText) SwatchTextView mutedDarkText;
  @InjectView(R.id.mutedLightText) SwatchTextView mutedLightText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    if (!handleIntent(getIntent())) {
      loadImage(Uri.parse("http://i.imgur.com/CqmBjo5.jpg"));
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  /** Returns {@code true} if there we can handle the intent. */
  boolean handleIntent(Intent intent) {
    if (intent == null || !Intent.ACTION_SEND.equals(intent.getAction())) return false;

    Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (uri == null) return false;

    loadImage(uri);
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.actionPick:
        pickImage();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void pickImage() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    if (isAvailable(intent)) {
      startActivityForResult(Intent.createChooser(intent, getString(R.string.actionPick)),
          REQUEST_SELECT_PICTURE);
    } else {
      Toast.makeText(this, R.string.noAppsAvailable, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent result) {
    if (requestCode == REQUEST_SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
      if (result == null || result.getData() == null) {
        Toast.makeText(this, R.string.noImageSelected, Toast.LENGTH_LONG).show();
      } else {
        loadImage(result.getData());
      }
    } else {
      super.onActivityResult(requestCode, resultCode, result);
    }
  }

  void loadImage(Uri image) {
    final PaletteTransformation paletteTransformation = new PaletteTransformation();
    Picasso.with(this)
        .load(image)
        .fit()
        .centerCrop()
        .transform(paletteTransformation)
        .into(heroImage, new Callback.EmptyCallback() {
          @Override public void onSuccess() {
            Palette palette = paletteTransformation.getPalette();

            vibrantText.setSwatch(palette.getVibrantSwatch());
            vibrantDarkText.setSwatch(palette.getDarkVibrantSwatch());
            vibrantLightText.setSwatch(palette.getLightVibrantSwatch());

            mutedText.setSwatch(palette.getMutedSwatch());
            mutedDarkText.setSwatch(palette.getDarkMutedSwatch());
            mutedLightText.setSwatch(palette.getLightMutedSwatch());
          }
        });
  }

  /** Returns {@code true} if any apps are installed that can receive this intent. */
  public boolean isAvailable(Intent intent) {
    return getPackageManager().queryIntentActivities(intent, MATCH_DEFAULT_ONLY).size() > 0;
  }
}
