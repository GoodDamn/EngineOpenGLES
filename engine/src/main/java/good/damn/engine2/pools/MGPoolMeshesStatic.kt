package good.damn.engine2.pools

import android.util.SparseArray
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.runnables.GLRunglConfigVertexArray
import good.damn.common.COHandlerGl
import good.damn.engine.ASObject3d
import good.damn.logic.triggers.LGTriggerMesh

class MGPoolMeshesStatic(
    private val glHandler: COHandlerGl
) {

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
    ): Array<MGMPoolMesh>? {
        get(fileNameModel)?.run {
            return this
        }

        val objs = ASObject3d.createFromAssets(
            "objs/$fileNameModel"
        ) ?: return null

        val obj = objs[0]

        val triggerPoint = LGTriggerMesh.createTriggerPoint(
            obj
        )

        val configurator = GLArrayVertexConfigurator(
            obj.config
        )

        glHandler.post(
            GLRunglConfigVertexArray(
                configurator,
                obj.vertices,
                obj.indices,
                GLPointerAttribute.defaultNoTangent
            )
        )

        val poolMesh = arrayOf(
            MGMPoolMesh(
                GLDrawerVertexArray(
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