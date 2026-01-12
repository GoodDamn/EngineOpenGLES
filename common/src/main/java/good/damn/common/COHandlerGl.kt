package good.damn.common

import java.util.concurrent.ConcurrentLinkedQueue

class COHandlerGl(
    private val queue: ConcurrentLinkedQueue<COIRunnableBounds>,
    private val queueCycle: ConcurrentLinkedQueue<COIRunnableBounds>
) {
    fun registerCycleTask(
        run: COIRunnableBounds
    ) {
        queueCycle.add(
            run
        )
    }

    fun post(
        run: COIRunnableBounds
    ) {
        queue.add(
            run
        )
    }
}