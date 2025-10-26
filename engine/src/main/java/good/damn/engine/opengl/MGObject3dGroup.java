package good.damn.engine.opengl;

import android.opengl.GLES30;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.pools.MGPoolTextures;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.textures.MGTexture;
import good.damn.engine.opengl.triggers.MGITrigger;
import good.damn.engine.opengl.triggers.MGTriggerMesh;

public final class MGObject3dGroup {

    public static MGTriggerMesh[] createFromObjects(
        @NonNull final MGObject3d[] objs,
        @NonNull final MGDrawerVertexArray drawVertBox,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGPoolTextures poolTextures
    ) {
        @NonNull
        final MGTriggerMesh[] triggerMeshes = new MGTriggerMesh[
            objs.length
        ];

        @NonNull MGObject3d obj;
        for (
            int i = 0;
            i < triggerMeshes.length;
            i++
        ) {
            obj = objs[i];
            final MGArrayVertex arrayVertex = new MGArrayVertex();
            arrayVertex.configure(
                obj.vertices,
                obj.indices,
                MGArrayVertex.STRIDE
            );

            @NonNull MGTexture texture = obj.texturesDiffuseFileName == null ?
                poolTextures.getDefaultTexture()
            : loadTextureCached(
                poolTextures,
                shaderDefault,
                MGEnumTextureType.DIFFUSE,
                obj.texturesDiffuseFileName[0]
            );

            triggerMeshes[i] = MGTriggerMesh.createFromVertexArray(
                arrayVertex,
                drawVertBox,
                shaderDefault,
                shaderWireframe,
                new MGDrawerModeSwitch(
                    arrayVertex,
                    new MGDrawerMeshOpaque(
                        arrayVertex,
                        texture,
                        new MGMaterial(
                            shaderDefault.getMaterial()
                        )
                    ),
                    GLES30.GL_CW
                ),
                triggerAction
            );
        }

        return triggerMeshes;
    }

    @NonNull
    private static MGTexture loadTextureCached(
        @NonNull final MGPoolTextures poolTextures,
        @NonNull final MGShaderDefault shaderDefault,
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
                shaderDefault
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
