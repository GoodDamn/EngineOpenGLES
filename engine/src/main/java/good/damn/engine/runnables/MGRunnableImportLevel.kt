package good.damn.engine.runnables

import good.damn.engine.level.MGStreamLevel
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.shaders.MGIShaderNormal
import java.io.File
import java.io.FileInputStream

class MGRunnableImportLevel(
    private val file: File
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

        val level = stream.read()


    }
}