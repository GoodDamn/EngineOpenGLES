package good.damn.wrapper.imports

import good.damn.engine.ASObject3d
import good.damn.engine2.utils.MGUtilsA3D
import good.damn.ia3d.A3DImport
import good.damn.ia3d.stream.A3DInputStream
import java.io.File
import java.io.FileInputStream

class MGImportImplA3D(
    private val misc: MGMImportMisc
): MGImportImplTempFile() {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".a3d"
    )

    override fun onProcessTempFile(
        file: File
    ) {
        misc.handler.post(
            MGRunnableA3D(
                file,
                misc
            )
        )
    }

    private class MGRunnableA3D(
        private val file: File,
        private val misc: MGMImportMisc
    ): Runnable {
        override fun run() {
            val asset = A3DImport.createFromStream(
                A3DInputStream(
                    misc.buffer,
                    FileInputStream(
                        file
                    )
                ),
                misc.buffer
            ) ?: return

            val mesh = asset.meshes[0]
            val configIndices = mesh.subMeshes[0].indices

            misc.modelsCallback.processObjects(
                file.name,
                arrayOf(
                    ASObject3d(
                        MGUtilsA3D.createMergedVertexBuffer(
                            mesh,
                            1.0f
                        ),
                        configIndices.buffer,
                        MGUtilsA3D.createConfigurationArrayVertex(
                            configIndices
                        ),
                        null,
                        null,
                        null
                    )
                )
            )

            file.delete()
        }
    }

}