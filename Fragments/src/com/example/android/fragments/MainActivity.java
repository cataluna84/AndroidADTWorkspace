/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

// A fragment is a kind of sub-activity that you can add or
// remove from other activities while the activity continues
// to run. 

// We extend the FragmentActivity to be able to use Fragments
// on platforms prior to Android 3.0

public class MainActivity extends FragmentActivity 
        implements HeadlinesFragment.OnHeadlineSelectedListener {

    // Called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of the Fragment that holds the titles
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            // beginTransaction() is used to begin any edits of Fragments
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    // Required if the OnHeadlineSelectedListener interface is implemented
    // This method is called when a headline is clicked on
    public void onArticleSelected(int position) {
        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        // If the article fragment is here we're in the two pane layout
        if (articleFrag != null) {
            // Get the ArticleFragment to update itself
            articleFrag.updateArticleView(position);
        } else {
            // If the fragment is not available, use the one pane layout and 
        	// swap between the article and headline fragments
        	
            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            
            // The Bundle contains information passed between activities
            Bundle args = new Bundle();
            
            // Save the current article value
            args.putInt(ArticleFragment.ARG_POSITION, position);
            
            // Add the article value to the new Fragment
            newFragment.setArguments(args);
            
            // The FragmentTransaction adds, removes, replaces and defines animations for Fragments
            // The FragmentManager provides methods for interacting
            // beginTransaction() is used to begin any edits of Fragments
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            
            // addToBackStack() causes the transaction to be remembered. 
            // It will reverse this operation when it is later popped off the stack.
            transaction.addToBackStack(null);
            
            // Schedules for the addition of the Fragment to occur
            transaction.commit();
        }
    }
}