package com.timmy.codelab.customview.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.view.isVisible
import com.timmy.codelab.customview.R

class CircleLoadingView : View {

    companion object {
        private const val CIRCLE_COUNT = 2
        private const val CIRCLE_DEFAULT_RADIUS = 5F // dp
        private const val CIRCLE_DEFAULT_SPACE = 6F // dp

        private const val ANIM_DURATION = 500L
        private const val ANIM_DELAY = 150L
    }

    private var circleRadius = CIRCLE_DEFAULT_RADIUS.toPx()
    private var circleSpace = CIRCLE_DEFAULT_SPACE.toPx()
    private var animYOffset = circleRadius

    private val yOffsets = mutableListOf<Float>()
    private val animatorSet = AnimatorSet()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        obtainAttrs(attrs)
        setupYOffsets()
        setupAnimatorSet()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animatorStartWhenVisible()
    }

    override fun onDetachedFromWindow() {
        animatorSet.cancel()
        super.onDetachedFromWindow()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        animatorStartWhenVisible()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startX = paddingStart + circleRadius
        val intervalX = ((circleRadius * 2) + circleSpace)

        val centerY = measuredHeight / 2.0F

        for (i in 0 until CIRCLE_COUNT) {
            val cx = startX + (i * intervalX)
            val cy = centerY - yOffsets[i]
            canvas?.drawCircle(cx, cy, circleRadius, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth: Int = (presentWidth() + paddingStart + paddingEnd).toInt()
        val measureHeight: Int = (presentHeight() + paddingTop + paddingBottom).toInt()
        val resolveMeasureWidth = resolveSize(measureWidth, widthMeasureSpec)
        val resolveMeasureHeight = resolveSize(measureHeight, heightMeasureSpec)
        setMeasuredDimension(resolveMeasureWidth, resolveMeasureHeight)
    }

    private fun presentWidth(): Float = (CIRCLE_COUNT * circleRadius * 2) + (CIRCLE_COUNT - 1) * circleSpace

    // 由於動畫會往上移動圓點半徑的高度，為了讓圓點置中且執行動畫時不會超出元件範圍，所以要特地加上 animYOffset * 2
    private fun presentHeight(): Float = (circleRadius * 2) + (animYOffset * 2)

    private fun obtainAttrs(attrs: AttributeSet?) {
        attrs?.run {
            context.obtainStyledAttributes(this, R.styleable.CircleLoadingView).run {
                circleRadius = getDimension(R.styleable.CircleLoadingView_circleRadius, circleRadius)
                circleSpace = getDimension(R.styleable.CircleLoadingView_circleSpace, circleSpace)
                paint.color = getColor(R.styleable.CircleLoadingView_circleColor, Color.GREEN)
                recycle()
            }
            animYOffset = circleRadius
        }
    }

    private fun setupYOffsets() {
        yOffsets.clear()
        for (i in 0 until CIRCLE_COUNT) {
            yOffsets.add(0F)
        }
    }

    private fun setupAnimatorSet() {

        val animators = mutableListOf<Animator>()

        for (i in 0 until CIRCLE_COUNT) {
            animators.add(ObjectAnimator.ofFloat(0f, animYOffset).apply {
                duration = ANIM_DURATION
                startDelay = i * ANIM_DELAY
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AnticipateInterpolator()
                addUpdateListener {
                    yOffsets[i] = it.animatedValue as Float
                    invalidate()
                }
            })
        }

        animatorSet.playTogether(animators)
    }

    private fun animatorStartWhenVisible() {
        if (isVisible) animatorSet.start() else animatorSet.cancel()
    }

    private fun Float.toPx(): Float {
        return this * Resources.getSystem().displayMetrics.density
    }
}