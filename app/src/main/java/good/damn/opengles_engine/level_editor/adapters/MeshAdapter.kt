package good.damn.opengles_engine.level_editor.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import good.damn.opengles_engine.level_editor.listeners.OnClickMeshListener
import good.damn.opengles_engine.level_editor.view_holders.ViewHolderMesh

class MeshAdapter(
    private val meshNames: Array<String>,
    private val mOnClickMeshListener: OnClickMeshListener
): RecyclerView.Adapter<ViewHolderMesh>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderMesh {
        val mesh = ViewHolderMesh.create(
            parent.context,
        )
        mesh.onClickMeshListener = mOnClickMeshListener
        return mesh
    }

    override fun getItemCount(): Int {
        return meshNames.size
    }

    override fun onBindViewHolder(
        holder: ViewHolderMesh,
        position: Int
    ) {
        holder.setMeshName(
            meshNames[position]
        )
    }
}