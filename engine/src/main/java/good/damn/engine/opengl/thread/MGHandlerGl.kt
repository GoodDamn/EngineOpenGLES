package good.damn.engine.opengl.thread

import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGl
: Runnable {

    private val mQueue = ConcurrentLinkedQueue<Runnable>()

    override fun run() {
        while (mQueue.isNotEmpty()) {
            mQueue.remove().run()
        }
    }

    fun post(
        run: Runnable
    ) = mQueue.add(
        run
    )
}