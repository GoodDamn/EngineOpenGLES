package good.damn.apigl.shaders

class GLShaderMaterial(
    val textures: Array<GLShaderTexture>
): good.damn.apigl.drawers.GLIUniform {

    companion object {
        @JvmField
        val empty = arrayOf(
            GLShaderMaterial(
                arrayOf()
            )
        )
    }

    override fun setupUniforms(
        program: Int
    ) {
        textures.forEach {
            it.setupUniforms(
                program
            )
        }
    }
}