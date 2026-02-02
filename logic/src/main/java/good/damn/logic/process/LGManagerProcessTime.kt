package good.damn.logic.process

import android.os.Handler
import android.os.Looper
import good.damn.engine.sdk.process.SDIProcessTime
import java.util.LinkedList

class LGManagerProcessTime {

    private val mHandler = Handler(
        Looper.getMainLooper()
    )

    private val mLoopRunnables = LinkedList<
        SDIProcessTime
    >()

    private val mRunnable = LGRunnableProcessTimeLoop(
        mLoopRunnables,
        mHandler,
        6L
    )

    fun registerLoopProcessTime(
        processTime: SDIProcessTime
    ) {
        mLoopRunnables.add(
            processTime
        )
    }

    fun unregisterAll() {
        mLoopRunnables.clear()
    }

    fun unregisterLoopProcessTime(
        processTime: SDIProcessTime
    ) {
        mLoopRunnables.remove(
            processTime
        )
    }

    fun start() {
        mRunnable.isRunning = true
        mHandler.post(
            mRunnable
        )
    }

    fun stop() {
        mRunnable.isRunning = false
        mHandler.removeCallbacks(
            mRunnable
        )
    }

}