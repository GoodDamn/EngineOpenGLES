package good.damn.engine.imports

import android.os.Handler
import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInstanceMesh
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportImplLevel(
    private val handler: Handler,
    private val buffer: ByteArray,
    private val informator: MGMInformator
): MGImportImplTempFile() {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".map"
    )

    final override fun onProcessTempFile(
        file: File
    ) {
        handler.post(
            MGRunnableMap(
                buffer,
                file,
                informator
            )
        )
    }

    private class MGRunnableMap(
        private val buffer: ByteArray,
        private val file: File,
        private val informator: MGMInformator
    ): Runnable {
        override fun run() {
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
                    file
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

}