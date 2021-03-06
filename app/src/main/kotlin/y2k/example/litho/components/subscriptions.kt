package y2k.example.litho.components

import android.text.Layout
import com.facebook.litho.widget.GridLayoutInfo
import com.facebook.litho.widget.VerticalGravity
import com.facebook.yoga.YogaEdge
import y2k.example.litho.*
import y2k.example.litho.R
import y2k.example.litho.Status.*
import y2k.example.litho.common.Error
import y2k.example.litho.common.Ok
import y2k.example.litho.common.Result
import y2k.example.litho.components.SubscriptionsScreen.Model
import y2k.example.litho.components.SubscriptionsScreen.Msg
import y2k.example.litho.components.SubscriptionsScreen.Msg.*
import y2k.litho.elmish.*
import y2k.example.litho.Loader as L

object SubscriptionsScreen : ElmFunctions<Model, Msg> {

    data class Model(
        val status: Status,
        val binder: ContextualRecyclerBinder<Subscription>,
        val cached: List<Subscription>)

    sealed class Msg {
        class FromCacheMsg(val value: Subscriptions) : Msg()
        class FromWebMsg(val value: Result<Subscriptions, Exception>) : Msg()
        class OpenMsg(val item: Subscription) : Msg()
    }

    override fun init(): Pair<Model, Cmd<Msg>> {
        val binder = ContextualRecyclerBinder(
            { layoutInfo(GridLayoutInfo(null, 2)) },
            ::createItemComponent, ::fastCompare)

        return Model(InProgress, binder, emptyList()) to
            Cmd.fromSuspend({ L.getCachedSubscriptions() }, ::FromCacheMsg)
    }

    override fun update(model: Model, msg: Msg): Pair<Model, Cmd<Msg>> = when (msg) {
        is OpenMsg -> model to Cmd.none() // FIXME:
        is FromCacheMsg -> model to
            Cmd.fromSuspend({ L.getSubscriptionsResult() }, ::FromWebMsg)
        is FromWebMsg -> when (msg.value) {
            is Ok -> model.copy(
                status = Success,
                binder = model.binder.copy(msg.value.value.value)) to Cmd.none()
            is Error -> model.copy(status = Failed) to Cmd.none()
        }
    }

    override fun view(model: Model) =
        column {
            child(recyclerView_ {
                binder_(model.binder)
            })
            if (model.status == InProgress)
                child(viewProgress())
            if (model.status == Failed)
                child(errorIndicator())
        }

    fun viewItem(item: Subscription) =
        column {
            heightDip(200)
            paddingDip(YogaEdge.ALL, 4)
            backgroundRes(R.drawable.sub_item_bg)
            childText { layout ->
                textAlignment(Layout.Alignment.ALIGN_CENTER)
                verticalGravity(VerticalGravity.CENTER)
                text(item.title)
                textSizeSp(35f)
                onClick(layout, OpenMsg(item))
                layout {
                    flexGrow(1f)
                }
            }
        }
}