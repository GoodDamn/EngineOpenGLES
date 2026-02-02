package good.damn.logic.process

import android.os.Handler
import android.os.SystemClock
import good.damn.engine.sdk.process.SDIProcessTime

class LGRunnableProcessTimeLoop(
    private val callbacksProcessTime: List<SDIProcessTime>,
    private val handler: Handler,
    private val delayMillis: Long
): Runnable {
    var isRunning = false

    private var mCurrentTime = SystemClock.elapsedRealtime()
    private var mPrevTime = mCurrentTime
    private var mDtTime = 0L

    final override fun run() {
        mCurrentTime = SystemClock.elapsedRealtime()
        mDtTime = mCurrentTime - mPrevTime
        mPrevTime = mCurrentTime

        val dt = mDtTime.toFloat() / 1000f
        callbacksProcessTime.forEach {
            it.onProcessTime(dt)
        }
        if (isRunning) {
            handler.postDelayed(
                this,
                delayMillis
            )
        }
    }
}