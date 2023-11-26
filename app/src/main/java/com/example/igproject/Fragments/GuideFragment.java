package com.example.igproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igproject.Fragments.Pag1Fragment;
import com.example.igproject.Fragments.Pag2Fragment;
import com.example.igproject.Fragments.Pag3Fragment;
import com.example.igproject.Fragments.Pag4Fragment;
import com.example.igproject.Fragments.Pag5Fragment;
import com.example.igproject.Fragments.ProfileFragment;
import com.example.igproject.R;


public class GuideFragment extends Fragment {


    ViewPager viewPager;

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

    public static class GuidePagesAdapter extends FragmentStatePagerAdapter {

        public GuidePagesAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = new Pag1Fragment();
                    break;
                case 1:
                    fragment = new Pag2Fragment();
                    break;
                case 2:
                    fragment = new Pag3Fragment();
                    break;
                case 3:
                    fragment = new Pag4Fragment();
                    break;
                case 4:
                    fragment = new Pag5Fragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}