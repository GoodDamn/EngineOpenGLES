package good.damn.common.matrices;

import androidx.annotation.NonNull;

public final class COMatrixTransformationInvert<
  T extends COMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final COMatrixInvert invert;
    public COMatrixTransformationInvert(
        @NonNull T model
    ) {
        this.model = model;
        invert = new COMatrixInvert(
            model.model
        );
    }
}
