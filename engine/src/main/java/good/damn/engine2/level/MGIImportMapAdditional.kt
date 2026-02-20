package good.damn.engine2.level

import good.damn.mapimporter.models.MIMMap

interface MGIImportMapAdditional {

    fun hasValidExtension(
        fileName: String
    ): Boolean

    fun import(
        map: MIMMap,
        localPathDir: String
    )
}