package good.damn.engine.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.opengl.GLES30.GL_EXTENSIONS
import android.opengl.GLES30.GL_RENDERER
import android.opengl.GLES30.GL_SHADING_LANGUAGE_VERSION
import android.opengl.GLES30.GL_VENDOR
import android.opengl.GLES30.GL_VERSION
import android.opengl.GLES30.glGetString
import android.util.Log
import good.damn.common.utils.COUtilsInputStream
import good.damn.engine.MGEngine
import good.damn.engine.MGMountDirectory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

object MGUtilsFile {

    private const val TAG = "MGUtilsFile"

    @JvmStatic
    fun glWriteExtensions() = File(
        MGMountDirectory.DIRECTORY,
        "extensions.txt"
    ).run {
        if (length() != 0L) {
            return@run
        }

        if (!exists() && createNewFile()) {
            Log.d(TAG, "glWriteExtensions: $name is created")
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
}