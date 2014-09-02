package com.example.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// A ListFragment is used to display a list of items
public class HeadlinesFragment extends ListFragment {
	
	// Will monitor if a headline is clicked on
    OnHeadlineSelectedListener mCallback;

    // The container Activity must implement this interface so the 
    // fragment can deliver messages
    public interface OnHeadlineSelectedListener {
        // This function is called when a list item is selected
        public void onArticleSelected(int position);
    }

    // Initializes the Fragment
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // reference to an built-in XML layout document that is part of the Android OS
        int layout = android.R.layout.simple_list_item_1;
        
        // A ListAdapter populates the ListView with data in ipsum arrays
        // An ArrayAdapter specifically deals with arrays
        // getActivity() gets an Intent to start a new activity
        // layout is the list items layout
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }
    
    // Called when the Fragment is visible on the screen
    @Override
    public void onStart() {
        super.onStart();

        // If we have both the article names and the article Fragments on the screen
        // at the same time we highlight the selected article
        
        // The getFragmentManager() returns the FragmentManager which allows us
        // to interact with Fragments associated with the current activity
        
        // getListView() gets a ListView 
        //  CHOICE_MODE_SINGLE allows up to one item to be in a chosen state in the list
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    // Called when a Fragment is attached to an Activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}