package y2k.example.litho.components

import com.facebook.litho.ComponentLayout
import com.facebook.litho.innerContext
import com.facebook.litho.widget.Recycler
import com.facebook.litho.widget.Text
import y2k.litho.elmish.Contextual

fun textL(f: Text.Builder.() -> Unit): Contextual<ComponentLayout.Builder> =
    { context -> Text.create(context).also(f).withLayout() }

fun recyclerL(f: Recycler.Builder.() -> Unit): Contextual<ComponentLayout.Builder> =
    { context -> Recycler.create(context).also(f).withLayout() }

fun ComponentLayout.ContainerBuilder.childLayout(c: Contextual<ComponentLayout.Builder>) {
    child(c(innerContext))
}