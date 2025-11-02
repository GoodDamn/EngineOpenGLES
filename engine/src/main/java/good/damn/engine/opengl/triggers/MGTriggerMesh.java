package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGObject3d;
import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;
import good.damn.engine.opengl.drawers.MGDrawerModeTexture;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.entities.MGMesh;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolMesh;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.pools.MGPoolMeshesStatic;
import good.damn.engine.opengl.pools.MGPoolTextures;
import good.damn.engine.opengl.shaders.MGIShaderTexture;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.textures.MGTexture;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGMesh mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGMesh mesh,
        @NonNull final MGDrawerTriggerStateable triggerState
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGPoolTextures poolTextures,
        @NonNull final MGDrawerVertexArray drawVertBox,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        final MGArrayVertex arrayVertex = new MGArrayVertex();
        arrayVertex.configure(
            obj.vertices,
            obj.indices,
            MGArrayVertex.STRIDE
        );

        @NonNull final MGShaderMaterial mat = shaderDefault
            .getMaterial();

        @NonNull final MGTexture textureDiffuse = obj.texturesDiffuseFileName == null ?
            poolTextures.getDefaultTexture()
        : loadTextureCached(
            poolTextures,
            mat.getTextureDiffuse(),
            MGEnumTextureType.DIFFUSE,
            obj.texturesDiffuseFileName[0]
        );

        @NonNull final MGTexture textureMetallic = obj.texturesMetallicFileName == null ?
            poolTextures.getDefaultTextureMetallic()
        : loadTextureCached(
            poolTextures,
            mat.getTextureMetallic(),
            MGEnumTextureType.METALLIC,
            obj.texturesMetallicFileName[0]
        );

        @NonNull final MGTexture textureEmissive = obj.texturesEmissiveFileName == null ?
            poolTextures.getDefaultTextureEmissive()
        : loadTextureCached(
            poolTextures,
            mat.getTextureEmissive(),
            MGEnumTextureType.EMISSIVE,
            obj.texturesEmissiveFileName[0]
        );

        @NonNull final MGDrawerVertexArray drawerVertexArray = new MGDrawerVertexArray(
            arrayVertex,
            GLES30.GL_TRIANGLES
        );

        return createFromVertexArray(
            arrayVertex,
            drawVertBox,
            shaderDefault,
            shaderWireframe,
            new MGDrawerModeSwitch(
                arrayVertex,
                new MGDrawerMeshOpaque(
                    arrayVertex,
                    new MGMaterial(
                        shaderDefault.getMaterial(),
                        textureDiffuse,
                        textureMetallic,
                        textureEmissive
                    )
                ),
                new MGDrawerModeTexture(
                    textureDiffuse,
                    drawerVertexArray
                ),
                new MGDrawerModeTexture(
                    textureMetallic,
                    drawerVertexArray
                ),
                new MGDrawerModeTexture(
                    textureEmissive,
                    drawerVertexArray
                ),
                GLES30.GL_CW
            ),
            outPoolMesh,
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertex vertexArray,
        @NonNull final MGDrawerVertexArray drawerVertArrayBox,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGDrawerModeSwitch drawerModeSwitch,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    )  {
        outPoolMesh.pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            vertexArray
        );

        outPoolMesh.pointMiddle = outPoolMesh.pointMinMax.first.interpolate(
            outPoolMesh.pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            vertexArray,
            outPoolMesh.pointMiddle
        );

        outPoolMesh.drawerMode = drawerModeSwitch;
        outPoolMesh.vertexArray = vertexArray;

        return createFromMeshPool(
            shaderDefault,
            outPoolMesh.toImmutable(),
            drawerVertArrayBox,
            triggerAction,
            shaderWireframe
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGMPoolMesh poolMesh,
        @NonNull final MGDrawerVertexArray drawerVertArrayBox,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGShaderSingleMode shaderWireframe
    ) {
        @NonNull final Pair<
            MGVector, MGVector
        > pointMinMax = poolMesh.getPointMinMax();

        @NonNull final MGVector pointMiddle = poolMesh
            .getPointMiddle();

        @NonNull
        final MGMatrixTriggerMesh matrix = new MGMatrixTriggerMesh(
            new MGMatrixTransformationInvert<>(
                new MGMatrixScaleRotation()
            ),
            new MGMatrixTransformationNormal<>(
                new MGMatrixScaleRotation(),
                shaderDefault
            ),
            pointMinMax.first,
            pointMinMax.second
        );

        matrix.setPosition(
            pointMiddle.getX(),
            pointMiddle.getY(),
            pointMiddle.getZ()
        );

        matrix.invalidatePosition();
        matrix.invalidateScaleRotation();
        matrix.calculateInvertTrigger();
        matrix.calculateNormals();

        @NonNull
        final MGMesh mesh = new MGMesh(
            poolMesh.getDrawerMode(),
            shaderDefault,
            matrix.matrixMesh.model,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerTriggerStateable triggerState = new MGDrawerTriggerStateable(
            new MGManagerTriggerStateCallback(
                new MGTriggerMethodBox(
                    matrix.matrixTrigger.invert
                ),
                triggerAction
            ),
            drawerVertArrayBox,
            shaderWireframe,
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            mesh,
            triggerState
        );
    }

    @NonNull
    private static MGTexture loadTextureCached(
        @NonNull final MGPoolTextures poolTextures,
        @NonNull final MGIShaderTexture shaderTexture,
        @NonNull final MGEnumTextureType textureType,
        @NonNull final String textureName
    ) {
        @Nullable MGTexture texture = poolTextures.get(
            textureName
        );

        if (texture != null) {
            return texture;
        }

        try {
            texture = MGTexture.Companion.createDefaultAsset(
                textureName,
                textureType,
                shaderTexture
            );

            poolTextures.add(
                textureName,
                texture
            );
        } catch (Exception e) {
            // file not found
        }

        return texture != null ?
            texture
            : poolTextures.getDefaultTexture();
    }
}
