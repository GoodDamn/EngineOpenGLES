package good.damn.engine.utils

import android.content.Context
import android.net.Uri
import android.opengl.GLES30.GL_EXTENSIONS
import android.opengl.GLES30.GL_RENDERER
import android.opengl.GLES30.GL_SHADING_LANGUAGE_VERSION
import android.opengl.GLES30.GL_VENDOR
import android.opengl.GLES30.GL_VERSION
import android.opengl.GLES30.glGetString
import android.util.Log
import good.damn.engine.MGEngine
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

object MGUtilsFile {

    private const val TAG = "MGUtilsFile"

    @JvmStatic
    fun getPublicFile(
        localPath: String
    ) = File(
        MGEngine.DIR_PUBLIC,
        localPath
    )

    @JvmStatic
    fun glWriteExtensions() = File(
        MGEngine.DIR_PUBLIC,
        "extensions.txt"
    ).run {
        if (length() != 0L) {
            return@run
        }

        if (!exists() && createNewFile()) {
            Log.d(TAG, "onSurfaceCreated: $name is created")
        }

        val extensions = glGetString(
            GL_EXTENSIONS
        ).replace(" ".toRegex(), "\n")

        val numExt = extensions.count {
            it == '\n'
        }

        val vendor = glGetString(
            GL_VENDOR
        )

        val renderer = glGetString(
            GL_RENDERER
        )

        val version = glGetString(
            GL_VERSION
        )

        val glslVersion = glGetString(
            GL_SHADING_LANGUAGE_VERSION
        )

        outputStream().run {
            write(
                numExt.toString().encodeToByteArray()
            )
            write(10)
            write(
                extensions.encodeToByteArray()
            )

            write(10)
            write(10)

            write(
                renderer.encodeToByteArray()
            )
            write(10)

            write(
                vendor.encodeToByteArray()
            )
            write(10)

            write(
                version.encodeToByteArray()
            )

            write(10)
            write(
                glslVersion.encodeToByteArray()
            )

            close()
        }
    }

    @JvmStatic
    fun read(
        uri: Uri?,
        context: Context
    ): ByteArray? {
        if (uri == null) {
            return null
        }

        val inp = context.contentResolver
            .openInputStream(uri) ?: return null

        val data = readBytes(inp)

        inp.close()

        return data
    }


    @JvmStatic
    fun readBytes(
        inp: InputStream,
        buffer: ByteArray = MGEngine.BUFFER_MB
    ): ByteArray {

        val outArr = ByteArrayOutputStream()

        var n: Int

        while (true) {
            n = inp.read(buffer)
            if (n == -1) {
                break
            }
            outArr.write(buffer,0,n)
        }

        val data = outArr.toByteArray()
        outArr.close()

        return data
    }
}