package com.eskaylation.downloadmusic.ui.dialog.timer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.eskaylation.downloadmusic.R;

import java.util.HashMap;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;


public final class TimePickerView extends RelativeLayout {
    public HashMap _$_findViewCache;
    public OnTimePickerListener listener;
    public CarouselLayoutManager minuteLayoutManager;
    public CarouselLayoutManager secondLayoutManager;


    public interface OnTimePickerListener {
        void onCancelPick();

        void onTimePicker(String str, long j);
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public final long getTimeToLong(int i, int i2) {
        return (long) ((i * 60000) + (i2 * 1000));
    }

    public final CarouselLayoutManager getSecondLayoutManager() {
        CarouselLayoutManager carouselLayoutManager = this.secondLayoutManager;
        if (carouselLayoutManager != null) {
            return carouselLayoutManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("secondLayoutManager");
        throw null;
    }

    public final void setSecondLayoutManager(CarouselLayoutManager carouselLayoutManager) {
        Intrinsics.checkParameterIsNotNull(carouselLayoutManager, "<set-?>");
        this.secondLayoutManager = carouselLayoutManager;
    }

    public final CarouselLayoutManager getMinuteLayoutManager() {
        CarouselLayoutManager carouselLayoutManager = this.minuteLayoutManager;
        if (carouselLayoutManager != null) {
            return carouselLayoutManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("minuteLayoutManager");
        throw null;
    }

    public final void setMinuteLayoutManager(CarouselLayoutManager carouselLayoutManager) {
        Intrinsics.checkParameterIsNotNull(carouselLayoutManager, "<set-?>");
        this.minuteLayoutManager = carouselLayoutManager;
    }

    public final OnTimePickerListener getListener() {
        OnTimePickerListener onTimePickerListener = this.listener;
        if (onTimePickerListener != null) {
            return onTimePickerListener;
        }
        Intrinsics.throwUninitializedPropertyAccessException("listener");
        throw null;
    }

    public final void setListener(OnTimePickerListener onTimePickerListener) {
        Intrinsics.checkParameterIsNotNull(onTimePickerListener, "<set-?>");
        this.listener = onTimePickerListener;
    }

    public TimePickerView(Context context) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "context");
       
    }

    public TimePickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
     
        init(attributeSet);
    }

    public TimePickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkParameterIsNotNull(context, "context");
       
