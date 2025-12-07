package good.damn.engine.opengl.executor

import good.damn.engine.opengl.runnables.MGIRunnableBounds
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGlExecutor {

    val queue = ConcurrentLinkedDeque<MGIRunnableBounds>()
    val queueCycle = ConcurrentLinkedQueue<MGIRunnableBounds>()

    fun runTasksBounds(
        width: Int,
        height: Int
    ) {
        while (queue.isNotEmpty()) {
            queue.removeLast().run(
                width, height
            )
        }
    }

    fun runCycle(
        width: Int,
        height: Int
    ) {
        for (task in queueCycle) {
            task.run(
                width,
                height
            )
        }
    }
}