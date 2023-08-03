package com.eskaylation.downloadmusic.ui.dialog.timer;

import android.view.View;
import android.view.View.OnClickListener;


public final class TimePickerViewinit3 implements OnClickListener {
    public final  TimePickerView this$0;

    public TimePickerViewinit3(TimePickerView timePickerView) {
        this.this$0 = timePickerView;
    }

    public final void onClick(View view) {
        if (this.this$0.getListener() != null) {
            this.this$0.getListener().onCancelPick();
        }
    }
}
