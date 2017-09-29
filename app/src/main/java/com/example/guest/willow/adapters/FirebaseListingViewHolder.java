package com.example.guest.willow.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.guest.willow.Constants;
import com.example.guest.willow.R;
import com.example.guest.willow.models.Listing;
import com.example.guest.willow.ui.ListingDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by wccha on 9/28/2017.
 */

public class FirebaseListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    View mView;
    Context mContext;

    public FirebaseListingViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindListing(Listing listing) {
        TextView line1TextView = (TextView) mView.findViewById(R.id.line1TextView);
        TextView line2TextView = (TextView) mView.findViewById(R.id.line2TextView);
        TextView localityTextView = (TextView) mView.findViewById(R.id.localityTextView);

        line1TextView.setText(listing.getLine1());
        line2TextView.setText(listing.getLine2());
        localityTextView.setText(listing.getLocality());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Listing> listings = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_LISTINGS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listings.add(snapshot.getValue(Listing.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, ListingDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("listings", Parcels.wrap(listings));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
