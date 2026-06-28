package good.damn.engine.sdk.matrices;

import androidx.annotation.NonNull;

public final class SDMatrixTransformationNormal<
  T extends SDMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final SDMatrixNormal normal;
    public SDMatrixTransformationNormal(
        @NonNull T model
    ) {
        this.model = model;
        normal = new SDMatrixNormal(
            model.model
        );
    }
}
