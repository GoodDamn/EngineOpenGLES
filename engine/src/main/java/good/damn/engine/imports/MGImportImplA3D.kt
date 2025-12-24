package good.damn.engine.imports

import android.os.Handler
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.runnables.MGICallbackModel
import good.damn.engine.utils.MGUtilsA3D
import good.damn.ia3d.A3DImport
import good.damn.ia3d.stream.A3DInputStream
import java.io.File
import java.io.FileInputStream

class MGImportImplA3D(
    private val handler: Handler,
    private val poolMeshes: MGPoolMeshesStatic,
    private val modelsCallback: MGICallbackModel,
    private val buffer: ByteArray
): MGImportImplTempFile() {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".a3d"
    )

    override fun onProcessTempFile(
        file: File
    ) {
        poolMeshes[
            file.name
        ]?.run {
            modelsCallback.onGetObjectsCached(
                this
            )
            return
        }

        handler.post(
            MGRunnableA3D(
                file,
                buffer,
                modelsCallback,
            )
        )
    }

    private class MGRunnableA3D(
        private val file: File,
        private val buffer: ByteArray,
        private val modelsCallback: MGICallbackModel
    ): Runnable {
        override fun run() {
            val asset = A3DImport.createFromStream(
                A3DInputStream(
                    buffer,
                    FileInputStream(
                        file
                    )
                ),
                buffer
            ) ?: return

            val mesh = asset.meshes[0]
            val configIndices = mesh.subMeshes[0].indices

            modelsCallback.onGetObjects(
                file.name,
                arrayOf(
                    MGObject3d(
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
        }
    }

}