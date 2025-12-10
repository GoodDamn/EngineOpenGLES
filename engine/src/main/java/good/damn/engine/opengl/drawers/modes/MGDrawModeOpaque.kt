package good.damn.engine.opengl.drawers.modes

import android.icu.text.ListFormatter.Width
import android.opengl.GLES30
import android.opengl.GLES30.GL_BACK
import android.opengl.GLES30.GL_COLOR_BUFFER_BIT
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES30.GL_DEPTH_TEST
import android.opengl.GLES30.glClear
import android.opengl.GLES30.glClearColor
import android.opengl.GLES30.glCullFace
import android.opengl.GLES30.glDisable
import android.opengl.GLES30.glEnable
import android.opengl.GLES30.glViewport
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer

data class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val framebufferG: MGFramebuffer
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerTriggerLight
    )

    override fun draw(
        width: Int,
        height: Int
    ) {
        glViewport(
            0, 0,
            width, height
        )
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional

        // Geometry pass
        framebufferG.bind()
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)

        informator.shaders.sky.run {
            use()
            camera.draw(
                this
            )
            drawerLightDirectional.drawColor(
                uniformColor
            )

            informator.meshSky.drawSingleTexture(
                this,
                this
            )
        }

        informator.meshesInstanced.forEach {
            it.key.run {
                use()
                camera.draw(
                    this
                )
                it.value.forEach {
                    it.draw(
                        materials
                    )
                }
            }
        }
        framebufferG.unbind()
        // Light (final) pass
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glDisable(GL_CULL_FACE)
        glDisable(GL_DEPTH_TEST)

        informator.shaders.lightPass.run {
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

        if (!informator.canDrawTriggers) {
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
        }


        /*val
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