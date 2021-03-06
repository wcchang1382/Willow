package com.example.guest.willow.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.willow.Constants;
import com.example.guest.willow.R;
import com.example.guest.willow.models.Listing;
import com.example.guest.willow.ui.ListingDetailActivity;
import com.example.guest.willow.ui.ListingDetailFragment;
import com.example.guest.willow.util.OnListingSelectedListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListingRecordAdapter extends RecyclerView.Adapter<ListingRecordAdapter.ListingViewHolder> {
    private ArrayList<Listing> mListings = new ArrayList<>();
    private Context mContext;
    private OnListingSelectedListener mOnListingSelectedListener;

    public ListingRecordAdapter(Context context, ArrayList<Listing> listings, OnListingSelectedListener listingSelectedListener) {
        mContext = context;
        mListings = listings;
        mOnListingSelectedListener = listingSelectedListener;
    }

    @Override
    public ListingRecordAdapter.ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_property_item, parent, false);
        ListingViewHolder viewHolder = new ListingViewHolder(view, mListings, mOnListingSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListingRecordAdapter.ListingViewHolder holder, int position) {
        holder.bindListing(mListings.get(position));
    }

    @Override
    public int getItemCount() {
        return mListings.size();
    }

    public class ListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.listingAddress1TextView) TextView mAddress1TextView;
        @Bind(R.id.listingAddress2TextView) TextView mAddress2TextView;
        @Bind(R.id.listingLocalityTextView) TextView mLocalityTextView;
        @Bind(R.id.listingPostal1TextView) TextView mPostal1TextView;

        private Context mContext;
        private int mOrientation;
        private ArrayList<Listing> mListings = new ArrayList<>();
        private OnListingSelectedListener mListingSelectedListener;

        public ListingViewHolder(View itemView, ArrayList<Listing> listings, OnListingSelectedListener listingSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            mOrientation = itemView.getResources().getConfiguration().orientation;
            mListings = listings;
            mListingSelectedListener = listingSelectedListener;

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(0);
            }

            itemView.setOnClickListener(this);
        }

        public void bindListing(Listing listing) {
            mAddress1TextView.setText(listing.getLine1());
            mAddress2TextView.setText(listing.getLine2());
            mPostal1TextView.setText(listing.getPostal1() + "");
            mLocalityTextView.setText(listing.getLocality());
        }

        private void createDetailFragment(int position) {
            ListingDetailFragment detailFragment = ListingDetailFragment.newInstance(mListings, position);
            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.listingDetailContainer, detailFragment);
            ft.commit();
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            mListingSelectedListener.onListingSelected(itemPosition, mListings);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(itemPosition);
            } else {
                Intent intent = new Intent(mContext, ListingDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_LISTINGS, Parcels.wrap(mListings));

                mContext.startActivity(intent);
            }
        }
    }
}
