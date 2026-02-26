package good.damn.engine.sdk.managers

import good.damn.engine.sdk.process.SDIProcessTime
import java.util.LinkedList

class SDManagerProcessTime(
    private val runnablesLoop: LinkedList<SDIProcessTime>
) {

    fun registerLoopProcessTime(
        v: SDIProcessTime
    ) = runnablesLoop.add(v)

    fun unregisterLoopProcessTime(
        v: SDIProcessTime
    ) = runnablesLoop.remove(v)

}