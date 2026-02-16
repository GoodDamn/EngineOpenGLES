package good.damn.wrapper.export

import android.util.Log
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

        glProvider.managers.managerLight

        outStream.close()
    }
}