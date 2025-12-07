package good.damn.engine.opengl.thread

import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGl(
    private val queue: ConcurrentLinkedQueue<Runnable>,
    private val queueCycle: ConcurrentLinkedQueue<Runnable>
) {
    fun registerCycleTask(
        run: Runnable
    ) {
        queue.add(
            run
        )
    }

    fun post(
        run: Runnable
    ) = queue.add(
        run
    )
}