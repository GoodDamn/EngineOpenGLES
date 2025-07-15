package good.damn.engine.opengl

import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.camera.BaseCamera
import good.damn.engine.opengl.light.DirectionalLight
import good.damn.engine.utils.FileUtils
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Level(
    private val meshes: Array<StaticMesh>,
    private val directionalLight: DirectionalLight
) {

    companion object {

        private const val TAG = "Level"

        fun createFromAssets(
            path: String,
            program: Int,
            camera: BaseCamera
        ): Level {

            val inp = DataInputStream(
                MGEngine.ASSETS.open(
                    path
                )
            )

            val buffer = ByteArray(255)

            val meshes = Array(
                inp.readByte().toInt() and 0xff
            ) {

                val posX = inp.readFloat()
                val posY = inp.readFloat()
                val posZ = inp.readFloat()

                val yaw = inp.readFloat()
                val pitch = inp.readFloat()
                val roll = inp.readFloat()

                val scaleX = inp.readFloat()
                val scaleY = inp.readFloat()
                val scaleZ = inp.readFloat()

                var len = inp.readByte().toInt() and 0xff
                inp.read(
                    buffer,
                    0,
                    len
                )
                val objName = String(
                    buffer,
                    0,
                    len,
                    MGEngine.CHARSET
                )

                len = inp.readByte().toInt() and 0xff
                inp.read(buffer,0,len)
                val textureName = String(
                    buffer,
                    0,
                    len,
                    MGEngine.CHARSET
                )

                val specIntensity = inp.readByte().toInt() and 0xff
                val shine = inp.readByte().toInt() and 0xff

                val mesh = StaticMesh(
                    Object3D.createFromAssets(
                        "objs/$objName.obj"
                    ),
                    "textures/$textureName",
                    program
                )

                mesh.material.shine = shine.toFloat()
                mesh.material.specular = specIntensity.toFloat()

                mesh.setPosition(
                    posX,
                    posY,
                    posZ
                )

                mesh
            }

            inp.close()
            return Level(
                meshes,
                DirectionalLight(
                    program
                )
            )
        }


        fun saveToAssets(
            fileName: String,
            meshes: Array<EditorMesh>
        ) {

            val folder = FileUtils.getDocumentsFolder()
            val file = File("$folder/$fileName")

            if (!file.exists() && file.createNewFile()) {
                Log.d(TAG, "saveToAssets: $fileName is created")
            }

            val fos = DataOutputStream(FileOutputStream(
                file
            ))

            fos.writeByte(
                meshes.size
            )

            meshes.forEach {
                writeVector(
                    it.position,
                    fos
                )

                writeVector(
                    it.rotation,
                    fos
                )

                writeVector(
                    it.scale,
                    fos
                )

                /*writeString(
                    it.objName,
                    fos
                )*/

                writeString(
                    it.texName,
                    fos
                )

                fos.writeByte(
                    it.specIntensity.toInt()
                )

                fos.writeByte(
                    it.shininess.toInt()
                )

            }

            fos.close()
        }

        private fun writeString(
            s: String,
            os: OutputStream
        ) {
            val ss = s.toByteArray(
                MGEngine.CHARSET
            )
            os.write(ss.size)
            os.write(ss)
        }

        private fun writeVector(
            vector: Vector,
            fos: DataOutputStream
        ) {
            fos.writeFloat(
                vector.x
            )

            fos.writeFloat(
                vector.y
            )

            fos.writeFloat(
                vector.z
            )
        }

    }

    fun draw() {
        directionalLight.draw()
        /*meshes.forEach {
            it.draw()
        }*/
    }

}