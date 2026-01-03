package good.damn.engine.imports

import good.damn.engine.models.json.light.MGMLight
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.sdk.models.SDMLightPointInterpolation
import good.damn.engine.utils.MGUtilsJson
import good.damn.engine.utils.MGUtilsVector3

class MGImportImplLight(
    private val bridgeRay: MGBridgeRayIntersect,
    private val managerLight: MGManagerLight
): MGIImport {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".light"
    )

    override fun processUserContent(
        userContent: MGMUserContent
    ) {
        val json = try {
            MGUtilsJson.createFromStream(
                userContent.stream
            )
        } catch (
            e: Exception
        ) {
            return
        }

        val jsonModel = MGMLight.createFromJson(
            json
        )

        val triggerLight = MGDrawerTriggerStateableLight.createFromLight(
            SDMLightPoint(
                MGUtilsVector3.createFromColorInt(
                    jsonModel.color
                ),
                jsonModel.interpolation.run {
                    SDMLightPointInterpolation(
                        constant,
                        linear,
                        quad,
                        radius
                    )
                }
            )
        ).apply {
            modelMatrix.radius = light.interpolation.radius
            modelMatrix.invalidatePosition()
            modelMatrix.invalidateRadius()
            modelMatrix.calculateInvertTrigger()

            bridgeRay.matrix = modelMatrix
        }

        managerLight.register(
            MGDrawerLightPoint(
                MGDrawerPositionEntity(
                    triggerLight.modelMatrix
                        .matrixTrigger.model
                ),
                triggerLight
            )
        )
    }


}