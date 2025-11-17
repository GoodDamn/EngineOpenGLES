package good.damn.ia3d.creators

import good.damn.ia3d.misc.A3DMVector3
import good.damn.ia3d.misc.A3DMVector4
import good.damn.ia3d.models.A3DMCreatorTransform
import good.damn.ia3d.models.A3DMTransform
import good.damn.ia3d.stream.A3DInputStream

object A3DCreatorTransform {

    private val SIGNATURE = 3

    fun createFromStream(
        stream: A3DInputStream
    ): A3DMCreatorTransform? {
        val sig = stream.readLInt()
        stream.readLInt() // void

        if (sig != SIGNATURE) {
            return null
        }

        val transformCount = stream.readLInt()

        val transforms = Array(
            transformCount
        ) {
            A3DMTransform(
                A3DMVector3.createFromStream(
                    stream
                ),
                A3DMVector4.createFromStream(
                    stream
                ),
                A3DMVector3.createFromStream(
                    stream
                ),
            )
        }

        val parentIds = IntArray(
            transformCount
        )

        for (i in 0 until transformCount) {
            parentIds[i] = stream.readLInt()
        }

        return A3DMCreatorTransform(
            transforms,
            parentIds
        )
    }
}