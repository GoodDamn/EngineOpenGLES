package good.damn.engine.imports

import android.os.Handler
import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInstanceMesh
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentLinkedQueue

class MGImportImplLevel(
    private val misc: MGMImportMisc,
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
        misc.handler.post(
            MGRunnableMap(
                file,
                informator,
                misc
            )
        )
    }

    private class MGRunnableMap(
        private val file: File,
        private val informator: MGMInformator,
        private val misc: MGMImportMisc
    ): Runnable {
        override fun run() {
            CoroutineScope(
                Dispatchers.IO
            ).launch {
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
                    misc.buffer
                )

                file.delete()
            }
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