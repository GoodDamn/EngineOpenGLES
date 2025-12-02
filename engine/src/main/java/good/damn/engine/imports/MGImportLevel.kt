package good.damn.engine.imports

import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMMeshInstance
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
                val mesh = it.second ?: return@MGFlowLevel

                informator.meshesInstanced[
                    it.first
                ]?.run {
                    addMesh(
                        mesh,
                        this
                    )
                    return@MGFlowLevel
                }

                informator.meshesInstanced[
                    it.first
                ] = ConcurrentLinkedQueue<
                    MGDrawerMeshInstanced
                >().apply {
                    addMesh(
                        mesh,
                        this
                    )
                }
            },
            FileInputStream(
                it
            ),
            informator,
            buffer
        )
    }

    private inline fun addMesh(
        mesh: MGMMeshInstance,
        queue: ConcurrentLinkedQueue<MGDrawerMeshInstanced>
    ) {
        queue.add(
            MGDrawerMeshInstanced(
                mesh.enableCullFace,
                mesh.vertexArray,
                mesh.material
            )
        )
    }

}