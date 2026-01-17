package good.damn.engine.opengl.pools

import android.util.SparseArray
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.models.MGMPoolVertexArray
import good.damn.engine.MGObject3d
import good.damn.engine.opengl.triggers.MGTriggerMesh

class MGPoolMeshesStatic {

    private val map = SparseArray<
        Array<MGMPoolVertexArray>
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
    ): Array<MGMPoolVertexArray>? {
        get(fileNameModel)?.run {
            return this
        }

        val obj = MGObject3d.createFromAssets(
            "objs/$fileNameModel"
        ) ?: return null

        val poolMesh = arrayOf(
            MGTriggerMesh.createFromObject(
                obj[0],
                informator
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
        arrayVertex: Array<MGMPoolVertexArray>
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