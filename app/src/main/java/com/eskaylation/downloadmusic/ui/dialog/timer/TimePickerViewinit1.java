package com.eskaylation.downloadmusic.ui.dialog.timer;

import com.azoft.carousellayoutmanager.CarouselLayoutManager.OnCenterItemSelectionListener;

import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Ref.ObjectRef;


public final class TimePickerViewinit1 implements OnCenterItemSelectionListener {
    public final  ObjectRef $hourAdapter;

    public TimePickerViewinit1(ObjectRef ref$ObjectRef) {
        this.$hourAdapter = ref$ObjectRef;
    }

    public final void onCenterItemChanged(int i) {
        ((TimePickerAdapter) this.$hourAdapter.element).setPos(i);
    }
}
