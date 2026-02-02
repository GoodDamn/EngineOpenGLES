package good.damn.apigl.shaders

interface GLIShader {

    fun use()

    fun setupUniforms(
        program: Int
    )
}