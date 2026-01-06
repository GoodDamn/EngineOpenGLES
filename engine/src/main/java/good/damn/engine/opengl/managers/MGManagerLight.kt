package good.damn.engine.opengl.managers

import android.opengl.GLES30
import android.util.Log
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGShaderLightPoint
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import java.util.concurrent.ConcurrentLinkedQueue

class MGManagerLight(
    private val drawerVolume: MGDrawerVertexArray
) {
    private val mLights = ConcurrentLinkedQueue<
        MGDrawerLightPoint
    >()

    fun draw(
        shader: MGShaderLightPoint,
        shaderModel: MGIShaderModel,
        shaderTextures: Array<MGShaderTexture>,
        textures: Array<MGTexture>
    ) {
        mLights.forEach {
            it.draw(
                shader,
                shaderModel
            )
            var i = 0
            textures.forEach {
                it.draw(
                    shaderTextures[i]
                )
                i++
            }

            drawerVolume.draw(
                GLES30.GL_TRIANGLES
            )

            i = 0
            textures.forEach {
                it.unbind(
                    shaderTextures[i]
                )
                i++
            }
        }
    }

    fun register(
        drawer: MGDrawerLightPoint
    ) {
        mLights.add(
            drawer
        )
    }
}
