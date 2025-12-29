package good.damn.engine.imports

import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.models.MGMMeshDrawer
import java.io.File
import java.io.FileInputStream

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
            Thread {
                MGStreamLevel.readBin(
                    MGFlowLevel {
                        informator.meshesInstanced.add(
                            MGMMeshDrawer(
                                it.shader,
                                MGDrawerMeshInstanced(
                                    it.enableCullFace,
                                    it.vertexArray,
                                    it.material
                                )
                            )
                        )
                    },
                    FileInputStream(
                        file
                    ),
                    informator,
                    misc.buffer
                )

                file.delete()
            }.start()
        }
    }

}