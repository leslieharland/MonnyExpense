package com.example.leslie.monnyfree.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.leslie.monnyfree.R;
import com.example.leslie.monnyfree.database.QueryExpense;
import com.example.leslie.monnyfree.model.DateGroup;
import com.example.leslie.monnyfree.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class CategoryExpandableListAdapter
        extends BaseExpandableListAdapter {
    private Context mContext;
    private int mLayout;

    private LinkedHashMap<String, List<QueryExpense>> input;


    private int mIdGroupChecked = ListView.INVALID_POSITION;
    private int mIdChildChecked = ListView.INVALID_POSITION;

    private ExpandableListView listView;
    private boolean mShowSelector;

    OnChildClickListener childClickListener;
    private final int[] expandedStateSet = {android.R.attr.state_expanded};
    private final int[] emptyStateSet = {};

    public CategoryExpandableListAdapter(Context context, int layout,
                                         LinkedHashMap<String, List<QueryExpense>> input,
                                         boolean showSelector) {
        mContext = context;
        mLayout = layout;
        this.input = input;
        mShowSelector = showSelector;
        childClickListener = (v, groupPosition, childPosition, id) -> false;
    }

    public LinkedHashMap<String, List<QueryExpense>> getList() {
        return input;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = getKey(groupPosition);
        List<QueryExpense> list = input.get(key);
        return list.get(childPosition);
    }

    public String getKey(int keyPosition) {
        int counter = 0;
        Iterator<String> keyIterator = input.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (counter++ == keyPosition) {
                return key;
            }
        }
        // will not be the case ...
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        CategoryListItemViewHolderChild holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (groupPosition == 0 && ((QueryExpense)getChild(groupPosition, 0)).getExpenseId() == -1) {
                convertView = inflater.inflate(R.layout.simple_new_expense, null);
            }else{
                convertView = inflater.inflate(mLayout, null);
            }
            holder = new CategoryListItemViewHolderChild(convertView);
            holder.container.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
            convertView.setTag(holder);
        } else {
            holder = (CategoryListItemViewHolderChild) convertView.getTag();
        }

        QueryExpense entity = (QueryExpense) getChild(groupPosition, childPosition);
        if (entity == null) return convertView;

        if (((QueryExpense)getChild(groupPosition, 0)).getExpenseId() != -1) {
            holder.text1.setText(entity.getDisplayTitle());
            holder.text2.setText(String.valueOf(entity.getAmount()));
            holder.text2.setTextColor(getContext().getResources().getColor(R.color.monny_default));
            holder.imageView.setImageBitmap(BitmapUtil.convertToBitmap(entity.getCategoryIcon()));
            holder.selector.setVisibility(View.GONE);
        }
        // Selector. Always hidden on subcategories.

//		if (mShowSelector) {
//			holder.selector.setVisibility(View.VISIBLE);
//			// set the tag to be the group position
//			holder.selector.setTag(entity.getCategId() + ":" + entity.getSubCategId());
//
//			holder.selector.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					String tag = v.getTag().toString();
//					String[] ids = tag.split(":");
//					Integer groupId = Integer.parseInt(ids[0]);
//					Integer childId = Integer.parseInt(ids[1]);
//					setIdChildChecked(groupId, childId);
//					// close
//					closeFragment();
//				}
//			});
//		} else {

//		}

        return convertView;
    }

    public void setChildClickListener(OnChildClickListener clickListener) {
        this.childClickListener = clickListener;
    }

    public interface OnChildClickListener {
        /**
         * Callback method to be invoked when a child in this expandable list has
         * been clicked.
         *
         * @param v             The view within the expandable list/ListView that was clicked
         * @param groupPosition The group position that contains the child that
         *                      was clicked
         * @param childPosition The child position within the group
         * @param id            The row id of the child that was clicked
         * @return True if the click was handled
         */
        boolean onChildClick(View v, int groupPosition, int childPosition, long id);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key = getKey(groupPosition);
        return input.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return input.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return input.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CategoryListItemViewHolderGroup holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayout, parent, false);


            holder = new CategoryListItemViewHolderGroup(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CategoryListItemViewHolderGroup) convertView.getTag();
        }
        holder.text1.setTextSize(getContext().getResources().getDimension(R.dimen.text_normal_size));
        holder.text1.setTypeface(null, Typeface.BOLD);
        holder.text2.setTextSize(getContext().getResources().getDimension(R.dimen.text_normal_size));
        holder.text2.setTypeface(null, Typeface.BOLD);
        if (groupPosition == 0) {
            holder.text1.setText(R.string.today);
        } else {
            holder.text1.setText(getKey(groupPosition));
        }
        holder.linearLayoutContainer.setBackgroundColor(getContext().getResources().getColor(R.color.default_gray));
        if (isExpanded) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            holder.text1.setTextColor(getContext().getResources().getColor(R.color.monny_default));
            holder.text2.setTextColor(getContext().getResources().getColor(R.color.monny_default));

        } else {
            holder.text1.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
            holder.text2.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        }
        // prevent exceptions. todo: Find out how this happens in the first place.
        if (input.size() == 0) return convertView;


//        //region Selector
//
//        if (mShowSelector) {
//            holder.selector.setVisibility(View.VISIBLE);
//            // set the tag to be the group position
//            holder.selector.setTag(dateGroup.getDateId());
//
//            holder.selector.setOnClickListener(v -> {
//                String tag = v.getTag().toString();
//                Integer groupId = Integer.parseInt(tag);
//                setIdGroupChecked(groupId);
//                // close
//                closeFragment();
//            });
//        } else {
//            holder.selector.setVisibility(View.GONE);
//        }
//
//
        if (holder.collapseImageView != null) {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.group_indicator);
            holder.collapseImageView.setImageDrawable(drawable);

            boolean hasChildren = getChildrenCount(groupPosition) != 0;
            if (!hasChildren) {
                holder.collapseImageView.setVisibility(View.INVISIBLE);
            } else {
                holder.collapseImageView.setVisibility(View.VISIBLE);

                if (drawable != null) {
                    drawable.setState(isExpanded ? expandedStateSet : emptyStateSet);
                }
            }
        }
////endregion
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getIdGroupChecked() {
        return mIdGroupChecked;
    }

    public int getIdChildChecked() {
        return mIdChildChecked;
    }

    public void setIdGroupChecked(int idGroup) {
        // If an existing group is clicked, collapse it. Reset the expanded id.
//        if (mIdGroupChecked == idGroup) {
//            mIdGroupChecked = ListView.INVALID_POSITION;
//        } else {
        mIdGroupChecked = idGroup;
        mIdChildChecked = ExpandableListView.INVALID_POSITION;
//        }
    }

    public void setIdChildChecked(int idGroup, int idChild) {
        mIdGroupChecked = idGroup;
        mIdChildChecked = idChild;
    }

    private void closeFragment() {
        FragmentActivity activity = (FragmentActivity) getContext();
      /*  HomeFragment fragment =
                (HomeFragment) activity
                        .getSupportFragmentManager()
                        .findFragmentByTag(MainActivity.FRAGMENTTAG);
        fragment.setResultAndFinish();*/
    }


    private Context getContext() {
        return mContext;
    }
}