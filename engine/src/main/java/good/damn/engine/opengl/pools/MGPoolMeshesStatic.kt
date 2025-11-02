package good.damn.engine.opengl.pools

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.models.MGMPoolMesh

class MGPoolMeshesStatic {

    private val map = HashMap<
        String,
        Array<MGMPoolMesh>
    >()

    fun remove(
        name: String
    ) {
        map.remove(name)
    }

    operator fun set(
        name: String,
        arrayVertex: Array<MGMPoolMesh>
    ) {
        map[name] = arrayVertex
    }

    operator fun get(
        n: String
    ) = map[n]
}