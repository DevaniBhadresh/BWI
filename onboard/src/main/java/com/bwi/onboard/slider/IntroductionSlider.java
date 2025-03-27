package com.bwi.onboard.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bwi.onboard.R;
import com.bwi.onboard.SupperSplashActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class IntroductionSlider extends ConstraintLayout {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private Button btnNext;
    private OnSliderChangeListener listener;

    public IntroductionSlider(@NonNull Context context) {
        super(context);
        init(context);
    }

    public IntroductionSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IntroductionSlider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.layout_introduction_slider, this, true);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        btnNext = findViewById(R.id.btnNext);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (listener != null) {
                    listener.onSliderItemChanged(position);
                }

                if (position == (adapter.getItemCount() - 1)) {
                    btnNext.setText(R.string.strGetStarted);
                } else {
                    btnNext.setText(R.string.strNext);
                }
            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < (adapter.getItemCount() - 1)) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                } else {
                    if (context instanceof SupperSplashActivity) {
                        ((SupperSplashActivity) context).completeOnboarding();
                    }
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            Log.d("TAB", "init: " + tab.getText());
            // Optionally customize tabs if needed
        }).attach();
    }

    /**
     * Set the slide layouts to display in the ViewPager.
     *
     * @param slideLayouts List of layouts for each slide
     */
    public void setSlides(List<Integer> slideLayouts) {
        adapter.setSlideLayouts(slideLayouts);
    }

    public void setOnSliderChangeListener(OnSliderChangeListener listener) {
        this.listener = listener;
    }
}
