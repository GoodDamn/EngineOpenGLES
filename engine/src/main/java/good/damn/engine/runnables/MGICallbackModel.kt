package good.damn.engine.runnables

import good.damn.engine.opengl.models.MGMPoolVertexArray
import good.damn.engine.opengl.objects.MGObject3d

interface MGICallbackModel {
    fun onGetObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    )
}