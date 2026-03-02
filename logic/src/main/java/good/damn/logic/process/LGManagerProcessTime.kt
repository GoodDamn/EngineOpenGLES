package good.damn.logic.process

import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import good.damn.engine.sdk.managers.SDManagerProcessTime
import good.damn.engine.sdk.process.SDIProcessTime
import java.util.LinkedList

class LGManagerProcessTime {

    private val mHandler = Handler(
        Looper.getMainLooper()
    )

    private val mLoopRunnables = SparseArray<
        List<SDIProcessTime>
    >()

    private val mRunnable = LGRunnableProcessTimeLoop(
        mLoopRunnables,
        mHandler,
        6L
    )

    fun registerLoopProcessTime(
        processTime: List<SDIProcessTime>
    ) {
        mLoopRunnables.put(
            processTime.hashCode(),
            processTime
        )
    }

    fun unregisterAll() {
        mLoopRunnables.clear()
    }

    fun unregisterLoopProcessTime(
        processTime: List<SDIProcessTime>
    ) {
        mLoopRunnables.remove(
            processTime.hashCode()
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