package com.eskaylation.downloadmusic.ui.dialog.timer;

import android.view.View;
import android.view.View.OnClickListener;

import com.eskaylation.downloadmusic.R;
import com.google.android.exoplayer2.text.webvtt.WebvttCueParser;

public final class TimePickerViewinit4 implements OnClickListener {
    public final  TimePickerView this$0;

    public TimePickerViewinit4(TimePickerView timePickerView) {
        this.this$0 = timePickerView;
    }

    public final void onClick(View view) {
        if (this.this$0.getListener() != null) {
            int centerItemPosition = this.this$0.getMinuteLayoutManager().getCenterItemPosition();
            int centerItemPosition2 = this.this$0.getSecondLayoutManager().getCenterItemPosition();
            if (centerItemPosition >= 60) {
                centerItemPosition -= 61;
            }
            if (centerItemPosition2 > 60) {
                centerItemPosition2 -= 61;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(centerItemPosition);
            sb.append(' ');
            sb.append(this.this$0.getContext().getString(R.string.minute));
            sb.append(' ');
            sb.append(centerItemPosition2);
            sb.append(' ');
            sb.append(this.this$0.getContext().getString(R.string.second));
            this.this$0.getListener().onTimePicker(sb.toString(), this.this$0.getTimeToLong(centerItemPosition, centerItemPosition2));
        }
    }
}
