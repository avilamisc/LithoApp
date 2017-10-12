package y2k.example.litho

import com.facebook.litho.ClickEvent
import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.*
import com.facebook.litho.widget.Recycler
import com.facebook.litho.widget.RecyclerBinder
import com.facebook.litho.widget.innerContext
import y2k.example.litho.common.applyDiff
import y2k.example.litho.components.SubscriptionsScreen
import y2k.litho.elmish.Contextual

fun createItemComponent(x: Subscription): Contextual<Component<*>> = {
    val provider = ElmishItemProvider { (SubscriptionsScreen.viewItem(x))(it).build() }
    ElmishItemComponent.create(it).item(provider).build()
}

class ElmishItemProvider(
    val func: () -> ComponentLayout)

@LayoutSpec
class ElmishItemComponentSpec {
    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext, @Prop item: ElmishItemProvider): ComponentLayout =
            item.func()

        @OnEvent(ClickEvent::class)
        @JvmStatic
        fun onItemClicked(c: ComponentContext, @Param item: Subscription) {
//            c.startActivity<EntitiesActivity>(item)
            TODO()
        }
    }
}

fun Recycler.Builder.binder_(b: ContextualRecyclerBinder<*>) {
    binder(b.getBinder(innerContext()))
}

fun recyclerView_(f: Recycler.Builder.() -> Unit): Contextual<ComponentLayout.Builder> {
    return { context ->
        Recycler.create(context)
            .apply(f)
            .withLayout()
    }
}

class ContextualRecyclerBinder<T>(
    private val factory: RecyclerBinder.Builder.() -> Unit,
    private val func: (T) -> Contextual<Component<*>>,
    private val compareId: (T, T) -> Boolean) {

    private var wrapper: BinderWrapper<T>? = null

    fun getBinder(context: ComponentContext): RecyclerBinder {
        val w = wrapper
        return if (w != null) w.binder else {
            val b = RecyclerBinder.Builder().apply(factory).build(context)
            wrapper = BinderWrapper(b, emptyList(), context)
            b
        }
    }

    fun copy(newItems: List<T>): ContextualRecyclerBinder<T> {
        val w = wrapper
        if (w != null) {
            val binder = w.binder
            val oldItems = w.items
            val context = w.context

            binder.applyDiff(
                oldItems,
                newItems,
                { func(it)(context) },
                compareId)

            wrapper = w.copy(items = newItems)
        }
        return this
    }

    data class BinderWrapper<T>(
        val binder: RecyclerBinder,
        val items: List<T>,
        val context: ComponentContext)
}