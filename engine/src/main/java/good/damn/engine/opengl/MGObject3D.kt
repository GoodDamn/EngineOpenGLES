package good.damn.engine.opengl

import good.damn.engine.MGEngine
import good.damn.engine.utils.MGUtilsBuffer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.Vector

class MGObject3D(
    val vertices: FloatBuffer,
    val indices: IntBuffer
) {
    companion object {

        fun createFromAssets(
            path: String
        ): MGObject3D {
            return createFromStream(
                MGEngine.ASSETS.open(
                    path
                )
            )
        }

        fun createFromStream(
            inp: InputStream
        ): MGObject3D {
            val vertices: Vector<Float> = Vector()
            val normals: Vector<Float> = Vector()
            val textures: Vector<Float> = Vector()
            val faces: Vector<String> = Vector()

            var reader: BufferedReader? = null

            try {
                val inStream = InputStreamReader(
                    inp
                )
                reader = BufferedReader(inStream)

                // read file until EOF
                var line = reader.readLine()
                while (line != null) {
                    val parts = line.split("\\s+".toRegex())
                    when (parts[0]) {
                        "v" -> {
                            // vertices
                            vertices.add(parts[1].toFloat())
                            vertices.add(parts[2].toFloat())
                            vertices.add(parts[3].toFloat())
                        }
                        "vt" -> {
                            // textures
                            textures.add(parts[1].toFloat())
                            textures.add(parts[2].toFloat())
                        }
                        "vn" -> {
                            // normals
                            normals.add(parts[1].toFloat())
                            normals.add(parts[2].toFloat())
                            normals.add(parts[3].toFloat())
                        }
                        "f" -> {
                            // faces: vertex/texture/normal
                            faces.add(parts[1])
                            faces.add(parts[2])
                            faces.add(parts[3])
                        }
                    }
                    line = reader.readLine()
                }
            } catch (e: Exception) {
                // cannot load or read file
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: Exception) {
                        //log the exception
                    }
                }
            }

            // 3 - position
            // 2 - texture coords
            // 3 - normals
            val bufferVertices = MGUtilsBuffer.allocateFloat(
                faces.size * 8
            )
            val bufferIndices = MGUtilsBuffer.allocateInt(
                faces.size
            )

            var posIndex = 0

            for ((index, face) in faces.withIndex()) {
                val parts = face.split("/")
                val vertexIndex = (parts[0].toShort() - 1).toShort()
                bufferIndices.put(
                    index,
                    index
                )

                var i = 3 * vertexIndex

                bufferVertices.put(
                    posIndex++,
                    vertices[i++]
                )

                bufferVertices.put(
                    posIndex++,
                    vertices[i++]
                )

                bufferVertices.put(
                    posIndex++,
                    vertices[i]
                )

                i = 2 * (parts[1].toInt() - 1)
                bufferVertices.put(
                    posIndex++,
                    textures[i++]
                )
                bufferVertices.put(
                    posIndex++,
                    1f - textures[i]
                )

                i = 3 * (parts[2].toInt() - 1)
                bufferVertices.put(
                    posIndex++,
                    normals[i++]
                )
                bufferVertices.put(
                    posIndex++,
                    normals[i++]
                )
                bufferVertices.put(
                    posIndex++,
                    normals[i]
                )
            }

            bufferVertices.position(0)
            bufferIndices.position(0)

            return MGObject3D(
                bufferVertices,
                bufferIndices
            )
        }

    }
}