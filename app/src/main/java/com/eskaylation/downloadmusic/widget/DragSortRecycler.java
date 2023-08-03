package com.eskaylation.downloadmusic.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;

public class DragSortRecycler extends ItemDecoration implements OnItemTouchListener {
    public float autoScrollSpeed = 0.5f;
    public float autoScrollWindow = 0.1f;
    public Paint bgColor = new Paint();
    public int dragHandleWidth = 0;
    public OnDragStateChangedListener dragStateChangedListener;
    public int fingerAnchorY;
    public int fingerOffsetInViewY;
    public int fingerY;
    public BitmapDrawable floatingItem;
    public float floatingItemAlpha = 0.5f;
    public int floatingItemBgColor = 0;
    public Rect floatingItemBounds;
    public Rect floatingItemStatingBounds;
    public boolean isDragging;
    public OnItemMovedListener moveInterface;
    public OnScrollListener scrollListener = new OnScrollListener() {
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            DragSortRecycler dragSortRecycler = DragSortRecycler.this;
            StringBuilder sb = new StringBuilder();
            sb.append("Scrolled: ");
            sb.append(i);
            sb.append(" ");
            sb.append(i2);
            dragSortRecycler.debugLog(sb.toString());
            DragSortRecycler dragSortRecycler2 = DragSortRecycler.this;
            dragSortRecycler2.fingerAnchorY = dragSortRecycler2.fingerAnchorY - i2;
        }
    };
    public int selectedDragItemPos = -1;
    public int viewHandleId = -1;

    public interface OnDragStateChangedListener {
        void onDragStart();

        void onDragStop();
    }

    public interface OnItemMovedListener {
        void onItemMoved(int i, int i2);
    }

    public boolean canDragOver(int i) {
        return true;
    }

    public final void debugLog(String str) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public OnScrollListener getScrollListener() {
        return this.scrollListener;
    }

    public void setOnItemMovedListener(OnItemMovedListener onItemMovedListener) {
        this.moveInterface = onItemMovedListener;
    }

    public void setViewHandleId(int i) {
        this.viewHandleId = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        debugLog("getItemOffsets");
        StringBuilder sb = new StringBuilder();
        sb.append("View top = ");
        sb.append(view.getTop());
        debugLog(sb.toString());
        if (this.selectedDragItemPos != -1) {
            int childPosition = recyclerView.getChildPosition(view);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("itemPos =");
            sb2.append(childPosition);
            debugLog(sb2.toString());
            if (canDragOver(childPosition)) {
                if (childPosition == this.selectedDragItemPos) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    Rect rect2 = this.floatingItemBounds;
                    float height = (float) (rect2.top + (rect2.height() / 2));
                    if (childPosition > this.selectedDragItemPos && ((float) view.getTop()) < height) {
                        float top = (height - ((float) view.getTop())) / ((float) view.getHeight());
                        if (top > 1.0f) {
                            top = 1.0f;
                        }
                        rect.top = -((int) (((float) this.floatingItemBounds.height()) * top));
                        rect.bottom = (int) (((float) this.floatingItemBounds.height()) * top);
                    }
                    if (childPosition < this.selectedDragItemPos && ((float) view.getBottom()) > height) {
                        float bottom = (((float) view.getBottom()) - height) / ((float) view.getHeight());
                        if (bottom > 1.0f) {
                            bottom = 1.0f;
                        }
                        rect.top = (int) (((float) this.floatingItemBounds.height()) * bottom);
                        rect.bottom = -((int) (((float) this.floatingItemBounds.height()) * bottom));
                    }
                }
            }
        } else {
            rect.top = 0;
            rect.bottom = 0;
            view.setVisibility(View.VISIBLE);
        }
    }

    public final int getNewPostion(RecyclerView recyclerView) {
        int childCount = recyclerView.getLayoutManager().getChildCount();
        Rect rect = this.floatingItemBounds;
        float height = (float) (rect.top + (rect.height() / 2));
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = recyclerView.getLayoutManager().getChildAt(i3);
            if (childAt.getVisibility() == View.VISIBLE) {
                int childPosition = recyclerView.getChildPosition(childAt);
                if (childPosition != this.selectedDragItemPos) {
                    float top = (float) (childAt.getTop() + (childAt.getHeight() / 2));
                    if (height > top) {
                        if (childPosition > i) {
                            i = childPosition;
                        }
                    } else if (height <= top && childPosition < i2) {
                        i2 = childPosition;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("above = ");
        sb.append(i);
        sb.append(" below = ");
        sb.append(i2);
        debugLog(sb.toString());
        if (i2 != Integer.MAX_VALUE) {
            if (i2 < this.selectedDragItemPos) {
                i2++;
            }
            return i2 - 1;
        }
        if (i < this.selectedDragItemPos) {
            i++;
        }
        return i;
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        boolean z;
        debugLog("onInterceptTouchEvent");
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null) {
            return false;
        }
        if (this.dragHandleWidth <= 0 || motionEvent.getX() >= ((float) this.dragHandleWidth)) {
            int i = this.viewHandleId;
            if (i != -1) {
                View findViewById = findChildViewUnder.findViewById(i);
                if (findViewById == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("The view ID ");
                    sb.append(this.viewHandleId);
                    sb.append(" was not found in the RecycleView item");
                    Log.e("DragSortRecycler", sb.toString());
                    return false;
                } else if (findViewById.getVisibility() != View.VISIBLE) {
                    return false;
                } else {
                    int[] iArr = new int[2];
                    findChildViewUnder.getLocationInWindow(iArr);
                    int[] iArr2 = new int[2];
                    findViewById.getLocationInWindow(iArr2);
                    int i2 = iArr2[0] - iArr[0];
                    int i3 = iArr2[1] - iArr[1];
                    z = new Rect(findChildViewUnder.getLeft() + i2, findChildViewUnder.getTop() + i3, findChildViewUnder.getLeft() + i2 + findViewById.getWidth(), findChildViewUnder.getTop() + i3 + findViewById.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("parentItemPos = ");
                    sb2.append(iArr[0]);
                    String str = " ";
                    sb2.append(str);
                    sb2.append(iArr[1]);
                    debugLog(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("handlePos = ");
                    sb3.append(iArr2[0]);
                    sb3.append(str);
                    sb3.append(iArr2[1]);
                    debugLog(sb3.toString());
                }
            } else {
                z = false;
            }
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        debugLog("Started Drag");
        setIsDragging(true);
        this.floatingItem = createFloatingBitmap(findChildViewUnder);
        this.fingerAnchorY = (int) motionEvent.getY();
        this.fingerOffsetInViewY = this.fingerAnchorY - findChildViewUnder.getTop();
        this.fingerY = this.fingerAnchorY;
        this.selectedDragItemPos = recyclerView.getChildPosition(findChildViewUnder);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("selectedDragItemPos = ");
        sb4.append(this.selectedDragItemPos);
        debugLog(sb4.toString());
        return true;
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        float f= 0;
        float height = 0;
        debugLog("onTouchEvent");
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (motionEvent.getAction() == 1 && this.selectedDragItemPos != -1) {
                int newPostion = getNewPostion(recyclerView);
                OnItemMovedListener onItemMovedListener = this.moveInterface;
                if (onItemMovedListener != null) {
                    onItemMovedListener.onItemMoved(this.selectedDragItemPos, newPostion);
                }
            }
            setIsDragging(false);
            this.selectedDragItemPos = -1;
            this.floatingItem = null;
            recyclerView.invalidateItemDecorations();
            return;
        }
        this.fingerY = (int) motionEvent.getY();
        if (this.floatingItem != null) {
            Rect rect = this.floatingItemBounds;
            rect.top = this.fingerY - this.fingerOffsetInViewY;
            if (rect.top < (-this.floatingItemStatingBounds.height()) / 2) {
                this.floatingItemBounds.top = (-this.floatingItemStatingBounds.height()) / 2;
            }
            Rect rect2 = this.floatingItemBounds;
            rect2.bottom = rect2.top + this.floatingItemStatingBounds.height();
            this.floatingItem.setBounds(this.floatingItemBounds);
        }
        float f2 = 0.0f;
        if (((float) this.fingerY) > ((float) recyclerView.getHeight()) * (1.0f - this.autoScrollWindow)) {
            f = (float) this.fingerY;
            height = ((float) recyclerView.getHeight()) * (1.0f - this.autoScrollWindow);
        } else {
            if (((float) this.fingerY) < ((float) recyclerView.getHeight()) * this.autoScrollWindow) {
                f = (float) this.fingerY;
                height = ((float) recyclerView.getHeight()) * this.autoScrollWindow;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Scroll: ");
            sb.append(f2);
            debugLog(sb.toString());
            recyclerView.scrollBy(0, (int) (f2 * this.autoScrollSpeed));
            recyclerView.invalidateItemDecorations();
        }
        f2 = f - height;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Scroll: ");
        sb2.append(f2);
        debugLog(sb2.toString());
        recyclerView.scrollBy(0, (int) (f2 * this.autoScrollSpeed));
        recyclerView.invalidateItemDecorations();
    }

    public final void setIsDragging(boolean z) {
        if (z != this.isDragging) {
            this.isDragging = z;
            OnDragStateChangedListener onDragStateChangedListener = this.dragStateChangedListener;
            if (onDragStateChangedListener == null) {
                return;
            }
            if (this.isDragging) {
                onDragStateChangedListener.onDragStart();
            } else {
                onDragStateChangedListener.onDragStop();
            }
        }
    }

    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, State state) {
        BitmapDrawable bitmapDrawable = this.floatingItem;
        if (bitmapDrawable != null) {
            bitmapDrawable.setAlpha((int) (this.floatingItemAlpha * 255.0f));
            this.bgColor.setColor(this.floatingItemBgColor);
            canvas.drawRect(this.floatingItemBounds, this.bgColor);
            this.floatingItem.draw(canvas);
        }
    }

    public final BitmapDrawable createFloatingBitmap(View view) {
        this.floatingItemStatingBounds = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        this.floatingItemBounds = new Rect(this.floatingItemStatingBounds);
        Bitmap createBitmap = Bitmap.createBitmap(this.floatingItemStatingBounds.width(), this.floatingItemStatingBounds.height(), Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        BitmapDrawable bitmapDrawable = new BitmapDrawable(view.getResources(), createBitmap);
        bitmapDrawable.setBounds(this.floatingItemBounds);
        return bitmapDrawable;
    }
}
