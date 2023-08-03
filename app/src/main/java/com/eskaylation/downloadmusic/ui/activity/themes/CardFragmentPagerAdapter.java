package com.eskaylation.downloadmusic.ui.activity.themes;

import android.content.Context;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.utils.AppConstants;
import java.util.ArrayList;
import java.util.List;
import com.eskaylation.downloadmusic.R;
public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {
    public float baseElevation;
    public List<CardFragment> fragments = new ArrayList();
    public ArrayList<BackgroundModel> listBackground;

    public CardFragmentPagerAdapter(Context context, FragmentManager fragmentManager, float f) {
        super(fragmentManager);
        this.baseElevation = f;
        this.listBackground = AppConstants.listBackground(context);
        for (int i = 0; i < this.listBackground.size(); i++) {
            addCardFragment(CardFragment.getInstance((BackgroundModel) this.listBackground.get(i)));
        }
    }

    public float getBaseElevation() {
        return this.baseElevation;
    }

    public ConstraintLayout getCardViewAt(int i) {
        return ((CardFragment) this.fragments.get(i)).getRootBG();
    }

    public int getCount() {
        return this.fragments.size();
    }

    public Fragment getItem(int i) {
        return CardFragment.getInstance((BackgroundModel) this.listBackground.get(i));
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Object instantiateItem = super.instantiateItem(viewGroup, i);
        this.fragments.set(i, (CardFragment) instantiateItem);
        return instantiateItem;
    }

    public void addCardFragment(CardFragment cardFragment) {
        this.fragments.add(cardFragment);
    }
}
