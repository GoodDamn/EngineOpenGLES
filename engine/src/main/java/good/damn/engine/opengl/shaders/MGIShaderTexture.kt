package good.damn.engine.opengl.shaders

interface MGIShaderTexture {
    val uniformTexture: Int

    fun setupUniforms(
        program: Int,
        name: String
    )
}