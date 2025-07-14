package good.damn.engine.opengl

import android.opengl.GLES30.*
import good.damn.engine.opengl.camera.BaseCamera
import good.damn.engine.opengl.entities.Material
import good.damn.engine.opengl.entities.Mesh
import good.damn.engine.opengl.textures.Texture
import good.damn.engine.utils.BufferUtils

open class StaticMesh(
    obj: Object3D,
    texturePath: String,
    program: Int,
    camera: BaseCamera
): Mesh(
    program,
    camera
) {

    var material = Material(
        program
    )

    private val mTexture = Texture(
        texturePath,
        program
    )

    private val mIndicesCount = obj.indices.size
    private val mVertexArray = intArrayOf(
        1
    )

    init {
        glGenVertexArrays(
            1,
            mVertexArray,
            0
        )

        glBindVertexArray(
            mVertexArray[0]
        )

        // Generate vertex buffer

        val idVertex = intArrayOf(
            1
        )

        glGenBuffers(
            1,
            idVertex,
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            idVertex[0]
        )

        glBufferData(
            GL_ARRAY_BUFFER,
            obj.vertices.size * 4,
            BufferUtils.createFloat(
                obj.vertices
            ),
            GL_STATIC_DRAW
        )

        mAttrTexCoord = glGetAttribLocation(
            program,
            "texCoord"
        )

        mAttrNormal = glGetAttribLocation(
            program,
            "normal"
        )

        mAttrPosition = glGetAttribLocation(
            program,
            "position"
        )

        // Generate index buffer

        val idIndex = intArrayOf(
            1
        )

        glGenBuffers(
            1,
            idIndex,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            idIndex[0]
        )

        glBufferData(
            GL_ELEMENT_ARRAY_BUFFER,
            obj.indices.size * 2,
            BufferUtils.createShort(
                obj.indices
            ),
            GL_STATIC_DRAW
        )

        // Enable vertex attrib

        enableVertex(
            mAttrPosition,
            0,
            3
        )

        enableVertex(
            mAttrTexCoord,
            3 * 4,
            2
        )

        enableVertex(
            mAttrNormal,
            5 * 4,
            3
        )

        glBindVertexArray(
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            0
        )
    }

    override fun draw() {
        super.draw()

        mTexture.draw()
        material.draw()

        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesCount,
            GL_UNSIGNED_SHORT,
            0
        )

        glBindVertexArray(
            0
        )
    }

    private fun enableVertex(
        attrib: Int,
        offset: Int,
        size: Int
    ) {

        glEnableVertexAttribArray(
            attrib
        )

        glVertexAttribPointer(
            attrib,
            size,
            GL_FLOAT,
            false,
            mStride,
            offset
        )

    }

}