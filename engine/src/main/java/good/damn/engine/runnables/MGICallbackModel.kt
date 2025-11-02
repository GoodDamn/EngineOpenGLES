package good.damn.engine.runnables

import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.models.MGMPoolMesh

interface MGICallbackModel {
    fun onGetObjectsCached(
        poolMesh: Array<MGMPoolMesh>
    )

    fun onGetObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    )
}