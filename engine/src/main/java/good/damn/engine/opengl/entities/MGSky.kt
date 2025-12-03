package good.damn.engine.opengl.entities

import android.opengl.GLES30
import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBitmap

class MGSky(
    private val textureDiffuse: MGTexture,
    private val verticesSky: MGArrayVertexConfigurator
): MGDrawerMeshTextureSwitch(
    textureDiffuse,
    null,
    null,
    null,
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
    fun configure() {
        MGUtilsBitmap.loadBitmap(
            "textures/sky/sky.png"
        )?.run {
            textureDiffuse.glTextureSetup(
                this,
                GL_CLAMP_TO_EDGE
            )
        }

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