package good.damn.engine.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.utils.MGUtilsAsset.Companion
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class MGUtilsFile {

    companion object {
        private val TAG = "MGUtilsFile"

        fun getPublicFile(
            localPath: String
        ) = File(
            MGEngine.DIR_PUBLIC,
            localPath
        )

        fun createShaderPublicDir() {
            val dir = getPublicFile(
                "shaders"
            )

            if (!dir.exists() && dir.mkdirs()) {
                Log.d(TAG, "copyShaderCodeToExternal: ${dir.name} is created")
            }
        }

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

        fun readFile(
            file: File,
            buffer: ByteArray
        ): ByteArray {
            val fis = FileInputStream(file)

            val d = readBytes(
                fis,
                buffer
            )

            fis.close()

            return d
        }

        fun fromDoc(
            fileName: String
        ): ByteArray? {
            val docPath = getDocumentsFolder()
                .path

            val file = File("$docPath/$fileName")

            if (!file.exists()) {
                return null
            }

            val inps = FileInputStream(file)

            val b = readBytes(inps)

            inps.close()

            return b
        }

        fun writeToDoc(
            fileName: String,
            data: ByteArray,
            offset: Int = 0,
            length: Int = data.size
        ): String? {

            val docPath = getDocumentsFolder()
                .path

            val file = File("$docPath/$fileName")

            return writeFile(
                file,
                data,
                offset,
                length
            )
        }

        private fun writeFile(
            file: File,
            data: ByteArray,
            offset: Int = 0,
            length: Int = data.size
        ): String? {
            return try {
                if (!file.exists() && file.createNewFile()) {
                    Log.d(TAG, "writeFile: $file is created")
                }

                val fos = FileOutputStream(file)
                fos.write(data, offset, length)
                fos.close()
                null
            } catch (e: IOException) {
                Log.d(TAG, "writeFile: EXCEPTION: ${e.message}")
                "${e.message} for ${file.path} WHICH EXISTING IS ${file.exists()}"
            }
        }

        fun getDocumentsFolder(): File {
            val dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            val subDir = File(dir, "ServerDir")

            if (!subDir.exists() && subDir.mkdir()) {
                Log.d(TAG, "writeToDoc: dir $subDir is created")
            }

            return subDir
        }

        fun hasUserRsa(
            user: String
        ): Boolean {
            return getUserRsaFile(
                user
            ).exists()
        }

        fun isUserFolderExists(
            user: String
        ): Boolean {
            return getUserFolder(user)
                .exists()
        }

        fun getUserRsaFile(
            user: String
        ): File {
            return getUserFolder(
                "$user/key"
            )
        }

        fun saveUserRsa(
            user: String,
            rsaKey: String
        ) {
            val folder = getUserFolder(
                user
            ).path

            val file = File("$folder/key")

            Log.d(TAG, "saveUserRsa: $file")
            
            writeFile(
                file,
                rsaKey.toByteArray(
                    MGEngine.CHARSET_ASCII
                )
            )
        }

        fun getUsersRsa(
            buffer: ByteArray
        ): HashSet<String> {
            val hashSet = HashSet<String>()
            val users = getUsers() ?: return hashSet
            for (user in users) {
                val keyFile = File("${user.path}/key")
                if (keyFile.exists()) {
                    val keyData = readFile(
                        keyFile,
                        buffer
                    )
                    hashSet.add(String(
                        keyData,
                        MGEngine.CHARSET_ASCII
                    ))
                }
            }
            return hashSet
        }

        fun getUsers(): Array<File>? {
            return File("${getDocumentsFolder()}/ssh")
                .listFiles()
        }

        fun getUserFolder(
            user: String
        ): File {
            return File("${getDocumentsFolder()}/ssh/${user}")
        }

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

}