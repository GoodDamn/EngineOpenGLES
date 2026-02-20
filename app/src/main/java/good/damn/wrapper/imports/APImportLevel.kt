package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.engine2.flow.MGFlowLevel
import good.damn.engine2.level.MGIImportMapAdditional
import good.damn.engine2.level.MGImportLevelAdditional
import good.damn.engine2.level.MGLevelSpawnPoints
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

    private val mImportsAdditional: Array<
        MGImportLevelAdditional
    > = arrayOf(
        MGLevelSpawnPoints()
    )

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".map"
    )

    override fun onSetProviderGl() {
        mImportsAdditional.forEach {
            it.glProvider = glProvider
        }
    }

    final override fun onProcessTempFile(
        rootFile: File,
        contextFiles: Array<File?>
    ) {
        Thread(
            APRunnableMap(
                rootFile,
                misc,
                glProvider,
                contextFiles,
                mImportsAdditional
            )
        ).start()
    }

    private class APRunnableMap(
        private val file: File,
        private val misc: APMImportMisc,
        private val provider: MGMProviderGL,
        private val contextFiles: Array<File?>,
        private val additionalImports: Array<MGImportLevelAdditional>
    ): Runnable {

        override fun run() {
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
                provider,
                additionalImports.filter { ai ->
                    contextFiles.find {
                        it ?: return@find false
                        ai.hasValidExtension(
                            it.name
                        )
                    } != null
                }
            )

            file.delete()

        }

    }

}