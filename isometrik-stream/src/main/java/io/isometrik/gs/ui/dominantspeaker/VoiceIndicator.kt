package io.isometrik.gs.ui.dominantspeaker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import io.isometrik.gs.ui.R

class VoiceIndicator : View {

  private val delays = ArrayList<Int>()
  private lateinit var translateYFloats: FloatArray
  private lateinit var translateCopy: FloatArray
  private lateinit var translateTemp: FloatArray
  private lateinit var colorsStop: IntArray
  private lateinit var colorsUser: IntArray
  private lateinit var colorsSystem: IntArray
  private val animators = ArrayList<ValueAnimator>()
  private var isFirst = true
  private var isRunning = false
  private var isStopping = false
  private var isInterceptor = false
  private var mBallSize: Int = 0
  private var mDecibel = 30.0f
  private var mRadius = 20f
  private var mType = 0
  private var mTempType = 0
  private val mCircleSpacing = 10f

  constructor(context: Context?) : super(context) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(
    context,
    attrs
  ) {
    getAttrs(attrs)
    init()
  }

  constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
    getAttrs(attrs, defStyleAttr)
    init()
  }

  private fun getAttrs(attrs: AttributeSet?) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VoiceIndicator)
    setTypeArray(typedArray)
    init()
  }

  private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
    val typedArray = context.obtainStyledAttributes(
      attrs,
      R.styleable.VoiceIndicator,
      defStyle,
      0
    )
    setTypeArray(typedArray)
    init()
  }

  private fun setTypeArray(typedArray: TypedArray) {
    mRadius = typedArray.getDimension(R.styleable.VoiceIndicator_voice_indicator_radius, mRadius)
    typedArray.recycle()
  }

  private fun init() {
    colorsStop = resources.getIntArray(R.array.default_ball_colors)
    colorsUser = resources.getIntArray(R.array.default_ball_colors)
    colorsSystem = resources.getIntArray(R.array.default_ball_colors)
    mBallSize = colorsStop.size - 1

    setArraySize(colorsStop.size)
  }

  private fun setDelay() {
    delays.clear()
    for (i in 1..mBallSize + 1) {
      delays.add(70 * i)
    }
  }

  /**
   * ball radius
   */
  fun setRadius(radius: Float) {
    mRadius = radius
    invalidate()
  }

  /**
   * color of balls
   */
  fun setBallColors(colors: IntArray) {
    colorsStop = colors
    colorsUser = colors
    mBallSize = colorsStop.size - 1
    setArraySize(colorsStop.size)
  }

  private fun setArraySize(size: Int) {
    translateYFloats = FloatArray(size)
    translateCopy = FloatArray(size)
    translateTemp = FloatArray(size)
    setDelay()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    var calWidth = 0.0f
    val padding = 50
    calWidth += mRadius * 2 * mBallSize + mCircleSpacing * mBallSize + padding
    setMeasuredDimension(calWidth.toInt(), resources.getDimension(R.dimen.ism_dp_40).toInt())
  }

  @SuppressLint("DrawAllocation")
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val x = (width / 2) - (mRadius * 2 * mBallSize + mCircleSpacing * mBallSize) / 2
    for (i in 0..mBallSize) {
      canvas.save()
      val translateX = x + mRadius * 2 * i + mCircleSpacing * i
      val paintUpperBall = Paint()
      val paintUpperLine = Paint()
      if (isFirst) {
        val yPos = height / 2
        translateYFloats[i] = yPos.toFloat()
        translateCopy[i] = yPos.toFloat()
        translateTemp[i] = yPos.toFloat()
        if (i == mBallSize) {
          isFirst = false
        }
      }
      when (mType) {
        STOP -> {
          paintUpperBall.color = colorsStop[i]
          paintUpperLine.color = colorsStop[i]
        }
        START_USER -> {
          paintUpperBall.color = colorsUser[i]
          paintUpperLine.color = colorsUser[i]
        }
        START_SYSTEM -> {
          paintUpperBall.color = colorsSystem[i]
          paintUpperLine.color = colorsSystem[i]
        }
      }
      paintUpperLine.strokeWidth = mRadius * 2
      canvas.drawLine(
        translateX,
        translateCopy[i],
        translateX,
        translateYFloats[i],
        paintUpperLine
      )
      canvas.translate(translateX, translateYFloats[i])
      canvas.drawCircle(0f, 0f, mRadius, paintUpperBall)
      canvas.restore()
    }
  }

  /**
   * @param type START_USER, START_SYSTEM
   */
  fun startAnimation(type: Int) {
    mTempType = type
    mType = type
    val dB = db
    oneCycleAnimation(dB)
  }

  private fun oneCycleAnimation(decibel: Float) {
    mType = mTempType
    if (isRunning) return
    isRunning = true
    clearAnim()
    var boundHeight: Float
    for (i in 0..mBallSize) {
      boundHeight = if (i % 2 != 0) decibel * 0.7f else decibel

      val scaleAnim = ValueAnimator.ofFloat(translateTemp[i], height / 2 - boundHeight)
      scaleAnim.duration = 200
      scaleAnim.repeatCount = 0
      scaleAnim.addUpdateListener(AnimatorUpdateListener { animation ->
        if (isInterceptor) return@AnimatorUpdateListener
        val height = animation.animatedValue as Float
        translateYFloats[i] = height
        postInvalidate()
      })
      scaleAnim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          super.onAnimationEnd(animation)
          translateTemp[i] = translateYFloats[i]
          if (i == mBallSize) {
            isRunning = false
            if (isInterceptor) return
            oneCycleAnimation(db)
          }
        }
      })
      animators.add(scaleAnim)

    }
    for (animator in animators) {
      animator.start()
    }
  }

  /**
   * stop Animation
   */
  fun stopAnimation() {
    isInterceptor = true
    if (isStopping) return
    isStopping = true
    clearAnim()
    for (i in 0..mBallSize) {
      val scaleAnim =
        ValueAnimator.ofFloat(translateYFloats[i], translateCopy[i])
      scaleAnim.duration = 400
      scaleAnim.repeatCount = 0
      scaleAnim.startDelay = delays[i].toLong()
      scaleAnim.addUpdateListener { animation ->
        val height = animation.animatedValue as Float
        translateYFloats[i] = height
        postInvalidate()
      }
      scaleAnim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          super.onAnimationEnd(animation)
          if (i == mBallSize) {
            isStopping = false
            isInterceptor = false
            mType = STOP
            invalidate()
          }
        }
      })
      animators.add(scaleAnim)
    }
    for (animator in animators) {
      animator.start()
    }
  }

  /**
   * clear Animation
   */
  private fun clearAnim() {
    for (animator in animators) {
      animator.cancel()
      animator.addUpdateListener(null)
    }
    animators.clear()
  }

  var db: Float
    get() {
      if (mType == START_SYSTEM) {
        mDecibel = RisingVoiceIndicator.systemDecibel
      }
      return mDecibel
    }
    set(dB) {
      if (0 < dB && dB < 100) mDecibel = if (dB < 50) {
//      if (0 < dB && dB < 255) mDecibel = if (dB < 128) {
        dB * 0.4f
      } else {
        dB * 0.8f
      }
    }

  companion object {
    const val STOP = 0
    const val START_USER = 1
    const val START_SYSTEM = 2
  }
}