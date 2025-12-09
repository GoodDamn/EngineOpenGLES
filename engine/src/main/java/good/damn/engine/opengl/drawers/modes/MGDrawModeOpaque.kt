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
        val managerLight = informator.managerLight

        // Geometry pass
        framebufferG.bind()
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glEnable(
            GL_DEPTH_TEST
        )
        glEnable(
            GL_CULL_FACE
        )
        glCullFace(
            GL_BACK
        )
        glClearColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )
        informator.meshesInstanced.forEach {
            informator.shaders.geometryPass.run {
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
        glDisable(
            GL_DEPTH_TEST
        )
        glDisable(
            GL_CULL_FACE
        )
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
            /*managerLight.draw(
                    lightPoints
                )*/
        }

        /*val shaderSky = informator.shaders.sky
        val shaderTrigger = informator.shaders.wireframe.single

        shaderSky.use()
        camera.draw(
            shaderSky
        )
        drawerLightDirectional.drawColor(
            shaderSky.uniformColor
        )

        informator.meshSky.drawSingleTexture(
            shaderSky,
            shaderSky
        )

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

        /*if (!informator.canDrawTriggers) {
            return
        }

        shaderTrigger.use()
        camera.draw(
            shaderTrigger
        )
        mTriggerManagers.forEach {
            it.draw(
                shaderTrigger
            )
        }*/
    }

}