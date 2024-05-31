package good.damn.opengles_engine.activities

import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import good.damn.opengles_engine.Application
import good.damn.opengles_engine.launchers.ContentLauncher
import good.damn.opengles_engine.level_editor.adapters.MeshAdapter
import good.damn.opengles_engine.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?>{

    private lateinit var mLevelEditorView: LevelEditorView
    private lateinit var mContentLauncher: ContentLauncher

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        mContentLauncher = ContentLauncher(
            context,
            context
        )

        val layout = LinearLayout(
            context
        )

        val recyclerViewMeshes = RecyclerView(
            context
        )

        mLevelEditorView = LevelEditorView(
            this
        )

        layout.orientation = LinearLayout
            .VERTICAL

        recyclerViewMeshes.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recyclerViewMeshes.setHasFixedSize(
            true
        )

        recyclerViewMeshes.adapter = Application.ASSETS.list(
            "objs"
        )?.let {
            MeshAdapter(
                it
            )
        }

        layout.addView(
            mLevelEditorView,
            -1,
            (Application.HEIGHT * 0.7f).toInt()
        )

        layout.addView(
            recyclerViewMeshes,
            -1,
            -2
        )

        setContentView(
            layout
        )
    }

    // External call
    fun loadFromUserDisk(
        mime: String
    ) {
        mContentLauncher.launch(
            mime
        )
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        mLevelEditorView.onLoadFromUserDisk(
            contentResolver.openInputStream(
                result
            )
        )
    }

}