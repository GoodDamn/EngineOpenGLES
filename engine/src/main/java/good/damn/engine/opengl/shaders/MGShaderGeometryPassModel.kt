package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation

class MGShaderGeometryPassModel(
    materials: Array<MGShaderMaterial>
): MGShaderGeometryPassInstanced(
    materials
), MGIShaderModel {

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