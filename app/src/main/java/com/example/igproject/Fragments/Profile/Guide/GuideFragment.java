package com.example.igproject.Fragments.Profile.Guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igproject.R;


public class GuideFragment extends Fragment {


    private ViewPager viewPager;

    private Integer size = 28;

    public GuideFragment() {
        // Required empty public constructor
    }

    public static GuideFragment newInstance(){
        return new GuideFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.guide);
        viewPager.setAdapter(new GuidePagesAdapter(getChildFragmentManager()));
    }

    public class GuidePagesAdapter extends FragmentStatePagerAdapter {

        public GuidePagesAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return GuidePageFragment.newInstance(position % GuideFragment.this.size);
        }

        @Override
        public int getCount() {
            return size - 1;
        }
    }
}