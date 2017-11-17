package y2k.example.litho

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.litho.ComponentContext
import com.facebook.litho.LithoView
import y2k.example.litho.components.RssListComponent
import y2k.example.litho.components.SubscriptionsScreen
import y2k.litho.elmish.program

/**
 * Created by y2k on 07/07/2017.
 **/

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        program(SubscriptionsScreen)
    }

    class App : Application() {

        override fun onCreate() {
            super.onCreate()
            _app = this
            Fresco.initialize(this)
            com.facebook.soloader.SoLoader.init(this, false)
        }

        companion object {
            val app: Application get() = _app
            private lateinit var _app: Application
        }
    }
}

class EntitiesActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = ComponentContext(this)
        val component = RssListComponent.create(context)
            .subscription(intent.getSerializableExtra("data") as Subscription)
            .build()
        setContentView(LithoView.create(context, component))
    }
}