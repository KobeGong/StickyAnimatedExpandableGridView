package com.baidu.stickyheadergridview.sample;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.stickyheadergridview.R;
import com.baidu.stickyheadergridview.network.ImageLoader;
import com.kobe.library.AnimatedExpandableGridView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolBar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager.setAdapter(new MyPagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class MyPagerAdapter extends PagerAdapter{
        CharSequence[] titles = new CharSequence[]{
                "图片", "图片", "图片", "图片"
        };

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.expandablegridview, container, false);
            final AnimatedExpandableGridView gridView = (AnimatedExpandableGridView) contentView.findViewById(R.id.expandableGridView);
            final MyExpandableListAdapter expandableListAdapter = new MyExpandableListAdapter(container.getContext());
            gridView.setAdapter(expandableListAdapter);
            container.addView(contentView);
            for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
                gridView.expandGroup(i);
            }

            gridView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if (gridView.isGroupExpanded(groupPosition)) {
                        gridView.collapseGroupWithAnimation(groupPosition);
                    } else {
                        gridView.expandGroupWithAnimation(groupPosition);
                    }
                    return true;
                }
            });


            return contentView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        public MyExpandableListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public String getChild(int groupPosition, int childPosition) {
            return DATA.children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return DATA.children[groupPosition].length;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader.load(getChild(groupPosition, childPosition), holder.image);
            return convertView;
        }

        public String getGroup(int groupPosition) {
            return DATA.groups[groupPosition];
        }

        public int getGroupCount() {
            return DATA.groups.length;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public int getChildTypeCount() {
            return 1;
        }

        @Override
        public int getGroupType(int groupPosition) {
            return 0;
        }

        @Override
        public int getGroupTypeCount() {
            return 1;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            GroupHolder holder;
            if(convertView == null){
                holder = new GroupHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.header, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.text1);
                convertView.setTag(holder);
            }else{
                holder = (GroupHolder) convertView.getTag();
            }
            holder.text.setText(getGroup(groupPosition));
            return convertView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }

    class ViewHolder{
        ImageView image;
        TextView text;
    }
    class GroupHolder{
        TextView text;
    }
}
