package good.damn.apigl.drawers

import good.damn.apigl.enums.GLEnumTextureType
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.textures.GLMTexturePart
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive
import java.util.LinkedList

class GLDrawerMaterialTexture(
    private val list: List<GLMTexturePart>
): GLIDrawerTexture<
    Array<GLShaderTexture>
> {
    override fun draw(
        shader: Array<GLShaderTexture>
    ) {
        var i = 0
        list.forEach {
            it.drawer.draw(
                shader[i]
            )
            i++
        }
    }

    override fun unbind(
        shader: Array<GLShaderTexture>
    ) {
        var i = 0
        list.forEach {
            it.drawer.unbind(
                shader[i]
            )
            i++
        }
    }
}