package good.damn.engine.sdk.matrices;

import androidx.annotation.NonNull;

public final class SDMatrixTransformationInvert<
  T extends SDMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final SDMatrixInvert invert;
    public SDMatrixTransformationInvert(
        @NonNull T model
    ) {
        this.model = model;
        invert = new SDMatrixInvert(
            model.model
        );
    }
}
