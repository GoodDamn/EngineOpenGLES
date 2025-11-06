package good.damn.engine.imports

import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.drawers.MGDrawerMeshInstanced
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportLevel(
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>
): MGIImport<Array<MGMMeshInstance>> {

    override fun onImport(
        it: Array<MGMMeshInstance>
    ) {
        it.forEach {
            meshesInstanced.add(
                MGDrawerMeshInstanced(
                    it.vertexArray,
                    it.matrices
                )
            )
        }
    }

}