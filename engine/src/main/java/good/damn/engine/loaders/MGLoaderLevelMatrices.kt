package good.damn.engine.loaders

import good.damn.engine.models.MGProp
import good.damn.common.matrices.MGMatrixScaleRotation
import good.damn.common.matrices.MGMatrixTransformationNormal
import good.damn.mapimporter.models.MIMMap
import good.damn.mapimporter.models.MIMProp

class MGLoaderLevelMatrices {

    companion object {
        @JvmStatic
        fun fillModelMatrix(
            model: MGMatrixScaleRotation,
            prop: MIMProp
        ) {
            var roll = 0f
            var yaw = 0f
            var pitch = 0f

            prop.rotation?.let {
                roll += Math.toDegrees(it.x.toDouble()).toFloat()
                yaw += Math.toDegrees(it.y.toDouble()).toFloat()
                pitch += Math.toDegrees(it.z.toDouble()).toFloat()
            }

            model.setRotation(
                roll,
                pitch,
                yaw
            )

            model.setPosition(
                prop.position.x,
                prop.position.z,
                prop.position.y,
            )

            prop.scale?.let {
                model.setScale(
                    it.x,
                    it.z,
                    it.y
                )
            }

            model.invalidatePosition()
            model.invalidateScaleRotation()
        }
    }

    var isLoadMatrices = false
        private set

    fun loadMatrices(
        meshes: Map<String, MGProp>,
        map: MIMMap
    ) {
        isLoadMatrices = false
        for (prop in map.props) {
            val mesh = meshes[
                prop.name
            ] ?: continue

            mesh.matrices.add(
                MGMatrixTransformationNormal(
                    MGMatrixScaleRotation()
                ).apply {
                    fillModelMatrix(
                        model,
                        prop
                    )

                    normal.apply {
                        calculateInvertModel()
                        calculateNormalMatrix()
                    }
                }
            )
        }

        isLoadMatrices = true
    }
}