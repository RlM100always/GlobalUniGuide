package com.techtravelcoder.alluniversityinformations.postDetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.techtravelcoder.alluniversityinformations.Fragment.CategoryFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.FavoriteFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.HomeFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.RecentFragment;

public class PageAdapter extends FragmentStateAdapter
{
    private static final int NUM_PAGES = 4;

    public PageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new CategoryFragment();
            case 2:
                return new RecentFragment();
            case 3:
                return new FavoriteFragment();
            default:
                return new HomeFragment(); // Default fragment to handle invalid position
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
