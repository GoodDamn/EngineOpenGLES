package good.damn.engine.opengl.shaders

interface MGIShaderTexture
: MGIShaderTextureUniform {
    fun setupUniforms(
        program: Int
    )
}