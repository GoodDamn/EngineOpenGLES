package good.damn.engine2.opengl.drawmodes

import android.opengl.GLES30
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.glEnable
import good.damn.apigl.GLRenderVars
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.engine2.providers.MGProviderGL

class MGDrawModeOpaque(
    lightPassDrawer: GLDrawerLightPass,
    private val lightPassShader: GLShaderLightPass
): MGDrawModeBase(
    lightPassDrawer,
    GLEnumDrawModeMesh.TRIANGLES
) {

    override fun draw(
        width: Int,
        height: Int
    ) {
        drawIt(
            width,
            height
        )
    }

    private inline fun drawIt(
        width: Int,
        height: Int
    ) = glProvider.apply {
        //val camera = informator.camera

        // Geometry pass
        drawers.drawerFramebuffer.bind()
        geometry.draw()
        if (parameters.canDrawTriggers) {
            shaders.wireframe.apply {
                use()
                drawers.drawerVolumes.draw(
                    this
                )
            }
        }

        drawers.drawerFramebuffer.unbind(
            width,
            height
        )

        lightPassShader.run {
            use()
            /*camera.drawPosition(
                this
            )*/

            drawers.drawerLightDirectional.draw(
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

            drawers.drawerLightDirectional.draw(
                lightDirectional
            )

            GLES30.glUniform2f(
                uniformScreenSize,
                width.toFloat(),
                height.toFloat()
            )

            managers.managerLight.draw(
                lightPoint,
                this,
                textures
            )
        }
    }

}