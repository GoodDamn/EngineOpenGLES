package good.damn.opengles_engine.opengl.thread

import java.util.LinkedList

class GLHandler: Runnable {

    private val mQueue = LinkedList<Runnable>()

    override fun run() {
        mQueue.forEach {
            it.run()
            mQueue.removeFirst()
        }
    }

    fun post(r: Runnable) {
        mQueue.add(
            r
        )
    }
}