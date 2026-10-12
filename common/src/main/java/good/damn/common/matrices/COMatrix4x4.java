package good.damn.common.matrices;

import androidx.annotation.NonNull;

public final class COMatrix4x4 {

    static {
        System.loadLibrary(
            "common"
        );
    }

    private final long mPointer;

    public COMatrix4x4() {
        mPointer = createMatrix();
    }

    public final void multiplyMatrix(
        @NonNull final COMatrix4x4 matrix
    ) {
        multiplyMatrix(
            mPointer,
            matrix.mPointer
        );
    }

    public final void release() {
        releaseMatrix(mPointer);
    }

    private native long createMatrix();

    private native void multiplyMatrix(
        long ptr1,
        long ptr2
    );

    private native void releaseMatrix(
        long ptr
    );
}
