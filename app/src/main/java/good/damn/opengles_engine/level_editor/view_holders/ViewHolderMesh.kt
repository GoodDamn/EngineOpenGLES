package good.damn.opengles_engine.level_editor.view_holders

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import good.damn.opengles_engine.Application
import good.damn.opengles_engine.level_editor.listeners.OnClickMeshListener

class ViewHolderMesh(
    layout: View,
    private val mObjName: TextView
): RecyclerView.ViewHolder(
    layout
) {

    var onClickMeshListener: OnClickMeshListener? = null
        set(v) {
            itemView.setOnClickListener {
                onClickMeshListener?.onClick(
                    mObjName.text.toString()
                )
            }
            field = v
        }

    fun setMeshName(
        meshName: String
    ) {
        mObjName.text = meshName
    }

    companion object {
        fun create(
            context: Context
        ): ViewHolderMesh {

            val layout = CardView(
                context
            )

            val textView = TextView(
                context
            )

            val paramsTextView = FrameLayout
                .LayoutParams(
                    -2,
                    -2
                )

            paramsTextView.leftMargin = (Application
                .WIDTH * 0.1f).toInt()

            textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                Application.WIDTH * 0.08f
            )

            textView.text = "No obj name"

            textView.layoutParams = paramsTextView

            layout.addView(
                textView
            )

            return ViewHolderMesh(
                layout,
                textView
            )
        }
    }
}