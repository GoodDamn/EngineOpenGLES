package good.damn.engine.opengl.shaders

interface MGIShaderCamera {
    val uniformModelView: Int
    val uniformCameraProjection: Int
    val uniformCameraView: Int
}