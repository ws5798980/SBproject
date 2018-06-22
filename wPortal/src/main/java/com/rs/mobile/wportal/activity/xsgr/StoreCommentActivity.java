package com.rs.mobile.wportal.activity.xsgr;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rs.mobile.common.activity.BaseActivity;
import com.rs.mobile.wportal.R;
import com.rs.mobile.wportal.adapter.xsgr.ViewPagerAdapter;
import com.rs.mobile.wportal.fragment.xsgr.MyDateDayFragment;
import com.rs.mobile.wportal.fragment.xsgr.MyDateFreeFragment;
import com.rs.mobile.wportal.fragment.xsgr.MyDateMonthFragment;
import com.rs.mobile.wportal.fragment.xsgr.MyDateWeekFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.rs.mobile.wportal.takephoto.CommonUtil.dip2px;

public class StoreCommentActivity extends BaseActivity {


    private List<Fragment> list;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    private CommentFragment commentFragment1, commentFragment2, commentFragment3;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xs_my_data);

        initView();

        setListener();

        initDate();
    }

    private void setListener() {

    }

    private void initDate() {
        list = new ArrayList<>();
        commentFragment1 = new CommentFragment();
        commentFragment2 = new CommentFragment();
        commentFragment3 = new CommentFragment();


        titles = new String[]{getResources().getString(R.string.commentall), getResources().getString(R.string.commentbad), getResources().getString(R.string.commentnosee)};
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]));
        list.add(commentFragment1);
        list.add(commentFragment2);
        list.add(commentFragment3);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, list, titles);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
//        tabLayout.setupWithViewPager(viewPager);
        reflex(tabLayout);
    }


    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

    }


    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}