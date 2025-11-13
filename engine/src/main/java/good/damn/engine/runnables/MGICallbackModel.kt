package good.damn.engine.runnables

import good.damn.engine.opengl.models.MGMPoolMesh
import good.damn.engine.opengl.objects.MGObject3d

interface MGICallbackModel {
    fun onGetObjectsCached(
        poolMesh: Array<MGMPoolMesh>
    )

    fun onGetObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    )
}