package good.damn.apigl.shaders

interface GLIShaderTexture
: GLIShaderTextureUniform {
    fun setupUniforms(
        program: Int
    )
}