package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.common.COHandlerGl
import good.damn.engine2.flow.MGFlowLevel
import good.damn.engine2.level.MGStreamLevel
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.opengl.MGMGeometry
import good.damn.engine2.opengl.models.MGMMeshDrawer
import good.damn.engine2.opengl.pools.MGMPools
import java.io.File
import java.io.FileInputStream

class APImportImplLevel(
    private val misc: APMImportMisc,
    private val geometry: MGMGeometry,
    private val pools: MGMPools,
    private val shaders: MGMInformatorShader,
    private val glHandler: COHandlerGl,
    private val managers: MGMManagers,
): APImportImplTempFile() {

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
                geometry,
                pools,
                shaders,
                glHandler,
                managers,
                misc
            )
        )
    }

    private class MGRunnableMap(
        private val file: File,
        private val geometry: MGMGeometry,
        private val pools: MGMPools,
        private val shaders: MGMInformatorShader,
        private val glHandler: COHandlerGl,
        private val managers: MGMManagers,
        private val misc: APMImportMisc
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
                    misc.buffer,
                    pools,
                    shaders,
                    glHandler,
                    geometry,
                    managers
                )

                file.delete()
            }.start()
        }
    }

}