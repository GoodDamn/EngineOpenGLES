package good.damn.wrapper.views

import android.content.Context
import android.opengl.GLSurfaceView
import good.damn.engine.opengl.renderer.TrafficRenderer

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