        init(attributeSet);
    }

    public final void init(AttributeSet attributeSet) {
        View.inflate(getContext(), R.layout.time_picker_view, this);
        this.minuteLayoutManager = new CarouselLayoutManager(1, true);
        CarouselLayoutManager carouselLayoutManager = this.minuteLayoutManager;
        String str = "minuteLayoutManager";

        if (carouselLayoutManager != null) {
            carouselLayoutManager.setMaxVisibleItems(1);
            CarouselLayoutManager carouselLayoutManager2 = this.minuteLayoutManager;
            if (carouselLayoutManager2 != null) {
                carouselLayoutManager2.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                Ref.ObjectRef ref$ObjectRef = new Ref.ObjectRef();
                ref$ObjectRef.element = new TimePickerAdapter(getContext());
                ((RecyclerView) _$_findCachedViewById(R.id.rvHour)).setHasFixedSize(true);
                RecyclerView recyclerView = (RecyclerView) _$_findCachedViewById(R.id.rvHour);
                String str2 = "rvHour";
                Intrinsics.checkExpressionValueIsNotNull(recyclerView, str2);
                CarouselLayoutManager carouselLayoutManager3 = this.minuteLayoutManager;
                if (carouselLayoutManager3 != null) {
                    recyclerView.setLayoutManager(carouselLayoutManager3);
                    ((RecyclerView) _$_findCachedViewById(R.id.rvHour)).addOnScrollListener(new CenterScrollListener());
                    RecyclerView recyclerView2 = (RecyclerView) _$_findCachedViewById(R.id.rvHour);
                    Intrinsics.checkExpressionValueIsNotNull(recyclerView2, str2);
                    recyclerView2.setAdapter((TimePickerAdapter) ref$ObjectRef.element);
                    ((TimePickerAdapter) ref$ObjectRef.element).initlistHour();
                    ((RecyclerView) _$_findCachedViewById(R.id.rvHour)).setHasFixedSize(true);
                    RecyclerView recyclerView3 = (RecyclerView) _$_findCachedViewById(R.id.rvHour);
                    Intrinsics.checkExpressionValueIsNotNull(recyclerView3, str2);
                    CarouselLayoutManager carouselLayoutManager4 = this.minuteLayoutManager;
                    if (carouselLayoutManager4 != null) {
                        recyclerView3.setLayoutManager(carouselLayoutManager4);
                        ((RecyclerView) _$_findCachedViewById(R.id.rvHour)).addOnScrollListener(new CenterScrollListener());
                        ((TimePickerAdapter) ref$ObjectRef.element).initlistHour();
                        RecyclerView recyclerView4 = (RecyclerView) _$_findCachedViewById(R.id.rvHour);
                        Intrinsics.checkExpressionValueIsNotNull(recyclerView4, str2);
                        recyclerView4.setAdapter((TimePickerAdapter) ref$ObjectRef.element);
                        CarouselLayoutManager carouselLayoutManager5 = this.minuteLayoutManager;
                        if (carouselLayoutManager5 != null) {
                            carouselLayoutManager5.addOnItemSelectionListener(new TimePickerViewinit1(ref$ObjectRef));
                            Ref.ObjectRef ref$ObjectRef2 = new Ref.ObjectRef();
                            ref$ObjectRef2.element = new TimePickerAdapter(getContext());
                            this.secondLayoutManager = new CarouselLayoutManager(1, true);
                            CarouselLayoutManager carouselLayoutManager6 = this.secondLayoutManager;
                            String str3 = "secondLayoutManager";
                            if (carouselLayoutManager6 != null) {
                                carouselLayoutManager6.setMaxVisibleItems(1);
                                CarouselLayoutManager carouselLayoutManager7 = this.secondLayoutManager;
                                if (carouselLayoutManager7 != null) {
                                    carouselLayoutManager7.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                    RecyclerView recyclerView5 = (RecyclerView) _$_findCachedViewById(R.id.rvMinute);
                                    if (recyclerView5 != null) {
                                        recyclerView5.setHasFixedSize(true);
                                    }
                                    RecyclerView recyclerView6 = (RecyclerView) _$_findCachedViewById(R.id.rvMinute);
                                    if (recyclerView6 != null) {
                                        CarouselLayoutManager carouselLayoutManager8 = this.secondLayoutManager;
                                        if (carouselLayoutManager8 != null) {
                                            recyclerView6.setLayoutManager(carouselLayoutManager8);
                                        } else {
                                            Intrinsics.throwUninitializedPropertyAccessException(str3);
                                            throw null;
                                        }
                                    }
                                    RecyclerView recyclerView7 = (RecyclerView) _$_findCachedViewById(R.id.rvMinute);
                                    if (recyclerView7 != null) {
                                        recyclerView7.addOnScrollListener(new CenterScrollListener());
                                    }
                                    ((TimePickerAdapter) ref$ObjectRef2.element).initListMinute();
                                    RecyclerView recyclerView8 = (RecyclerView) _$_findCachedViewById(R.id.rvMinute);
                                    if (recyclerView8 != null) {
                                        recyclerView8.setAdapter((TimePickerAdapter) ref$ObjectRef2.element);
                                    }
                                    CarouselLayoutManager carouselLayoutManager9 = this.secondLayoutManager;
                                    if (carouselLayoutManager9 != null) {
                                        carouselLayoutManager9.addOnItemSelectionListener(new TimePickerViewinit2(ref$ObjectRef2));
                                        ((Button) _$_findCachedViewById(R.id.btnCancel)).setOnClickListener(new TimePickerViewinit3(this));
                                        ((Button) _$_findCachedViewById(R.id.btnChose)).setOnClickListener(new TimePickerViewinit4(this));
                                        return;
                                    }
                                    Intrinsics.throwUninitializedPropertyAccessException(str3);
                                    throw null;
                                }
                                Intrinsics.throwUninitializedPropertyAccessException(str3);
                                throw null;
                            }
                            Intrinsics.throwUninitializedPropertyAccessException(str3);
                            throw null;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException(str);
                        throw null;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException(str);
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException(str);
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException(str);
        throw null;
    }

    public final void setTimePickerListener(OnTimePickerListener onTimePickerListener) {
        Intrinsics.checkParameterIsNotNull(onTimePickerListener, "mListener");
        this.listener = onTimePickerListener;
    }
}
