package good.damn.engine.opengl.generators

import android.util.Log
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.iterators.vertex.MGIVertexIterator
import good.damn.engine.utils.MGUtilsBuffer

class MGGeneratorLandscape(
    val vertexArray: MGArrayVertexManager
) {

    val actualWidth: Int
        get() = mWidth * INTERVAL_VERTEX

    val actualHeight: Int
        get() = mHeight * INTERVAL_VERTEX

    private var mWidth = 1
    private var mHeight = 1

    companion object {
        private const val TAG = "MGGeneratorLandscape"
        private const val INTERVAL_VERTEX = 50
    }

    fun setResolution(
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height

        val dgx = 1.0f / mWidth
        val dgy = 1.0f / mHeight

        val widthInterval = mWidth * INTERVAL_VERTEX
        val heightInterval = mHeight * INTERVAL_VERTEX

        var textureX: Float
        var textureY = 0f

        val gridLen = (width+1) * (height+1)

        val bufferVertex = MGUtilsBuffer.allocateFloat(
            gridLen * 8
        ) // position(3), texCoord(2), normal(3)

        val bufferIndices = MGUtilsBuffer.allocateInt(
            gridLen * 6
        )

        var time = System.currentTimeMillis()
        for (z in 0..heightInterval step INTERVAL_VERTEX) {
            textureX = 0f
            val fz = z.toFloat()
            for (x in 0..widthInterval step INTERVAL_VERTEX) {
                val fx = x.toFloat()

                // Position
                bufferVertex.put(fz)
                bufferVertex.put(0.0f)
                bufferVertex.put(fx)

                // TexCoords
                bufferVertex.put(textureX)
                bufferVertex.put(textureY)

                // Normals
                bufferVertex.put(0.0f)
                bufferVertex.put(1.0f)
                bufferVertex.put(0.0f)

                textureX += dgx
            }

            textureY += dgy
        }
        Log.d(TAG, "setResolution: BUFFER_VERTEX: ${System.currentTimeMillis() - time}")
        time = System.currentTimeMillis()

        var leftTop: Int
        var leftBottom: Int
        var rightTop: Int
        var rightBottom: Int

        val ww = mWidth + 1

        for (y in 0 until mHeight) {
            for (x in 0 until mWidth) {
                leftTop = x + y * ww
                leftBottom = leftTop + ww

                rightTop = leftTop + 1
                rightBottom = leftBottom + 1

                bufferIndices.put(leftTop)
                bufferIndices.put(rightTop)
                bufferIndices.put(rightBottom)
                bufferIndices.put(rightBottom)
                bufferIndices.put(leftBottom)
                bufferIndices.put(leftTop)
            }
        }

        Log.d(TAG, "setResolution: BUFFER_INDICES: ${System.currentTimeMillis() - time}")

        bufferVertex.position(0)
        bufferIndices.position(0)

        time = System.currentTimeMillis()
        vertexArray.configure(
            bufferVertex,
            bufferIndices,
            MGPointerAttribute.defaultNoTangent
        )
        Log.d(TAG, "setResolution: CONFIGURE: ${System.currentTimeMillis() - time}")
    }

    fun forEachVertex(
        vertexIterator: MGIVertexIterator
    ) {
        var index = 0

        var time = System.currentTimeMillis()
        vertexArray.bindVertexBuffer()
        val c = vertexArray.sizeVertexArray

        while (index < c) {
            val x = (vertexArray[index] / INTERVAL_VERTEX).toInt()
            val z = (vertexArray[index + 2] / INTERVAL_VERTEX).toInt()

            vertexIterator.onEachVertex(
                index,
                x,
                z,
                vertexArray
            )

            index += 8
        }

        Log.d(TAG, "displace: changeVertexData: ${System.currentTimeMillis() - time}")
        time = System.currentTimeMillis()
        vertexArray.sendVertexBufferData()
        Log.d(TAG, "displace: changeVertexData: send to GPU ${System.currentTimeMillis() - time}")

        vertexArray.unbindVertexBuffer()
    }
}