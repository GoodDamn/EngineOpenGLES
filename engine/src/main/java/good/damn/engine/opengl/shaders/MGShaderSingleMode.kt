package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetAttribLocation
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUseProgram

class MGShaderSingleMode
: MGIShader,
MGIShaderCamera {

    override var attribPosition = 0
        private set

    override var attribTextureCoordinates = 0
        private set

    override var attribNormal = 0
        private set

    override var uniformModelView = 0
        private set

    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    private var mProgram = 0

    override fun use() {
        glUseProgram(
            mProgram
        )
    }

    override fun setupUniforms(
        program: Int
    ) {
        mProgram = program

        attribPosition = glGetAttribLocation(
            program,
            "position"
        )

        attribTextureCoordinates = glGetAttribLocation(
            program,
            "texCoord"
        )

        attribNormal = glGetAttribLocation(
            program,
            "normal"
        )


        // Uniforms

        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        uniformCameraProjection = glGetUniformLocation(
            program,
            "projection"
        )

        uniformCameraView = glGetUniformLocation(
            program,
            "view"
        )
    }
}