package good.damn.engine.runnables

import android.os.Handler
import android.os.Looper
import good.damn.engine.models.MGMInformator

class MGManagerTriggerLoop(
    informator: MGMInformator
) {
    private val mHandler = Handler(
        Looper.getMainLooper()
    )

    private val mRunnable = MGRunnableLoopTrigger(
        informator,
        mHandler
    )

    val isRunning: Boolean
        get() = mRunnable.isRunning

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

    private class MGRunnableLoopTrigger(
        private val informator: MGMInformator,
        private val handler: Handler
    ): Runnable {
        var isRunning = false

        override fun run() {
            informator.managerTriggerLight.loopTriggers(
                informator.camera.modelMatrix.x,
                informator.camera.modelMatrix.y,
                informator.camera.modelMatrix.z,
            )

            if (isRunning) {
                handler.post(
                    this
                )
            }
        }
    }

}