package good.damn.engine.opengl.scene

import android.opengl.GLES30
import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBitmap
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MGSceneSky(
    switcherDrawMode: MGSwitcherDrawMode
): MGScene(
    switcherDrawMode
) {

    private val modelMatrixSky = MGMatrixScale().apply {
        setScale(
            2000000f,
            2000000f,
            2000000f
        )
        invalidateScale()
    }

    private val mVerticesSky = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.SHORT
    )

    private val mTextureSky = MGTexture(
        MGEnumTextureType.DIFFUSE
    )

    private val meshSky = MGDrawerMeshSwitch(
        MGDrawerVertexArray(
            mVerticesSky
        ),
        MGDrawerPositionEntity(
            modelMatrixSky
        ),
        GLES30.GL_CCW
    )

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        super.onSurfaceCreated(
            gl,
            config
        )

        MGUtilsBitmap.loadBitmap(
            "textures/sky/sky.png"
        )?.run {
            mTextureSky.glTextureSetup(
                this,
                GL_CLAMP_TO_EDGE
            )
        }

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            mVerticesSky.configure(
                vertices,
                indices,
                MGPointerAttribute.default32
            )
        }
    }
}