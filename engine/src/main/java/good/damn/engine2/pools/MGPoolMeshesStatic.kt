package good.damn.engine2.pools

import android.util.SparseArray
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.runnables.GLRunglConfigVertexArray
import good.damn.common.COHandlerGl
import good.damn.engine.ASObject3d
import good.damn.logic.utils.LGUtilsTrigger
import java.io.File

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
        file: File,
    ): Array<MGMPoolMesh>? {
        get(file.path)?.run {
            return this
        }

        val objs = ASObject3d.createFromFile(
            file
        ) ?: return null

        val obj = objs[0]

        val triggerPoint = LGUtilsTrigger.createTriggerPoint(
            obj.vertices
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
            file.path,
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