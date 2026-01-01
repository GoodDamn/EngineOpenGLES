package good.damn.engine.opengl.drawers.modes

import android.opengl.GLES30
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.glCullFace
import android.opengl.GLES30.glDisable
import android.opengl.GLES30.glEnable
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPass
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPassPointLight
import good.damn.engine.opengl.textures.MGTexture

class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val lightPassDrawer: MGDrawerLightPass,
    private val lightPassShader: MGShaderLightPass,
    private val lightPassShaderPointLight: MGShaderLightPassPointLight,
    private val lightPassPointLightTextures: Array<MGTexture>,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerLightVolumes
    )

    override fun draw(
        width: Int,
        height: Int
    ) {
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional

        // Geometry pass
        drawerFramebufferG.bind()
        /*informator.meshSky.meshMaterial.run {
            shader.run {
                use()
                drawer.drawMaterials(
                    materials,
                    this
                )
            }
        }*/

        informator.meshes.forEach {
            it.shader.run {
                use()
                it.drawer.drawNormals(
                    this
                )
                it.drawer.drawMaterials(
                    materials,
                    this
                )
            }
        }

        informator.meshesInstanced.forEach {
            it.shader.run {
                use()
                it.drawer.draw(
                    materials
                )
            }
        }

        if (informator.canDrawTriggers) {
            informator.shaders.wireframe.apply {
                use()
                mTriggerManagers.forEach {
                    it.draw(
                        this
                    )
                }
            }
        }

        drawerFramebufferG.unbind(
            width,
            height
        )

        glEnable(
            GLES30.GL_BLEND
        )

        GLES30.glBlendEquation(
            GLES30.GL_FUNC_ADD
        )

        GLES30.glBlendFunc(
            GLES30.GL_ONE,
            GLES30.GL_ONE
        )

        lightPassShader.run {
            use()
            camera.drawPosition(
                this
            )

            drawerLightDirectional.draw(
                lightDirectional
            )

            lightPassDrawer.draw(
                this
            )
        }

        lightPassShaderPointLight.run {
            use()
            camera.drawPosition(
                this
            )

            drawerLightDirectional.draw(
                lightDirectional
            )

            GLES30.glUniform2f(
                uniformScreenSize,
                width.toFloat(),
                height.toFloat()
            )

            informator.managerLight.draw(
                lightPoint,
                this,
                textures,
                lightPassPointLightTextures
            )
        }
    }

}