package good.damn.engine.runnables

import android.os.Handler
import good.damn.engine.sdk.process.SDIProcessTime

class MGRunnableProcessTimeLoop(
    private val callbacksProcessTime: List<SDIProcessTime>,
    private val handler: Handler
): Runnable {
    var isRunning = false

    private var mCurrentTime = System.currentTimeMillis()
    private var mPrevTime = mCurrentTime
    private var mDtTime = 0L

    final override fun run() {
        mCurrentTime = System.currentTimeMillis()
        mDtTime = mCurrentTime - mPrevTime
        mPrevTime = mCurrentTime

        val dt = mDtTime.toFloat() / 1000f
        callbacksProcessTime.forEach {
            it.onProcessTime(dt)
        }
        if (isRunning) {
            handler.postDelayed(
                this,
                16L
            )
        }
    }
}