package com.mediacollector;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;

/**
 * Adapter um innerhalb eines ListView über ein TextView Einträge zu filtern.
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class ResultsAdapter extends ArrayAdapter<TextImageEntry> implements
		Filterable {
	
	private ArrayList<TextImageEntry> mObjects;
	private final Object mLock = new Object();

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
	public ResultsAdapter(Context context, int textViewResourceId,
			ArrayList<TextImageEntry> items) {

		super(context, textViewResourceId, items);
		
		this.context = context;
		this.mObjects = items;
		this.allItems = this.mObjects;
		inflater = LayoutInflater.from(context);
	}

    @Override
    public int getCount() {
        return mObjects.size();
    }
    
    @Override
    public TextImageEntry getItem(int position) {
        return mObjects.get(position);
    }
    
    @Override
    public int getPosition(TextImageEntry item) {
        return mObjects.indexOf(item);
    }
    
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new TextFilter();
		}
		return filter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.entry_child_layout, null);
		}
		TextImageEntry o = mObjects.get(position);
		if (o != null) {
			((TextView) v.findViewById(R.id.name)).setText(o.getText());
			((TextView) v.findViewById(R.id.details)).setText(o.getYear());
			Bitmap cover;
			if (o.getImage() != null)
				cover = BitmapFactory.decodeFile(o.getImage() + "_small.jpg");
			else
				cover = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.no_cover);
			((ImageView) v.findViewById(R.id.image)).setImageBitmap(cover);

		}
		return v;
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
			mObjects = (ArrayList<TextImageEntry>) results.values;

			notifyDataSetChanged();
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {

			FilterResults results = new FilterResults();
			ArrayList<TextImageEntry> i = new ArrayList<TextImageEntry>();
            if (allItems == null) {
                synchronized (mLock) {
                	allItems = new ArrayList<TextImageEntry>(mObjects);
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
					final TextImageEntry value = allItems.get(index);
					final String valueText = value.getText().toLowerCase();

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
