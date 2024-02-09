package com.example.belotcounter;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class FragmentAdapter extends FragmentPagerAdapter {
    private Context myContext;
    public FragmentResults fragmentResults;
    public FragmentGraph fragmentGraph;
    public FragmentAwards fragmentAwards;
    int totalTabs;
    public FragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragmentResults = new FragmentResults();
                return fragmentResults;
            case 1:
                fragmentGraph = new FragmentGraph();
                return fragmentGraph;
            case 2:
                fragmentAwards = new FragmentAwards();
                return fragmentAwards;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        //here you can set your custom title by using position
        switch (position){
            case 0:
                return myContext.getResources().getString(R.string.tab_text_1);
            case 1:
                return myContext.getResources().getString(R.string.tab_text_2);
            case 2:
                return myContext.getResources().getString(R.string.tab_text_3);
            default:
                return "";
        }
    }
}