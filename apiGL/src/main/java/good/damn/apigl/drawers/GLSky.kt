package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.common.matrices.COMatrixScale
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.engine.MGObject3d
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGMMaterialShader

class GLSky {
    lateinit var meshMaterial: MGMMeshDrawer<
        MGShaderGeometryPassModel,
        good.damn.apigl.drawers.GLDrawerMeshMaterialMutable
    >
        private set

    fun configure(
        informator: MGMInformator
    ) {
        val verticesSky = good.damn.apigl.arrays.GLArrayVertexConfigurator(
            MGEnumArrayVertexConfiguration.SHORT
        )

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            verticesSky.configure(
                vertices,
                indices,
                good.damn.apigl.arrays.pointers.GLPointerAttribute.defaultNoTangent
            )
        }

        val localDirPath = "textures/sky"
        val materialShader = MGMMaterialShader.Builder(
            "sky",
            localDirPath,
            informator.shaders.source
        ).diffuse()
            .opacity()
            .emissive(1.0f)
            .normal()
            .useDepthConstant()
            .specular()
            .build()

        materialShader.materialTexture.load(
            informator.poolTextures,
            localDirPath
        )

        val shader = informator.shaders.cacheGeometryPass.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            informator.shaders.source.vert,
            MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build(),
            arrayOf(
                MGShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )

        meshMaterial = MGMMeshDrawer(
            shader,
            good.damn.apigl.drawers.GLDrawerMeshMaterialMutable(
                arrayOf(
                    GLMaterial(
                        materialShader.materialTexture
                    )
                ),
                good.damn.apigl.drawers.GLDrawerMesh(
                    good.damn.apigl.drawers.GLDrawerVertexArray(
                        verticesSky
                    ),
                    good.damn.apigl.drawers.GLDrawerPositionEntity(
                        COMatrixScale().apply {
                            setScale(
                                2000000f,
                                2000000f,
                                2000000f
                            )
                            invalidateScale()
                        }
                    ),
                    GLES30.GL_CW
                )
            )
        )
    }
}