package good.damn.wrapper.contracts;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class APContractActivityGetMultipleContent
extends ActivityResultContract<
    String[],
    Uri[]
> {
    @NonNull
    @Override
    public Intent createIntent(
        @NonNull Context context,
        String[] inputTypes
    ) {
        return new Intent(
            Intent.ACTION_GET_CONTENT
        ).addCategory(
            Intent.CATEGORY_OPENABLE
        ).setType(
            inputTypes[0]
        ).putExtra(
            Intent.EXTRA_ALLOW_MULTIPLE,
            true
        );
    }

    @Override
    public Uri[] parseResult(
        final int resultCode,
        final @Nullable Intent intent
    ) {
        if (resultCode != Activity.RESULT_OK ||
            intent == null
        ) {
            return null;
        }

        @Nullable
        final Uri data = intent.getData();

        @Nullable
        final ClipData clipData = intent.getClipData();

        int totalCount = 0;
        int offsetData = 0;
        if (data != null) {
            totalCount++;
            offsetData++;
        }

        if (clipData != null) {
            totalCount += clipData.getItemCount();
        }

        final Uri[] output = new Uri[
            totalCount
        ];

        if (data != null) {
            output[0] = data;
        }

        if (clipData != null) {
            final int count = clipData.getItemCount();
            for (int i = 0; i < count; i++) {
                output[offsetData++] = clipData.getItemAt(
                    i
                ).getUri();
            }
        }

        return output;
    }
}
