package good.damn.engine.opengl.arrays.pointers

import android.opengl.GLES30.GL_FLOAT
import android.opengl.GLES30.glEnableVertexAttribArray
import android.opengl.GLES30.glVertexAttribPointer
import java.util.LinkedList

class MGPointerAttribute private constructor(
    private val pointers: List<MGMPointerAttribute>,
    private val stride: Int
) {

    companion object {
        @JvmField
        val default32 = Builder()
            .pointPosition()
            .pointTextureCoordinates()
            .pointNormal()
            .pointTangent()
            .build()

        @JvmField
        val defaultNoTangent = Builder()
            .pointPosition()
            .pointTextureCoordinates()
            .pointNormal()
            .build()
    }

    fun bindPointers() {
        pointers.forEach {
            glEnableVertexAttribArray(
                it.attrib
            )

            glVertexAttribPointer(
                it.attrib,
                it.size,
                GL_FLOAT,
                false,
                stride,
                it.offset
            )
        }
    }

    class Builder {
        private val list = LinkedList<
            MGMPointerAttribute
        >()

        private var mOffset = 0
        fun pointPosition2(): Builder {
            list.add(
                MGMPointerAttribute(
                    0,
                    mOffset,
                    2
                )
            )

            mOffset += 2 * 4
            return this
        }

        fun pointPosition(): Builder {
            list.add(
                MGMPointerAttribute(
                    0,
                    mOffset,
                    3
                )
            )
            mOffset += 3 * 4
            return this
        }

        fun pointTextureCoordinates(): Builder {
            list.add(
                MGMPointerAttribute(
                    1,
                    mOffset,
                    2
                )
            )
            mOffset += 2 * 4
            return this
        }

        fun pointNormal(): Builder {
            list.add(
                MGMPointerAttribute(
                    2,
                    mOffset,
                    3
                )
            )
            mOffset += 3 * 4
            return this
        }

        fun pointTangent(): Builder {
            list.add(
                MGMPointerAttribute(
                    11,
                    mOffset,
                    3
                )
            )
            mOffset += 3 * 4
            return this
        }

        fun build() = MGPointerAttribute(
            list,
            mOffset
        )
    }

    private data class MGMPointerAttribute(
        val attrib: Int,
        val offset: Int,
        val size: Int
    )
}