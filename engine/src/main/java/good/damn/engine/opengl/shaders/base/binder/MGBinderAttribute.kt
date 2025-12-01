package good.damn.engine.opengl.shaders.base.binder

import android.opengl.GLES30
import java.lang.reflect.Array
import java.util.LinkedList

class MGBinderAttribute private constructor(
    private val locations: List<MGMBinderLocation>
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
            MGMBinderLocation
        >()

        fun bindPosition(): Builder {
            list.add(
                MGMBinderLocation(
                    "position",
                    0
                )
            )
            return this
        }

        fun bindTextureCoordinates(): Builder {
            list.add(
                MGMBinderLocation(
                    "texCoord",
                    1
                )
            )
            return this
        }

        fun bindNormal(): Builder {
            list.add(
                MGMBinderLocation(
                    "normal",
                    2
                )
            )
            return this
        }

        fun bindInstancedModel(): Builder {
            list.add(
                MGMBinderLocation(
                    "modelInstance",
                    3
                )
            )
            return this
        }

        fun bindInstancedRotationMatrix(): Builder {
            list.add(
                MGMBinderLocation(
                    "instanceRotation",
                    7
                )
            )
            return this
        }

        fun bindTangent(): Builder {
            list.add(
                MGMBinderLocation(
                    "aTangent",
                    11
                )
            )
            return this
        }

        fun build() = MGBinderAttribute(
            list
        )
    }

    private data class MGMBinderLocation(
        val name: String,
        val location: Int
    )
}