package good.damn.engine.opengl.drawers.modes

import android.icu.text.ListFormatter.Width
import android.opengl.GLES30
import android.opengl.GLES30.GL_BACK
import android.opengl.GLES30.GL_COLOR_BUFFER_BIT
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES30.GL_DEPTH_TEST
import android.opengl.GLES30.GL_STENCIL_BUFFER_BIT
import android.opengl.GLES30.glClear
import android.opengl.GLES30.glClearColor
import android.opengl.GLES30.glCullFace
import android.opengl.GLES30.glDisable
import android.opengl.GLES30.glEnable
import android.opengl.GLES30.glViewport
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer

data class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerTriggerLight
    )

    override fun draw(
        width: Int,
        height: Int
    ) {
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional

        // Geometry pass
        drawerFramebufferG.bind()
        informator.shaders.sky.run {
            use()
            informator.meshSky.drawSingleTexture(
                this,
                this
            )
        }

        informator.meshesInstanced.forEach {
            it.key.run {
                use()
                it.value.forEach {
                    it.draw(
                        materials
                    )
                }
            }
        }

        drawerFramebufferG.unbind(
            width,
            height
        )

        informator.shaders.lightPassOpaque.run {
            use()
            camera.drawPosition(
                this
            )

            drawerLightDirectional.draw(
                lightDirectional
            )

            informator.drawerLightPass.draw(
                this
            )
            informator.managerLight.draw(
                lightPoints
            )
        }

        /*if (!informator.canDrawTriggers) {
            return
        }

        glEnable(GL_DEPTH_TEST)

        informator.shaders.wireframe.single.run {
            use()
            camera.draw(
                this
            )
            mTriggerManagers.forEach {
                it.draw(
                    this
                )
            }
        }*/


        /*
        val shaderTrigger =
        informator.meshes.forEach {
            it.key.run {
                use()
                camera.draw(
                    this
                )
                camera.drawPosition(
                    this
                )
                drawerLightDirectional.draw(
                    lightDirectional
                )
                it.value.forEach {
                    it.drawNormals(
                        this
                    )
                    it.drawMaterial(
                        material,
                        this
                    )
                }
                managerLight.draw(
                    lightPoints
                )
            }
        }*/

        /**/
    }

}