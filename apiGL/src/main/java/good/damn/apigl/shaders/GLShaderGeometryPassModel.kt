package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformLocation

class GLShaderGeometryPassModel(
    materials: Array<GLShaderMaterial>
): GLShaderGeometryPassInstanced(
    materials
), GLIShaderModel {

    override var uniformModelView = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )
    }
}