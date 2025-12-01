package good.damn.engine.imports

import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.thread.MGHandlerGl
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportLevel(
    private val informator: MGMInformator
): MGImportFile {

    final override fun onImportFile(
        it: File,
        buffer: ByteArray
    ) {
        MGStreamLevel.readBin(
            MGFlowLevel {
                informator.meshesInstanced.add(
                    MGDrawerMeshInstanced(
                        it.enableCullFace,
                        it.vertexArray,
                        it.material
                    )
                )
            },
            FileInputStream(
                it
            ),
            informator.poolTextures,
            informator.glHandler,
            buffer
        )
    }

}