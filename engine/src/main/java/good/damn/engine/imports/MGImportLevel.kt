package good.damn.engine.imports

import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.drawers.MGDrawerMeshInstanced
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderDefault
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportLevel(
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>,
    private val shaderDefault: MGShaderDefault,
    private val poolTextures: MGPoolTextures
): MGImportFile {

    final override fun onImportFile(
        it: File
    ) {
        MGStreamLevel.read(
            FileInputStream(
                it
            ),
            shaderDefault.material,
            poolTextures
        )?.forEach {
            meshesInstanced.add(
                MGDrawerMeshInstanced(
                    it.vertexArray,
                    it.material
                )
            )
        }
    }

}