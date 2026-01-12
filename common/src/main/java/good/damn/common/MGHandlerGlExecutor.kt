package good.damn.common

import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGlExecutor {

    val queue = ConcurrentLinkedQueue<COIRunnableBounds>()
    val queueCycle = ConcurrentLinkedQueue<COIRunnableBounds>()

    fun runTasksBounds(
        width: Int,
        height: Int
    ) {
        while (queue.isNotEmpty()) {
            queue.remove().run(
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