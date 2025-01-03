package io.isometrik.gs.ui.dominantspeaker

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.dominantspeaker.Utils.Companion.getRandomNumber

class RisingVoiceIndicator : RelativeLayout {

  private var upperIndicator: VoiceIndicator? = null
  private var underIndicator: VoiceIndicator? = null
  private var mType = 0
  private var thread: Thread? = null
  private var mRadius = 20f
  private lateinit var mBallColors: IntArray

  constructor(context: Context?) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(
    context,
    attrs
  ) {
    getAttrs(attrs)
    init(context, attrs)

  }

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
    getAttrs(attrs, defStyleAttr)
    init(context, attrs)

  }

  private fun getAttrs(attrs: AttributeSet?) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RisingVoiceIndicator)
    setTypeArray(typedArray)
  }

  private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
    val typedArray = context.obtainStyledAttributes(
      attrs,
      R.styleable.RisingVoiceIndicator,
      defStyle,
      0
    )
    setTypeArray(typedArray)
  }

  private fun setTypeArray(typedArray: TypedArray) {
    mRadius = typedArray.getDimension(R.styleable.RisingVoiceIndicator_radius, mRadius)
    val colorsId = typedArray.getResourceId(
      R.styleable.RisingVoiceIndicator_ball_colors,
      R.array.default_ball_colors
    )
    mBallColors = typedArray.resources.getIntArray(colorsId)
    typedArray.recycle()
  }

  private fun init(context: Context, @Suppress("UNUSED_PARAMETER") attrs: AttributeSet) {
    val view = LayoutInflater.from(context).inflate(R.layout.ism_voice_indicator, this, true)
    upperIndicator = view.findViewById(R.id.upper_indicator)
    underIndicator = view.findViewById(R.id.under_indicator)

    setRadius(mRadius)
    setBallColors(mBallColors)
  }

  fun setRadius(radius: Float) {
    upperIndicator?.setRadius(radius)
    underIndicator?.setRadius(radius)
  }

  fun setBallColors(ballColors: IntArray) {
    upperIndicator?.setBallColors(ballColors)
    underIndicator?.setBallColors(ballColors)
  }

  @JvmOverloads
  fun start(type: Int = VoiceIndicator.START_USER) {
    mType = type
    makeSystemDecibel()
    upperIndicator?.startAnimation(type)
    underIndicator?.startAnimation(type)
  }

  fun stop() {
    stopSystemDecibel()
    upperIndicator?.stopAnimation()
    underIndicator?.stopAnimation()
  }

  fun setDecibel(dB: Float) {
    upperIndicator?.db = dB
    underIndicator?.db = dB
  }

  fun getDecibel(): Float? {
    return upperIndicator?.db
  }

  private fun makeSystemDecibel() {

    if (mType == VoiceIndicator.START_SYSTEM
      && thread == null
    ) {

      thread = Thread(Runnable {
        try {
          while (thread != null && !thread!!.isInterrupted) {
//            systemDecibel = getRandomNumber(45, 75).toFloat()
            systemDecibel = getRandomNumber(8, 32).toFloat()
            Thread.sleep(200)
          }
        } catch (e: InterruptedException) {
//          e.printStackTrace()
        }
      })
      thread!!.start()
    }
  }

  private fun stopSystemDecibel() {
    if (thread != null) {
      thread!!.interrupt()
      thread = null
//thread!!.stop()
    }
  }

  companion object {
    var systemDecibel = 0f
  }
}