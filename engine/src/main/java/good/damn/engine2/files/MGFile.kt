package good.damn.engine2.files

import good.damn.engine2.MGMountDirectory
import java.io.File

class MGFile(
    localPath: String
): File(
    MGMountDirectory.DIRECTORY,
    localPath
)