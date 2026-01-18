package good.damn.wrapper.files

import good.damn.wrapper.APApp
import java.io.File

class APFile(
    localPath: String
): File(
    APApp.DIR_PUBLIC,
    localPath
)