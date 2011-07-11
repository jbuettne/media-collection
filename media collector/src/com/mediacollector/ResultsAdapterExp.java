package com.mediacollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mediacollector.collection.TextImageEntry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ResultsAdapterExp extends SimpleExpandableListAdapter implements
		Filterable {	

	private final 	String 				ID	 		= "id";
	private final 	String 				TEXT	 	= "name";
	private final 	String 				IMAGE	 	= "image";
	private final 	String 				YEAR	 	= "year";
	private final 	String 				TRACKCOUNT	= "trackcount";	
	
	private ArrayList<HashMap<String, String>> groupData;
	private ArrayList<HashMap<String, String>> groupDataAll;
	private ArrayList<ArrayList<HashMap<String, Object>>> childData;
	private final Object mLock = new Object();
    private String[] mGroupFrom;
    private int[] mGroupTo;;
    private String[] mChildFrom;
    private int[] mChildTo;

	public ArrayList<TextImageEntry> allItems;
	private Context context;
	private LayoutInflater inflater;
	private TextFilter filter;
	
    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
	public ResultsAdapterExp(Context context,
			ArrayList<HashMap<String, String>> groupData, int groupLayout,
            String[] groupFrom, int[] groupTo,
            ArrayList<ArrayList<HashMap<String, Object>>> childData,
            int childLayout, String[] childFrom, int[] childTo) {
		
        super(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childData,
                childLayout, childLayout, childFrom, childTo);
        
        this.context = context;
        this.groupData = groupData;
        this.groupDataAll = this.groupData;
        this.childData = childData;
        this.mChildFrom = childFrom;
        this.mChildTo = childTo;
        
		inflater = LayoutInflater.from(context);
    }
	
	@Override
    public int getGroupCount() {
        return groupData.size();
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
	public Filter getFilter() {
		if (filter == null) {
			filter = new TextFilter();
		}
		return filter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, int childPosition, 
			boolean isLastChild, View convertView, ViewGroup parent) {
		final View v = super.getChildView(groupPosition, childPosition, 
				isLastChild, convertView, parent);
		Bitmap cover;
		if ((String) ((Map<String, Object>) 
				getChild(groupPosition, childPosition))
				.get(IMAGE) == null)
			cover = BitmapFactory.decodeResource(
					context.getResources(),R.drawable.no_cover);
		else cover = BitmapFactory.decodeFile(
				(String) ((Map<String, Object>) 
						getChild(groupPosition, childPosition))
						.get(IMAGE) + "_small.jpg");
		((ImageView) v.findViewById(R.id.image)).setImageBitmap(cover);
		((TextView) v.findViewById(R.id.name)).setText( 
				(String) ((Map<String, Object>) 
						getChild(groupPosition, childPosition))
						.get(TEXT));
		((TextView) v.findViewById(R.id.details)).setText( 
				(String) ((Map<String, Object>) 
						getChild(groupPosition, childPosition))
						.get(YEAR));
        bindView(v, childData.get(groupPosition).get(childPosition), mChildFrom, mChildTo);
		return v;
	}        	
	public View newChildView(boolean isLastChild, ViewGroup parent) {
		return inflater.inflate(R.layout.entry_child_layout, 
				null, false);
	}
    private void bindView(View view, Map<String, Object> data, String[] from, int[] to) {
        int len = to.length;

        for (int i = 0; i < len; i++) {
            TextView v = (TextView)view.findViewById(to[i]);
            if (v != null) {
                v.setText((String)data.get(from[i]));
            }
        }
    }
    /**
     * <p>An array filters constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
	private class TextFilter extends Filter {

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence prefix, FilterResults results) {
			groupData = (ArrayList<HashMap<String, String>>) results.values;

			notifyDataSetChanged();
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {

			FilterResults results = new FilterResults();
			ArrayList<HashMap<String, String>> i = new ArrayList<HashMap<String, String>>();
            if (groupDataAll == null) {
                synchronized (mLock) {
                	groupDataAll = new ArrayList<HashMap<String, String>>(groupData);
                }
            }

			
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					results.values = allItems;
					results.count = allItems.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				for (int index = 0; index < allItems.size(); index++) {
					final HashMap<String, String> value = groupDataAll.get(index);
					final String valueText = value.get(TEXT).toLowerCase();

					if (valueText.contains(prefixString)) {
						i.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						for (int k = 0; k < wordCount; k++) {
							if (words[k].contains(prefixString)) {
								i.add(value);
								break;
							}
						}
					}
				}
				results.values = i;
				results.count = i.size();
			}
			return results;
		}
	}
}
