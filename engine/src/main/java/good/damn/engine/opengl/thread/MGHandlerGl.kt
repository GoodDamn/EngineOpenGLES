package good.damn.engine.opengl.thread

import good.damn.engine.opengl.runnables.MGIRunnableBounds
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGl(
    private val queue: ConcurrentLinkedQueue<MGIRunnableBounds>,
    private val queueCycle: ConcurrentLinkedQueue<MGIRunnableBounds>
) {
    fun registerCycleTask(
        run: MGIRunnableBounds
    ) {
        queueCycle.add(
            run
        )
    }

    fun post(
        run: MGIRunnableBounds
    ) {
        queue.add(
            run
        )
    }
}