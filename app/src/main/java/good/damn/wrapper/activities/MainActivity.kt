package good.damn.wrapper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import good.damn.wrapper.views.TrafficView

class MainActivity
: AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val context = this

        val trafficView = TrafficView(
            context
        )

        trafficView.setOnClickListener {
            val intent = Intent(
                context,
                LevelEditorActivity::class.java
            )

            intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
            )

            startActivity(intent)
        }

        setContentView(
            trafficView
        )
    }

}