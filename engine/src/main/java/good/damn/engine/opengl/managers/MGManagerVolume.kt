package good.damn.engine.opengl.managers

import android.opengl.GLES30
import android.opengl.Matrix
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.common.camera.COCameraProjection
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.common.volume.COIVolume
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.sdk.process.SDIProcessTime

class MGManagerVolume(
    private val camera: COCameraProjection
): MGIDrawerShader<MGIShaderModel>,
SDIProcessTime {



    override fun draw(
        shader: MGIShaderModel
    ) {
        mVolumes.forEach {
            it.drawerModel.draw(
                shader
            )

            mDrawerPrimitive.draw(
                GLES30.GL_LINES
            )
        }
    }
}