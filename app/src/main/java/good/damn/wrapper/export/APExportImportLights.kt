package good.damn.wrapper.export

import android.util.Log
import androidx.collection.SparseArrayCompat
import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.sdk.SDVector3
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.sdk.models.SDMLightPointInterpolation
import good.damn.engine2.providers.MGProviderGL
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import good.damn.wrapper.imports.APIImport
import good.damn.wrapper.models.APMUserContent
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

class APExportImportLights
: MGProviderGL(),
APIExport,
APIImport {

    companion object {
        private const val TAG = "APExportLights"
        private const val EXTENSION = ".slights"
    }

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        EXTENSION
    )

    override fun processUserContent(
        userContent: APMUserContent,
        contextUserContents: Array<APMUserContent?>,
        offsetContextUserContents: Int
    ) {
        val inp = DataInputStream(
            userContent.stream
        )

        val countInterpolations = inp.readUnsignedShort()
        val countLights = inp.readUnsignedShort()
        val countPositions = inp.readUnsignedShort()

        val arrInterpolations = Array(
            countInterpolations
        ) {
            SDMLightPointInterpolation(
                inp.readFloat(),
                inp.readFloat(),
                0f,
                inp.readFloat()
            )
        }

        val arrLights = Array(
            countLights
        ) {
            SDMLightPoint(
                SDVector3(
                    inp.readUnsignedByte() / 255f,
                    inp.readUnsignedByte() / 255f,
                    inp.readUnsignedByte() / 255f
                ),
                arrInterpolations[
                    inp.readUnsignedShort()
                ],
                inp.readUnsignedByte() / 255f
            )
        }

        (0 until countPositions).forEach { _ ->
            val triggerLight = LGTriggerStateableLight.createFromLight(
                arrLights[
                    inp.readUnsignedShort()
                ]
            )

            triggerLight.modelMatrix.apply {
                setPosition(
                    inp.readFloat(),
                    inp.readFloat(),
                    inp.readFloat()
                )
                radius = triggerLight.light.interpolation.radius
                invalidatePosition()
                invalidateRadius()
                calculateInvertTrigger()
            }

            val drawer = GLDrawerLightPoint(
                triggerLight.modelMatrix.matrixTrigger.model,
                triggerLight.light
            )

            glProvider.managers.apply {
                managerFrustrum.volumes.add(
                    GLVolumeLight(
                        drawer,
                        triggerLight.modelMatrix.matrixTrigger.model
                    )
                )

                managerLight.lights.add(
                    drawer
                )
            }
        }
    }

    override fun export(
        file: File
    ) {
        val outFile = File(
            file.path + EXTENSION
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
            }

            val mapInterpolations = SparseArrayCompat<
                SDMLightPointInterpolation
            >(mapLight.size() / 2)

            mapLight.forEach {
                val keyHash = it.interpolation.hashCode()
                if (!mapInterpolations.containsKey(
                    keyHash
                )) {
                    mapInterpolations.put(
                        keyHash,
                        it.interpolation
                    )
                }
            }

            outStream.writeShort(
                mapInterpolations.size()
            )

            outStream.writeShort(
                mapLight.size()
            )

            outStream.writeShort(
                size
            )

            mapInterpolations.forEach {
                outStream.writeFloat(
                    it.constant
                )

                outStream.writeFloat(
                    it.linear
                )

                outStream.writeFloat(
                    it.radius
                )
            }

            mapLight.forEach {
                outStream.writeShort(
                    mapInterpolations.indexOfKey(
                        it.interpolation.hashCode()
                    )
                )

                writeLight(
                    outStream,
                    it
                )
            }

            forEach {
                outStream.writeShort(
                    mapLight.indexOfKey(
                        it.light.hashCode()
                    )
                )

                writePosition(
                    outStream,
                    it.modelMatrix
                )
            }
        }

        outStream.close()
    }

    private inline fun <T> SparseArrayCompat<T>.forEach(
        action: ((T) -> Unit)
    ) {
        for (i in 0 until size()) {
            get(keyAt(i))?.let {
                action(it)
            }
        }
    }

    private inline fun writeLight(
        outStream: DataOutputStream,
        light: SDMLightPoint
    ) {
        outStream.apply {
            writeByte(
                (light.color.x * 255).toInt()
            )

            writeByte(
                (light.color.y * 255).toInt()
            )

            writeByte(
                (light.color.z * 255).toInt()
            )

            writeByte(
                (light.alpha * 255).toInt()
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