package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.engine2.models.json.light.MGMLight
import good.damn.wrapper.models.APMUserContent
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.sdk.models.SDMLightPointInterpolation
import good.damn.engine2.utils.MGUtilsJson
import good.damn.engine2.utils.MGUtilsVector3
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.wrapper.hud.bridges.APRayIntersectImplLight
import good.damn.wrapper.providers.APProviderGL

class APImportLight(
    private val bridgeRay: APBridgeRayIntersect
): APProviderGL(),
APIImport {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".light"
    )

    override fun processUserContent(
        userContent: APMUserContent
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

        val triggerLight = LGTriggerStateableLight.createFromLight(
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

            bridgeRay.intersectUpdate = APRayIntersectImplLight(
                modelMatrix,
                light.interpolation
            )
        }

        val drawerLightPoint = GLDrawerLightPoint(
            triggerLight.modelMatrix.matrixTrigger.model,
            triggerLight.light
        )

        glProvider.managers.managerLight.register(
            drawerLightPoint
        )

        glProvider.managers.managerFrustrum.volumes.add(
            GLVolumeLight(
                drawerLightPoint,
                triggerLight.modelMatrix.matrixTrigger.model
            )
        )
    }


}