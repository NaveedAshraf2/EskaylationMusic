package com.eskaylation.downloadmusic.ui.dialog.timer;

import com.azoft.carousellayoutmanager.CarouselLayoutManager.OnCenterItemSelectionListener;
import kotlin.jvm.internal.Ref.ObjectRef;


public final class TimePickerViewinit2 implements OnCenterItemSelectionListener {
    public final  ObjectRef $minuteAdapter;

    public TimePickerViewinit2(ObjectRef ref$ObjectRef) {
        this.$minuteAdapter = ref$ObjectRef;
    }

    public final void onCenterItemChanged(int i) {
        ((TimePickerAdapter) this.$minuteAdapter.element).setPos(i);
    }
}
