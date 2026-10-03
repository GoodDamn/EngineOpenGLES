package good.damn.wrapper.renderer

import android.opengl.GLES20.GL_BACK
import android.opengl.GLES20.GL_LESS
import android.opengl.GLES20.GL_NO_ERROR
import android.opengl.GLES20.glCullFace
import android.opengl.GLES20.glDepthFunc
import android.opengl.GLES20.glGetError
import android.opengl.GLES30
import android.util.Log
import good.damn.apigl.GLApi
import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.common.utils.COUtilsFile
import good.damn.engine.ASObject3d

class APRendererNew(
    private val glHandler: COHandlerGl,
    private val glApi: GLApi,
    private val matrix: FloatArray
): COIRunnableBounds {


    override fun run(
        width: Int,
        height: Int
    ) {
        val attrs = intArrayOf(
            0, // position
            1, // texture coords
            2, // normal
        )

        val binderAttribute = glApi.createBinderAttribute(
            attrs
        )

        val program = glApi.createProgram(
            """
                #version 310 es
                uniform mat4 u_MVP;
                layout(location=0) in vec3 position;
                layout(location=1) in vec2 texCoord;
                layout(location=3) in vec3 normal;
                
                void main() {
                    gl_Position = u_MVP * vec4(1.0, position);
                }
                
            """.trimIndent(),
            """
                #version 310 es
                precision mediump float;
                out vec4 FragColor;
                void main() {
                    FragColor = vec4(0.8, 0.8, 0.8, 1.0);
                }
            """.trimIndent(),
            binderAttribute
        )

        val uniformLocation = glApi.getUniformLocation(
            program,
            "u_MVP"
        )

        val vertexAttribute = glApi.createVertexAttribute(
            attrs
        )

        ASObject3d.createFromFile(
            COUtilsFile.getPublicFile(
                "objs/sphere.fbx"
            )
        )?.get(0)?.apply {
            val indices = rawIndices
                ?: return

            val vertices = rawVertices
                ?: return

            val descriptorVertexArray = glApi.createVertexArray(
                vertexAttribute,
                vertices,
                indices
            )

            glHandler.registerCycleTask(
                object: COIRunnableBounds {
                    override fun run(
                        width: Int,
                        height: Int
                    ) {
                        GLES30.glClearColor(
                            0.0f,
                            1.0f,
                            0.0f,
                            1.0f
                        )

                        glApi.useProgram(
                            program
                        )

                        glApi.setModelMatrix(
                            uniformLocation,
                            matrix
                        )

                        glApi.drawMesh(
                            descriptorVertexArray,
                            GLES30.GL_TRIANGLES,
                            GLES30.GL_UNSIGNED_INT,
                            indices.size
                        )
                    }

                }
            )
        }

        catchError(5)


        glDepthFunc(
            GL_LESS
        )

        glCullFace(
            GL_BACK
        )

    }

    private inline fun catchError(
        ind: Int
    ) {
        while (true) {
            val i = glGetError()
            if (i == GL_NO_ERROR) {
                break
            }
            Log.d("APRendererEditor", "run: ERROR$ind: ${i.toString(16)}")
        }
    }
}