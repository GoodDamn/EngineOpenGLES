package good.damn.engine.imports

import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.drawers.MGDrawerMeshInstanced
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportLevel(
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>
): MGImportFile {

    final override fun onImportFile(
        it: File
    ) {
        val stream = MGStreamLevel(
            FileInputStream(
                it
            )
        )

        stream.read()?.forEach {
            meshesInstanced.add(
                MGDrawerMeshInstanced(
                    it.vertexArray
                )
            )
        }
    }

}