package good.damn.engine.runnables

import good.damn.engine.imports.MGImportLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.opengl.drawers.MGDrawerMeshInstanced
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.shaders.MGIShaderNormal
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGRunnableImportLevel(
    private val file: File,
    private val levelImport: MGImportLevel
): Runnable {

    override fun run() {
        if (!file.exists()) {
            return
        }

        val stream = MGStreamLevel(
            FileInputStream(
                file
            )
        )
        file.delete()
        stream.read()?.run {
            levelImport.onImport(
                this
            )
        }
    }
}