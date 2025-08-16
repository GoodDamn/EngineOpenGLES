package good.damn.engine.opengl.shaders

interface MGIShader {

    val attribPosition: Int
    val attribTextureCoordinates: Int
    val attribNormal: Int

    val uniformModelView: Int
    val uniformTexture: Int
    val uniformTextureOffset: Int

    val uniformCameraProjection: Int
    val uniformCameraView: Int

    val light: MGShaderLight
    val material: MGShaderMaterial

    fun setupUniforms(
        program: Int
    )
}