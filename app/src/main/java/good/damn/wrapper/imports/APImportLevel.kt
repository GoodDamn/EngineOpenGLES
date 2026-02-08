package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.engine2.flow.MGFlowLevel
import good.damn.engine2.level.MGStreamLevel
import good.damn.engine2.opengl.models.MGMMeshDrawer
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.providers.MGProviderGL
import java.io.File
import java.io.FileInputStream

class APImportLevel(
    private val misc: APMImportMisc
): MGProviderGL(),
APIProcessTempFile {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".map"
    )

    final override fun onProcessTempFile(
        file: File
    ) {
        misc.handler.post(
            APRunnableMap(
                file,
                misc,
                glProvider
            )
        )
    }

    private class APRunnableMap(
        private val file: File,
        private val misc: APMImportMisc,
        private val provider: MGMProviderGL
    ): Runnable {

        override fun run() {
            Thread {
                MGStreamLevel.readBin(
                    MGFlowLevel {
                        provider.geometry.meshesInstanced.add(
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
                    misc.buffer,
                    provider.pools,
                    provider.shaders,
                    provider.glHandler,
                    provider.geometry,
                    provider.managers
                )

                file.delete()
            }.start()
        }

    }

}