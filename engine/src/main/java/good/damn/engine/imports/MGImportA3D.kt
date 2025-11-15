package good.damn.engine.imports

import android.opengl.GLES30
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.runnables.MGICallbackModel
import good.damn.engine.utils.MGUtilsA3D
import good.damn.engine.utils.MGUtilsArray
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMAsset

class MGImportA3D(
    private val poolMeshes: MGPoolMeshesStatic,
    private val modelsCallback: MGICallbackModel
) {
    fun import(
        fileName: String,
        asset: A3DMAsset
    ) {
        poolMeshes[
            fileName
        ]?.run {
            modelsCallback.onGetObjectsCached(
                this
            )
            return
        }

        val mesh = asset.meshes[0]
        val configIndices = mesh.subMeshes[0].indices

        modelsCallback.onGetObjects(
            fileName,
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