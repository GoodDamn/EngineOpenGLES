package good.damn.engine.opengl.thread

import java.util.LinkedList

class MGHandlerGL: Runnable {

    private val mQueue = LinkedList<Runnable>()

    override fun run() {
        while (mQueue.isNotEmpty()) {
            mQueue.first.run()
            mQueue.removeFirst()
        }
    }

    fun post(r: Runnable) {
        mQueue.add(
            r
        )
    }
}