package io.isometrik.gs.ui.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import java.util.Timer;
import java.util.TimerTask;

public class TimeDownView extends AppCompatTextView {
  private Timer timer;
  private DownTimerTask downTimerTask;
  private int downCount;
  private int lastDown;
  private final long intervalMills = 1000;
  private long delayMills;
  ScaleAnimation scaleAnimation;

  public TimeDownView(Context context) {
    this(context, null);
  }

  public TimeDownView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TimeDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
    this.setTextSize(sp2px(context, 90));
  }

  private void init() {
    if (timer == null) {
      timer = new Timer();
    }
    if (downHandler == null) {
      downHandler = new DownHandler();
    }
    setGravity(Gravity.CENTER);
    initDefaultAnimate();
  }

  /**
   * start the timer
   */
  public void downSecond(int seconds) {
    downTime(seconds, 0, 0);
  }

  /**
   * Countdown on
   *
   * @param downCount Total countdown
   * @param lastDown Displayed last countdown
   * @param delayMills Delay start countdown (ms)
   */
  public void downTime(int downCount, int lastDown, long delayMills) {
    this.downCount = downCount;
    this.lastDown = lastDown;
    this.delayMills = delayMills;

    if (downTimerTask == null) {
      downTimerTask = new DownTimerTask();
    }
    try {
      timer.schedule(downTimerTask, delayMills, intervalMills);
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    if (View.VISIBLE == visibility) {
      drawTextFlag = DRAW_TEXT_YES;
    } else {
      drawTextFlag = DRAW_TEXT_NO;
      if (downTimerTask != null) {
        downTimerTask.cancel();
        downTimerTask = null;
      }
    }
  }

  public void destroy() {
    if (downTimerTask != null) {
      downTimerTask.cancel();
      downTimerTask = null;
    }
    if (timer != null) {
      timer.cancel();
      timer.purge();
      timer = null;
    }
    if (downHandler != null) downHandler.removeCallbacksAndMessages(null);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (drawTextFlag == DRAW_TEXT_NO) {
      return;
    }
    super.onDraw(canvas);
  }

  private class DownTimerTask extends TimerTask {

    @Override
    public void run() {
      if (downCount >= (lastDown - 1)) {
        Message msg = Message.obtain();
        msg.what = 1;
        downHandler.sendMessage(msg);
      }
    }
  }

  public interface DownTimeWatcher {
    void onTime(int num);

    void onLastTime(int num);

    void onLastTimeFinish(int num);
  }

  private DownTimeWatcher downTimeWatcher = null;

  /**
   * Listen for countdown changes
   */
  public void setOnTimeDownListener(DownTimeWatcher downTimeWatcher) {
    this.downTimeWatcher = downTimeWatcher;
  }

  private DownHandler downHandler;

  private class DownHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.what == 1) {
        if (downTimeWatcher != null) {
          downTimeWatcher.onTime(downCount);
        }
        if (downCount >= (lastDown - 1)) {
          /*
           * Before the end
           * */
          if (downCount >= lastDown) {
            setText(downCount + 1 + "");
            startDefaultAnimate();
            if (downCount == lastDown && downTimeWatcher != null) {
              downTimeWatcher.onLastTime(downCount);
            }
          }
          /*
           * When the countdown ends
           * */
          else if (downCount == (lastDown - 1)) {
            /*
             * If lastDown is 0, downCount == -1 is when the countdown really ends.
             * */
            if (downTimeWatcher != null) {
              downTimeWatcher.onLastTimeFinish(downCount);
            }
            /*
             * The countdown is over. Although the setText () method triggers onDraw, it is overridden so that it does not draw and sets the mark to not draw.
             * */
            if (afterDownDismissFlag == AFTER_LAST_TIME_DISMISS) {
              drawTextFlag = DRAW_TEXT_NO;
            }
            /*
             * refresh
             * */
            invalidate();
          }
          downCount--;
        }
        //
      }
    }
  }

  private final int DRAW_TEXT_YES = 1;
  private final int DRAW_TEXT_NO = 0;
  /**
   * Whether to execute the onDraw logo, the default draw
   */
  private int drawTextFlag = DRAW_TEXT_YES;

  private final int AFTER_LAST_TIME_DISMISS = 1;
  private final int AFTER_LAST_TIME_NODISMISS = 0;
  /**
   * The sign of whether the text disappears after the countdown ends. It disappears by default.
   */
  private int afterDownDismissFlag = AFTER_LAST_TIME_DISMISS;

  /**
   * The text does not disappear after setting the countdown
   */
  public void setAfterDownNoDismiss() {
    afterDownDismissFlag = AFTER_LAST_TIME_NODISMISS;
  }

  /**
   * Text disappears after setting the countdown
   */
  public void setAfterDownDismiss() {
    afterDownDismissFlag = AFTER_LAST_TIME_DISMISS;
  }

  private final boolean startDefaultAnimFlag = true;

  public void closeDefaultAnimate() {
    clearAnimation();
  }

  private void startDefaultAnimate() {
    if (startDefaultAnimFlag) {
      startAnimation(scaleAnimation);
    }
  }

  private void initDefaultAnimate() {
    if (scaleAnimation == null) {
      scaleAnimation =
          new ScaleAnimation(1f, 0.6f, 1f, 0.6f, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
      scaleAnimation.setDuration(intervalMills);
    }
  }

  /**
   * Convert sp value to px value, keep the text size unchanged
   */
  private int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }
}