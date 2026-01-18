package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.level.MGStreamLevel
import good.damn.engine.opengl.MGMGeometry
import good.damn.engine.opengl.models.MGMMeshDrawer
import java.io.File
import java.io.FileInputStream

class MGImportImplLevel(
    private val misc: MGMImportMisc,
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
        private val geometry: MGMGeometry,
        private val misc: MGMImportMisc
    ): Runnable {
        override fun run() {
            Thread {
                MGStreamLevel.readBin(
                    MGFlowLevel {
                        geometry.meshesInstanced.add(
                            MGMMeshDrawer(
                                it.shader,
                                GLDrawerMeshInstanced(
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