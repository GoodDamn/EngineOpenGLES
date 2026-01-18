package good.damn.engine2.opengl

import android.opengl.GLES30
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.glEnable
import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLIDrawer
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.engine2.models.MGMInformatorShader

class MGDrawModeOpaque(
    private val shaders: MGMInformatorShader,
    private val lightPassDrawer: GLDrawerLightPass,
    private val lightPassDrawerLights: GLDrawerLights,
    private val lightPassShader: GLShaderLightPass,
    private val drawerGeometry: MGMGeometry,
    private val drawerFramebufferG: GLDrawerFramebufferG,
    private val drawerLightDirectional: GLDrawerLightDirectional,
    private val volume: MGMVolume
): GLIDrawer {

    override fun draw(
        width: Int,
        height: Int
    ) {
        //val camera = informator.camera

        // Geometry pass
        drawerFramebufferG.bind()
        drawerGeometry.draw()
        shaders.wireframe.apply {
            use()
            volume.draw(
                this
            )
        }

        drawerFramebufferG.unbind(
            width,
            height
        )

        lightPassShader.run {
            use()
            /*camera.drawPosition(
                this
            )*/

            drawerLightDirectional.draw(
                lightDirectional
            )

            lightPassDrawer.draw(
                textures
            )
        }

        glEnable(
            GL_CULL_FACE
        )

        shaders.lightPassPointLight.apply {
            use()
            /*camera.drawPosition(
                this
            )*/

            drawerLightDirectional.draw(
                lightDirectional
            )

            GLES30.glUniform2f(
                uniformScreenSize,
                width.toFloat(),
                height.toFloat()
            )

            lightPassDrawerLights.draw(
                lightPoint,
                textures
            )
        }
    }

}