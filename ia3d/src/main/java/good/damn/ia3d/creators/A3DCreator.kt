package good.damn.ia3d.creators

import good.damn.ia3d.models.A3DMAsset
import good.damn.ia3d.stream.A3DInputStream
import java.io.DataInputStream

object A3DCreator {

    private val SIGNATURE = 1

    fun readRootBlock(
        stream: A3DInputStream,
        buffer: ByteArray
    ): A3DMAsset? {
        val sig = stream.readLInt()
        stream.readLInt()

        if (sig != SIGNATURE) {
            return null
        }

        val materials = A3DCreatorMaterial.createFromStream(
            stream,
            buffer
        ) ?: return null

        val meshes = A3DCreatorMesh.createFromStream(
            stream
        ) ?: return null

        val transforms = A3DCreatorTransform.createFromStream(
            stream
        ) ?: return null

        val objects = A3DCreatorObject.createFromStream(
            stream,
            buffer
        ) ?: return null

        return A3DMAsset(
            materials,
            meshes,
            transforms,
            objects
        )
    }
}