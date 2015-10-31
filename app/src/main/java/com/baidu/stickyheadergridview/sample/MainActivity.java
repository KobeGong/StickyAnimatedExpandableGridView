package com.baidu.stickyheadergridview.sample;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.stickyheadergridview.AnimatedExpandableListView;
import com.baidu.stickyheadergridview.AnimatedExpandableGridView;
import com.baidu.stickyheadergridview.R;
import com.baidu.stickyheadergridview.StickyGridHeadersGridView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        StickyGridHeadersGridView.OnHeaderClickListener, StickyGridHeadersGridView.OnHeaderLongClickListener {


    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager.setAdapter(new MyPagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onHeaderClick(AdapterView<?> parent, View view, long id) {

    }

    @Override
    public boolean onHeaderLongClick(AdapterView<?> parent, View view, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class MyPagerAdapter extends PagerAdapter{
        CharSequence[] titles = new CharSequence[]{
                "缓存", "图片", "视频", "语音"
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
//            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.header_gridview, container, false);
//            GridView mGridView = (GridView) contentView.findViewById(R.id.asset_grid);
//            mGridView.setOnItemClickListener(MainActivity.this);
//            mGridView.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(
//                    getApplicationContext(), getResources().getStringArray(R.array.countries),
//                    R.layout.header, R.layout.item));
//            ((StickyGridHeadersGridView) mGridView).setOnHeaderClickListener(MainActivity.this);
//            ((StickyGridHeadersGridView) mGridView).setOnHeaderLongClickListener(MainActivity.this);
//            mGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
//            container.addView(contentView);

            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.expandablegridview, container, false);
            final AnimatedExpandableGridView gridView = (AnimatedExpandableGridView) contentView.findViewById(R.id.expandableGridView);
            final MyExpandableListAdapter expandableListAdapter = new MyExpandableListAdapter();
            gridView.setAdapter(expandableListAdapter);
            container.addView(contentView);
            for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
                gridView.expandGroup(i);
            }
//            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.animatablelistview, container, false);
//            final AnimatedExpandableListView listView = (AnimatedExpandableListView) contentView.findViewById(R.id.listview);
//            final MyAniamtableListAdapter expandableListAdapter = new MyAniamtableListAdapter();
//            listView.setAdapter(expandableListAdapter);
//            container.addView(contentView);
//            for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
//                listView.expandGroup(i);
//            }

            gridView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if (gridView.isGroupExpanded(groupPosition)) {
                        gridView.collapseGroupWithAnimation(groupPosition);
                    } else {
                        gridView.expandGroupWithAnimation(groupPosition);
                    }
                    expandableListAdapter.notifyDataSetChanged();
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
        // Sample data set.  children[i] contains the children (String[]) for groups[i].
        private String[] groups = { "People Names", "Dog Names1", "Cat Names","People Names", "Dog Names", "Cat Names","People Names", "Dog Names", "Cat Names", "Fish Names" };
        private String[][] children = {
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Goldy", "Bubbles" }
        };

        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }



        public ImageView getGenericChildView(){
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (90* Resources.getSystem().getDisplayMetrics().density));
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.pic);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }


        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(R.id.id1, holder);
            }else{
                holder = (ViewHolder) convertView.getTag(R.id.id1);
            }
            holder.text.setText(String.valueOf(childPosition));
            Log.d("gonggaofeng","childPosition="+childPosition);




            return convertView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            TextView textView = getGenericTextView();
            textView.setBackgroundColor(Color.GRAY);
            textView.setText(getGroup(groupPosition).toString() + groupPosition);
            return textView;
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

    public TextView getGenericTextView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(MainActivity.this);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 16, 0, 16);
        return textView;
    }


    class MyAniamtableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{

        private String[] groups = { "People Names", "Dog Names", "Cat Names","People Names", "Dog Names", "Cat Names","People Names", "Dog Names", "Cat Names", "Fish Names" };
        private String[][] children = {
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Goldy", "Bubbles" }
        };


        private int mNumColumns = 3;

        public View getCellView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
            ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(R.id.id1, holder);
            }else{
                holder = (ViewHolder) convertView.getTag(R.id.id1);
            }
            holder.text.setText(String.valueOf(childPosition));
            Log.d("gonggaofeng","childPosition="+childPosition);

            return convertView;
        }


        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LinearLayout row = null;
            if(convertView != null && convertView instanceof LinearLayout){
                row = (LinearLayout)convertView;
            }else{
                row = new LinearLayout(parent.getContext());
            }
            if (row.getLayoutParams() == null) {
                row.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.ITEM_VIEW_TYPE_IGNORE));
                row.setPadding(0, 10, 0, 10);
                row.setGravity(Gravity.CENTER_HORIZONTAL);
            }

            int groupChildrenCount = getCellCount(groupPosition);

            int index = 0;
            for (int i = mNumColumns * childPosition; i < (mNumColumns * (childPosition + 1)); i++, index++) {
                View child;

                View cachedChild = index < row.getChildCount() ? row.getChildAt(index) : null;

                if (i < groupChildrenCount) {
                    if (cachedChild != null && cachedChild.getTag() == null) {
                        ((ViewGroup) cachedChild.getParent()).removeView(cachedChild);
                        cachedChild = null;
                    }
                    Log.d("gonggaofeng", "i="+i);
                    child = getCellView(groupPosition, i, i == (groupChildrenCount - 1), cachedChild, parent);
                    child.setTag(getChild(groupPosition, i));
                } else {
                    if (cachedChild != null && cachedChild.getTag() != null) {
                        ((ViewGroup) cachedChild.getParent()).removeView(cachedChild);
                        cachedChild = null;
                    }

                    child = new View(parent.getContext());
                    child.setTag(null);
                }

                if (!(child.getLayoutParams() instanceof LinearLayout.LayoutParams)) {
                    LinearLayout.LayoutParams params;
                    if (child.getLayoutParams() == null) {
                        params = new LinearLayout.LayoutParams(240, AbsListView.LayoutParams.WRAP_CONTENT, 1);
                    } else {
                        params = new LinearLayout.LayoutParams(240, child.getLayoutParams().height, 1);
                    }

                    child.setLayoutParams(params);
                }

                child.setPadding(10, 0, 10, 0);

                if (index == row.getChildCount()) {
                    row.addView(child, index);
                } else {
                    child.invalidate();
                }
            }

            return row;
        }


        public int getCellCount(int groupPosition){
            return children[groupPosition].length;
        }
        @Override
        public int getRealChildrenCount(int groupPosition) {

            int realCount = getCellCount(groupPosition);

            int count = realCount;
            if (mNumColumns > 0) {
                int line = realCount / mNumColumns;
                line = realCount % mNumColumns > 0 ? line + 1 : line;
                return line;
            }

            return count;
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = getGenericTextView();
            textView.setBackgroundColor(Color.GRAY);
            textView.setText(getGroup(groupPosition).toString() + groupPosition);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
