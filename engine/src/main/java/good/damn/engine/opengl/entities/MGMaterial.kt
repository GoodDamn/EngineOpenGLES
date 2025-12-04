package good.damn.engine.opengl.entities

import android.graphics.BitmapFactory
import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenTexture
import good.damn.engine.utils.MGUtilsBitmap
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

class MGMaterial(
    private val materialTexture: MGMaterialTexture
): MGIDrawerTexture<MGShaderMaterial> {
    var shine = 1f

    fun getTextureByType(
        type: MGEnumTextureType
    ) = materialTexture.getTextureByType(
        type
    )

    override fun draw(
        shader: MGShaderMaterial
    ) {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        materialTexture.draw(
            shader.textures
        )
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        materialTexture.unbind(
            shader.textures
        )
    }

}