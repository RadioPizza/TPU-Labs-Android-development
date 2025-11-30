package ru.olegkravtsov.lab27

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ballPaint = Paint().apply {
        color = context.getColor(R.color.ball_color)
        isAntiAlias = true
    }

    private var ballX = 0f
    private var ballY = 0f
    private var ballRadius = resources.getDimension(R.dimen.ball_radius)

    private var velocityX = 0f
    private var velocityY = 0f

    private var gravityX = 0f
    private var gravityY = 0f

    private var lastPhysicsUpdate = System.currentTimeMillis()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ballX = w / 2f
        ballY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(ballX, ballY, ballRadius, ballPaint)
    }

    fun updatePhysics() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = (currentTime - lastPhysicsUpdate) / 1000f

        if (deltaTime > 0) {
            velocityX += gravityX * deltaTime * 50
            velocityY += gravityY * deltaTime * 50

            ballX += velocityX * deltaTime * 50
            ballY += velocityY * deltaTime * 50

            checkBoundaryCollision()
            invalidate()
        }

        lastPhysicsUpdate = currentTime
    }

    private fun checkBoundaryCollision() {
        if (ballX - ballRadius < 0) {
            ballX = ballRadius
            velocityX = -velocityX * 0.8f
        } else if (ballX + ballRadius > width) {
            ballX = width - ballRadius
            velocityX = -velocityX * 0.8f
        }

        if (ballY - ballRadius < 0) {
            ballY = ballRadius
            velocityY = -velocityY * 0.8f
        } else if (ballY + ballRadius > height) {
            ballY = height - ballRadius
            velocityY = -velocityY * 0.8f
        }
    }

    fun setGravity(gx: Float, gy: Float) {
        gravityX = -gx
        gravityY = gy
    }

    fun getBallPosition(): String {
        return "X: ${"%.2f".format(ballX)}, Y: ${"%.2f".format(ballY)}"
    }

    fun getGravityValues(): String {
        return "GX: ${"%.2f".format(gravityX)}, GY: ${"%.2f".format(gravityY)}"
    }

    fun getVelocityValues(): String {
        return "VX: ${"%.2f".format(velocityX)}, VY: ${"%.2f".format(velocityY)}"
    }
}