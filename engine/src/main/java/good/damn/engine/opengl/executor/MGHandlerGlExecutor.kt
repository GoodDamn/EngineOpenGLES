package good.damn.engine.opengl.executor

import java.util.concurrent.ConcurrentLinkedQueue

class MGHandlerGlExecutor {

    val queue = ConcurrentLinkedQueue<Runnable>()
    val queueTasks = ConcurrentLinkedQueue<Runnable>()

    fun runTasks() {
        while (queue.isNotEmpty()) {
            queue.remove().run()
        }
    }

    fun runCycle() {
        for (task in queueTasks) {
            task.run()
        }
    }
}