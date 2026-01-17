package good.damn.apigl.shaders.base

import android.opengl.GLES30
import java.util.LinkedList

class GLBinderAttribute private constructor(
    private val locations: List<GLMBinderLocation>
) {

    fun bindAttributes(
        program: Int
    ) {
        locations.forEach {
            GLES30.glBindAttribLocation(
                program,
                it.location,
                it.name
            )
        }
    }

    class Builder {

        private val list = LinkedList<
            GLMBinderLocation
        >()

        fun bindPosition(): Builder {
            list.add(
                GLMBinderLocation(
                    "position",
                    0
                )
            )
            return this
        }

        fun bindTextureCoordinates(): Builder {
            list.add(
                GLMBinderLocation(
                    "texCoord",
                    1
                )
            )
            return this
        }

        fun bindNormal(): Builder {
            list.add(
                GLMBinderLocation(
                    "normal",
                    2
                )
            )
            return this
        }

        fun bindInstancedModel(): Builder {
            list.add(
                GLMBinderLocation(
                    "modelInstance",
                    3
                )
            )
            return this
        }

        fun bindInstancedRotationMatrix(): Builder {
            list.add(
                GLMBinderLocation(
                    "instanceRotation",
                    7
                )
            )
            return this
        }

        fun bindTangent(): Builder {
            list.add(
                GLMBinderLocation(
                    "aTangent",
                    11
                )
            )
            return this
        }

        fun build() = GLBinderAttribute(
            list
        )
    }

    private data class GLMBinderLocation(
        val name: String,
        val location: Int
    )
}