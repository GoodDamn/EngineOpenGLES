package good.damn.engine.opengl.shaders

interface MGIShader {

    fun use()

    fun setupUniforms(
        program: Int
    )
}