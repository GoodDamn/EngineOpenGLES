package good.damn.engine2.level

import good.damn.mapimporter.models.MIMMap

interface MGIProviderMapImport {
    fun import(
        map: MIMMap,
        localPathDir: String
    )
}