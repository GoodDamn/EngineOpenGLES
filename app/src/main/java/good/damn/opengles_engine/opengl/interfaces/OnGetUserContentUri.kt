package good.damn.opengles_engine.opengl.interfaces

import android.net.Uri
import good.damn.opengles_engine.opengl.models.UserContent
import java.io.InputStream

interface OnGetUserContentUri {

    fun onGetUserContentUri(
        userContent: UserContent
    )

}