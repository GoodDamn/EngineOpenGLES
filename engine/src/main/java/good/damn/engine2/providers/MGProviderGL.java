package good.damn.engine2.providers;

import androidx.annotation.NonNull;

public class MGProviderGL {
    @NonNull
    private MGMProviderGL glProvider;

    public final void setGlProvider(
        @NonNull MGMProviderGL v
    ) {
        glProvider = v;
        onSetProviderGl();
    }

    @NonNull
    public final MGMProviderGL getGlProvider() {
        return glProvider;
    }

    protected void onSetProviderGl() {}
}
