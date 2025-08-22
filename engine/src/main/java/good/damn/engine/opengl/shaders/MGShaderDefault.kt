package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetAttribLocation
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUseProgram
import kotlinx.coroutines.processNextEventInCurrentThread

class MGShaderDefault
: MGIShader,
MGIShaderCamera,
MGIShaderLight,
MGIShaderTexture {

    override var attribPosition = 0
        private set

    override var attribTextureCoordinates = 0
        private set

    override var attribNormal = 0
        private set




    override var uniformTexture = 0
        private set

    override var uniformModelView = 0
        private set

    override var uniformTextureOffset = 0
        private set



    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    override val light = MGShaderLight()
    override val material = MGShaderMaterial()

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

        light.setupUniforms(
            program
        )

        material.setupUniforms(
            program
        )

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
        uniformTexture = glGetUniformLocation(
            program,
            "texture"
        )

        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        uniformTextureOffset = glGetUniformLocation(
            program,
            "textureOffset"
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