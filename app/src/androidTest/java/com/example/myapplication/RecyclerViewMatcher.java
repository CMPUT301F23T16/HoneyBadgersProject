package com.example.myapplication;

import android.content.res.Resources;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * A custom matcher for Espresso that facilitates testing of specific views within
 * items in a RecyclerView. This matcher is useful for locating and asserting
 * views at a specific position in a RecyclerView.
 */
public class RecyclerViewMatcher {

    private final int recyclerViewId;
    private final int position;

    public RecyclerViewMatcher(int recyclerViewId, int position) {
        this.recyclerViewId = recyclerViewId;
        this.position = position;
    }

    public Matcher<View> atPositionOnView(final int targetViewId, int item_name) {
        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            @Override
            public void describeTo(Description description) {
                String idDescription = Integer.toString(recyclerViewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource name not found)", recyclerViewId);
                    }
                }

                description.appendText("with id: " + idDescription);
            }

            @Override
            public boolean matchesSafely(View view) {
                this.resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView =
                            view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                        RecyclerView.ViewHolder viewHolder =
                                recyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder != null) {
                            childView = viewHolder.itemView;
                        }
                    } else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }
            }
        };
    }
}