package good.damn.opengles_engine.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.View
import good.damn.opengles_engine.opengl.renderer.TrafficRenderer

class TrafficView(
    context: Context
): GLSurfaceView(
    context
) {

   init {
      setEGLContextClientVersion(
          3
      )

      setRenderer(
         TrafficRenderer()
      )

      renderMode = RENDERMODE_CONTINUOUSLY
   }


}