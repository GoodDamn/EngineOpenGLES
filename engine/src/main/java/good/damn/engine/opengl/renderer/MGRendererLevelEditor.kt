package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.entities.MGDrawerSky
import good.damn.engine.opengl.entities.MGLandscape
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMeshStatic
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.rays.MGRayIntersection
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.ui.MGButtonGL
import good.damn.engine.opengl.ui.MGSeekBarGl
import good.damn.engine.touch.MGIListenerMove
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.touch.MGTouchFreeMove
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.utils.MGUtilsShader
import java.util.LinkedList
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
MGIListenerScale,
MGIListenerDelta,
MGIListenerMove {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mShaderDefault = MGShaderDefault()
    private val mShaderSkySphere = MGShaderSkySphere()

    private val modelMatrixSky = MGMMatrix()
    private val modelMatrixCamera = MGMMatrix()
    private val modelMatrixLandscape = MGMMatrix()

    private val mBatchArrayObject = MGArrayVertex()

    private val materialInteract = MGMaterial(
        mShaderDefault.material
    )

    private val materialLandscape = MGMaterial(
        mShaderDefault.material
    )

    private val mVerticesSky = MGArrayVertex()

    private val mTextureSky = MGTexture(
        mShaderDefault
    )

    private val mTextureInteract = MGTexture(
        mShaderDefault
    )

    private val mTextureLandscape = MGTexture(
        mShaderDefault
    )

    private val mLandscape = MGLandscape(
        mTextureLandscape,
        materialLandscape,
        modelMatrixLandscape,
        mShaderDefault
    )

    private val mCameraFree = MGCameraFree(
        mShaderDefault,
        modelMatrixCamera
    )

    private val mDrawerSky = MGDrawerSky(
        mShaderSkySphere,
        modelMatrixSky,
        mVerticesSky,
        mTextureSky
    )

    private val mTouchScale = MGTouchScale().apply {
        onScale = this@MGRendererLevelEditor
        onDelta = this@MGRendererLevelEditor
    }

    private val mTouchMove = MGTouchFreeMove().apply {
        setListenerMove(
            this@MGRendererLevelEditor
        )
        setListenerDelta(
            this@MGRendererLevelEditor
        )
    }

    private val mBtnLoadUserContent = MGButtonGL {
        requesterUserContent.requestUserContent(
            this,
            "*/*"
        )
    }

    private val mBtnSwitchWireframe = MGButtonGL {
        MGEngine.isWireframe = !MGEngine.isWireframe
        /*mHandler.post {
            glUseProgram(
                if (MGEngine.isWireframe) mProgramWireframe
                else mProgramDefault
            )
        }*/
    }

    private val mBtnPlaceMesh = MGButtonGL {
        placeMesh()
    }

    private val mBarSeekAmbient = MGSeekBarGl {
        mDrawerLightDirectional.ambient = it
    }

    private val mHandler = MGHandlerGl()

    private val mOutPointLead = MGVector(0f)
    private val mPointCamera = MGVector(0f)
    private val meshes = LinkedList<MGMeshStatic>()

    private var mWidth = 0
    private var mHeight = 0

    private val mDrawerLightDirectional = MGDrawerLightDirectional(
        mShaderDefault.light
    )

    private val mRayIntersection = MGRayIntersection(
        mLandscape
    )

    private var mCurrentModelInteract: MGMMatrix? = null

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        val programDefault = MGUtilsShader.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
        )

        val programWireframe = MGUtilsShader.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag_wireframe.glsl"
        )

        val programSkySphere = MGUtilsShader.createProgramFromAssets(
            "shaders/sky/vert.glsl",
            "shaders/sky/frag.glsl"
        )

        glLinkProgram(
            programSkySphere
        )

        glLinkProgram(
            programWireframe
        )

        glLinkProgram(
            programDefault
        )

        glUseProgram(
            programDefault
        )

        mShaderSkySphere.setupUniforms(
            programSkySphere
        )

        mShaderDefault.setupUniforms(
            programDefault
        )

        MGObject3D.createFromAssets(
            "objs/box.obj"
        ).run {
            mBatchArrayObject.configure(
                mShaderDefault,
                vertices,
                indices
            )
        }

        mTextureInteract.setupTexture(
            "textures/rock.jpg"
        )

        /*mCameraRotation.radius = 1250f
        mTouchScale.scale = mCameraRotation.radius

        mCameraRotation.setRotation(
            0f,
            0.01f
        )*/

        mTextureLandscape.setupTexture(
            "textures/terrain.png",
            GL_REPEAT
        )

        mTextureSky.setupTexture(
            "textures/sky/skysphere_light.jpg"
        )

        MGObject3D.createFromAssets(
            "objs/semi_sphere.obj"
        ).run {
            mVerticesSky.configure(
                mShaderSkySphere,
                vertices,
                indices
            )
        }

        mLandscape.apply {
            setResolution(
                1024,
                1024
            )

            displace(
                MGMapDisplace.createFromAssets(
                    "maps/terrain_height.png"
                )
            )
        }

        modelMatrixLandscape.setScale(
            3.0f,
            3.0f,
            3.0f
        )

        glEnable(
            GL_DEPTH_TEST
        )

        glEnable(
            GL_CULL_FACE
        )

        glCullFace(
            GL_FRONT
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceChanged: ${Thread.currentThread().name}")
        mWidth = width
        mHeight = height

        val fWidth = width.toFloat()
        val fHeight = height.toFloat()

        mCameraFree.setPerspective(
            width,
            height
        )

        val w: Float
        val h: Float

        if (width < height) {
            w = fWidth
            h = fHeight
        } else {
            w = fHeight
            h = fWidth
        }

        mTouchMove.setBoundsMove(
            0f,
            0f,
            fWidth * 0.5f,
            fHeight
        )

        val btnLen = w * 0.1f

        mTouchMove.setBoundsDelta(
            fWidth * 0.5f,
            0f,
            fWidth,
            fHeight
        )

        mBarSeekAmbient.bounds(
            0f,
            0f,
            btnLen,
            fHeight
        )

        mBtnSwitchWireframe.bounds(
            fWidth - btnLen,
            fHeight - btnLen,
            btnLen,
            btnLen
        )

        mBtnLoadUserContent.bounds(
            fWidth - btnLen,
            0f,
            btnLen,
            btnLen
        )

        mBtnPlaceMesh.bounds(
            (fWidth - btnLen) * 0.5f,
            (fHeight - btnLen) * 0.5f,
            btnLen,
            btnLen
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        val f = System.currentTimeMillis() % 100000L * 0.001f
        val fx = sin(f) * 840f
        val fz = cos(f) * 840f

        mDrawerLightDirectional.setPosition(
            fx, 600f, fz
        )

        mHandler.run()
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        glClear(GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT or
            GL_STENCIL_BUFFER_BIT
        )

        glClearColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )

        mShaderSkySphere.use()
        mCameraFree.draw()
        mDrawerSky.draw()

        mShaderDefault.use()
        mCameraFree.draw()
        mDrawerLightDirectional.draw()

        mLandscape.draw()

        meshes.forEach {
            it.draw()
        }

        glFlush()
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {
        val mapDisplace = MGMapDisplace.createFromStream(
            userContent.stream
        )

        Handler(
            Looper.getMainLooper()
        ).post {
            mHandler.post {
                mLandscape.displace(
                    mapDisplace
                )
            }
        }
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mBarSeekAmbient.intercept(event.x, event.y)
            mBtnLoadUserContent.intercept(event.x, event.y)
            mBtnSwitchWireframe.intercept(event.x, event.y)
            mBtnPlaceMesh.intercept(event.x, event.y)
        }

        mTouchMove.onTouchEvent(
            event
        )
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        mCameraFree.addRotation(
            dx * 0.001f,
            dy * 0.001f
        )
        mCameraFree.invalidatePosition()
        updateIntersection()
    }

    override fun onScale(
        scale: Float
    ) {
        //mCameraRotation.radius = scale
    }

    override fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        mCameraFree.addPosition(
            x, y,
            directionX,
            directionY
        )
        mCameraFree.invalidatePosition()
        updateIntersection()
    }

    private inline fun updateIntersection() {
        mPointCamera.x = modelMatrixCamera.x
        mPointCamera.y = modelMatrixCamera.y
        mPointCamera.z = modelMatrixCamera.z

        mRayIntersection.intersect(
            mPointCamera,
            mCameraFree.direction,
            mOutPointLead
        )

        mCurrentModelInteract?.run {
            x = mOutPointLead.x
            y = mOutPointLead.y
            z = mOutPointLead.z
            invalidatePosition()
        }
    }

    private fun placeMesh() {
        mHandler.post {
            val modelMatrix = MGMMatrix()
            mCurrentModelInteract = modelMatrix
            meshes.add(
                MGMeshStatic(
                    mShaderDefault,
                    modelMatrix,
                    materialInteract,
                    mTextureInteract,
                    mBatchArrayObject
                )
            )
        }
    }
}