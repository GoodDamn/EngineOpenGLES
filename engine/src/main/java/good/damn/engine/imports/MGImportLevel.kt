package good.damn.engine.imports

import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInstanceMesh
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
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
                informator.meshesInstanced[
                    it.shader
                ]?.run {
                    addMesh(
                        it,
                        this
                    )
                    return@MGFlowLevel
                }

                informator.meshesInstanced[
                    it.shader
                ] = ConcurrentLinkedQueue<
                    MGDrawerMeshInstanced
                >().apply {
                    addMesh(
                        it,
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
        mesh: MGMInstanceMesh,
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