package good.damn.engine.utils

object MGUtilsArray {

    fun createMergedVertexArray(
        position: FloatArray,
        uv: FloatArray,
        normal: FloatArray
    ): FloatArray {
        val vertexCount = position.size / 3
        val output = FloatArray(
            8 * vertexCount
        )

        var iUv = 0
        var iNormal = 0
        var iPosition = 0

        var ii = 0
        while (ii < output.size) {
            output[ii++] = position[iPosition++]
            output[ii++] = position[iPosition++]
            output[ii++] = position[iPosition++]

            output[ii++] = uv[iUv++]
            output[ii++] = uv[iUv++]

            output[ii++] = normal[iNormal++]
            output[ii++] = normal[iNormal++]
            output[ii++] = normal[iNormal++]
        }

        return output
    }
}