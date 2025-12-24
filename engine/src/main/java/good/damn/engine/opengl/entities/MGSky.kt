package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.loaders.texture.MGLoaderTexture
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolTextures

class MGSky(
    private val materialTexture: MGMaterialTexture,
    private val verticesSky: MGArrayVertexConfigurator
): MGDrawerMeshMaterialMutable(
    arrayOf(
        MGMaterial(
            materialTexture
        )
    ),
    MGDrawerMeshSwitch(
        MGDrawerVertexArray(
            verticesSky
        ),
        MGDrawerPositionEntity(
            MGMatrixScale().apply {
                setScale(
                    2000000f,
                    2000000f,
                    2000000f
                )
                invalidateScale()
            }
        ),
        GLES30.GL_CW
    )
) {
    fun configure(
        poolTextures: MGPoolTextures
    ) {
        materialTexture.load(
            poolTextures,
            "textures/sky",
            MGLoaderTexture.INSTANCE
        )

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            verticesSky.configure(
                vertices,
                indices,
                MGPointerAttribute.defaultNoTangent
            )
        }
    }

}