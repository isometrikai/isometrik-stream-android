package io.isometrik.gs.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.streams.list.StreamsListActivity;
import io.isometrik.gs.ui.users.list.UsersActivity;

/**
 * The type Splash activity.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent;
    if (IsometrikStreamSdk.getInstance().getUserSession().getUserId() == null) {

      intent = new Intent(SplashActivity.this, UsersActivity.class);
    } else {
      intent = new Intent(SplashActivity.this, StreamsListActivity.class);
    }
    startActivity(intent);
    finish();
  }
}
