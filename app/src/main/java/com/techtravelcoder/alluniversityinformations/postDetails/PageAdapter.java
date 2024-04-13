package com.techtravelcoder.alluniversityinformations.postDetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.techtravelcoder.alluniversityinformations.Fragment.CategoryFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.FavoriteFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.HomeFragment;
import com.techtravelcoder.alluniversityinformations.Fragment.RecentFragment;

public class PageAdapter extends FragmentPagerAdapter
{
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new HomeFragment();
            case 1 : return new CategoryFragment();
            case 2 : return new RecentFragment();
            //case 3 : return new FavoriteFragment();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
