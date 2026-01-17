package good.damn.wrapper.pools

import android.util.SparseArray
import good.damn.engine.MGObject3d
import good.damn.engine.models.MGMInformator
import good.damn.apigl.arrays.MGArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.MGPointerAttribute
import good.damn.apigl.drawers.MGDrawerVertexArray
import good.damn.apigl.runnables.MGRunglConfigVertexArray
import good.damn.logic.triggers.LGTriggerMesh
import good.damn.wrapper.models.MGMPoolMesh

class MGPoolMeshesStatic {

    private val map = SparseArray<
        Array<MGMPoolMesh>
    >()

    fun remove(
        fileNameModel: String
    ) {
        map.remove(
            fileNameModel.hashCode()
        )
    }

    fun loadOrGetFromCache(
        fileNameModel: String,
        informator: MGMInformator
    ): Array<MGMPoolMesh>? {
        get(fileNameModel)?.run {
            return this
        }

        val objs = MGObject3d.createFromAssets(
            "objs/$fileNameModel"
        ) ?: return null

        val obj = objs[0]

        val triggerPoint = LGTriggerMesh.createTriggerPoint(
            obj
        )

        val triggerMatrix = LGTriggerMesh.createTriggerPointMatrix(
            triggerPoint
        )

        val configurator = good.damn.apigl.arrays.MGArrayVertexConfigurator(
            obj.config
        )

        informator.glHandler.post(
            good.damn.apigl.runnables.MGRunglConfigVertexArray(
                configurator,
                obj.vertices,
                obj.indices,
                good.damn.apigl.arrays.pointers.MGPointerAttribute.defaultNoTangent
            )
        )

        val poolMesh = arrayOf(
            MGMPoolMesh(
                good.damn.apigl.drawers.MGDrawerVertexArray(
                    configurator
                ),
                triggerPoint
            )
        )

        set(
            fileNameModel,
            poolMesh
        )

        return poolMesh
    }

    private operator fun set(
        fileNameModel: String,
        arrayVertex: Array<MGMPoolMesh>
    ) {
        map[
            fileNameModel.hashCode()
        ] = arrayVertex
    }

    private operator fun get(
        fileNameModel: String
    ) = map[
        fileNameModel.hashCode()
    ]
}