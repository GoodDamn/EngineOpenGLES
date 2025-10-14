package good.damn.engine.runnables

import good.damn.engine.opengl.MGObject3d

interface MGICallbackModel {
    fun onGetObjects(
        objs: Array<MGObject3d>?
    )
}