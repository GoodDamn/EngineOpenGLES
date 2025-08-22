package good.damn.engine.opengl.shaders

interface MGIShaderCamera {
    val attribPosition: Int
    val attribTextureCoordinates: Int
    val attribNormal: Int

    val uniformModelView: Int
    val uniformCameraProjection: Int
    val uniformCameraView: Int
}