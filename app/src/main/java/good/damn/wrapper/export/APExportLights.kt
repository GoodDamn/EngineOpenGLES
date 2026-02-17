package good.damn.wrapper.export

import android.util.Log
import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine2.providers.MGProviderGL
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

class APExportLights
: MGProviderGL(),
APIExport {

    companion object {
        private const val TAG = "APExportLights"
    }

    override fun export(
        file: File
    ) {
        val outFile = File(
            "${file.path}.slights"
        ).apply {
            if (!exists() && createNewFile()) {
                Log.d(TAG, "export: exported $name is created")
            }
        }

        val outStream = DataOutputStream(
            FileOutputStream(
                outFile
            )
        )

        glProvider.managers.managerLight.lights.apply {
            val mapLight = SparseArrayCompat<SDMLightPoint>(
                size / 2
            )

            outStream.writeShort(
                size
            )

            forEach {
                val keyHash = it.light.hashCode()
                if (!mapLight.containsKey(
                    keyHash
                )) {
                    mapLight.put(
                        keyHash,
                        it.light
                    )
                }

                outStream.writeShort(
                    mapLight.indexOfKey(keyHash)
                )

                writePosition(
                    outStream,
                    it.modelMatrix
                )
            }

            outStream.writeShort(
                mapLight.size()
            )

            for (i in 0 until mapLight.size()) {
                mapLight.get(
                    mapLight.keyAt(i)
                )?.let {
                    writeLight(
                        outStream,
                        it
                    )
                }
            }
        }

        outStream.close()
    }

    private inline fun writeLight(
        outStream: DataOutputStream,
        light: SDMLightPoint
    ) {
        outStream.apply {
            writeByte(
                (light.alpha * 255).toInt()
            )

            writeFloat(
                light.radius
            )

            writeFloat(
                light.interpolation.constant
            )

            writeFloat(
                light.interpolation.linear
            )

            writeByte(
                (light.color.x * 255).toInt()
            )

            writeByte(
                (light.color.y * 255).toInt()
            )

            writeByte(
                (light.color.z * 255).toInt()
            )
        }
    }

    private inline fun writePosition(
        outStream: DataOutputStream,
        modelMatrix: COMatrixTranslate
    ) {
        modelMatrix.apply {
            outStream.writeFloat(x)
            outStream.writeFloat(y)
            outStream.writeFloat(z)
        }
    }
}