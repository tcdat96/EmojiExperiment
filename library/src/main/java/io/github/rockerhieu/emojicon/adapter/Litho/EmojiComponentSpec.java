package io.github.rockerhieu.emojicon.adapter.Litho;

import android.text.Layout;
import android.view.View;

import com.facebook.litho.ClickEvent;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;

import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by cpu10661 on 10/5/17.
 */

@LayoutSpec
public class EmojiComponentSpec {

    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext c,
                                          @Prop String text,
                                          @Prop int textSizeDip,
                                          @Prop(optional = true) Layout.Alignment textAlignment) {

        return Text.create(c)
                .text(text)
                .textSizeDip(textSizeDip)
                .textAlignment(textAlignment)
                .withLayout()
                .paddingDip(YogaEdge.BOTTOM, 8)
                .clickHandler(EmojiComponent.onClick(c))
                .build();
    }

    @OnEvent(ClickEvent.class)
    static void onClick(ComponentContext c,
                        @FromEvent View view,
                        @Prop String text,
                        @Prop OnClickListener listener) {
        listener.onClickListener(new Emojicon(text));
    }

    public interface OnClickListener {
        void onClickListener(Emojicon emojicon);
    }
}
