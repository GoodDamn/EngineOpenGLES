package good.damn.engine.opengl.matrices;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.shaders.MGIShaderNormal;

public final class MGMatrixTransformationNormal<
  T extends MGMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final MGMatrixNormal normal;
    public MGMatrixTransformationNormal(
        @NonNull T model,
        @NonNull final MGIShaderNormal shader
    ) {
        this.model = model;
        normal = new MGMatrixNormal(
            shader,
            model.model
        );
    }
}
