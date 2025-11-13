package good.damn.engine.imports

import good.damn.engine.level.MGStreamLevel
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.pools.MGPoolTextures
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportLevel(
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>,
    private val poolTextures: MGPoolTextures
): MGImportFile {

    final override fun onImportFile(
        it: File
    ) {
        MGStreamLevel.readBin(
            FileInputStream(
                it
            ),
            poolTextures
        )?.forEach {
            it?.run {
                meshesInstanced.add(
                    MGDrawerMeshInstanced(
                        vertexArray,
                        material
                    )
                )
            }
        }
    }

}