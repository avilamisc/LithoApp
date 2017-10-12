package y2k.example.litho.components

import android.graphics.Color
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.widget.Progress
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaPositionType
import y2k.litho.elmish.childWithLayout
import y2k.litho.elmish.column
import y2k.litho.elmish.progressL

/**
 * Created by y2k on 11/07/2017.
 **/

@LayoutSpec
class PlaceholderComponentSpec {

    companion object {

        @OnCreateLayout
        @JvmStatic
        fun onCreateLayout(c: ComponentContext): ComponentLayout =
            Column.create(c)
                .alignItems(YogaAlign.CENTER)
                .justifyContent(YogaJustify.CENTER)
                .child(Progress.create(c)
                    .color(Color.GRAY)
                    .withLayout()
                    .widthDip(50).heightDip(50))
                .build()
    }
}

@Deprecated("")
fun ComponentContext.errorIndicator(): ComponentLayout.ContainerBuilder =
    Column.create(this)
        .backgroundColor(0xFF303030L.toInt())
        .paddingDip(YogaEdge.ALL, 4)
        .child(Text.create(this)
            .textSizeSp(24f)
            .textColor(Color.WHITE)
            .text("ERROR")
            .withLayout().alignSelf(YogaAlign.FLEX_END))

fun errorIndicator() =
    column {
        backgroundColor(0xFF303030L.toInt())
        paddingDip(YogaEdge.ALL, 4)

        childWithLayout(textL {
            textSizeSp(24f)
            textColor(Color.WHITE)
            text("ERROR")
        }, {
            alignSelf(YogaAlign.CENTER)
        })
    }

@Deprecated("")
fun ComponentContext.preloadIndicator(): ComponentLayout.Builder =
    Progress.create(this)
        .color(Color.GRAY)
        .withLayout()
        .positionType(YogaPositionType.ABSOLUTE)
        .alignSelf(YogaAlign.CENTER)
        .widthDip(50).heightDip(50)

fun viewProgress() =
    progressL { layout ->
        color(Color.GRAY)
        layout {
            widthDip(50)
            heightDip(50)
            positionType(YogaPositionType.ABSOLUTE)
            alignSelf(YogaAlign.CENTER)
        }
    }