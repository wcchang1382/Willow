package com.example.guest.willow.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.willow.Constants;
import com.example.guest.willow.R;
import com.example.guest.willow.models.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListingDetailFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.line1TextView) TextView mLine1Label;
    @Bind(R.id.line2TextView) TextView mLine2Label;
    @Bind(R.id.localityTextView) TextView mLocalityLabel;
    @Bind(R.id.saveListingButton) Button mSaveListingButton;

    private Listing mListing;
    private ArrayList<Listing> mListings;
    private int mPosition;

    public static ListingDetailFragment newInstance(ArrayList<Listing> listings, Integer position) {
        ListingDetailFragment listingDetailFragment = new ListingDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_KEY_LISTINGS, Parcels.wrap(listings));
        args.putInt(Constants.EXTRA_KEY_POSITION, position);
        listingDetailFragment.setArguments(args);
        return listingDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListings = Parcels.unwrap(getArguments().getParcelable(Constants.EXTRA_KEY_LISTINGS));
        mPosition = getArguments().getInt(Constants.EXTRA_KEY_POSITION);
        mListing = mListings.get(mPosition);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_detail, container, false);
        ButterKnife.bind(this, view);

        mLine1Label.setText(mListing.getLine1());
        mLine2Label.setText(mListing.getLine2());
        mLocalityLabel.setText(mListing.getLocality());

        mLine1Label.setOnClickListener(this);

        mSaveListingButton.setOnClickListener(this);

        return view;
//        return inflater.inflate(R.layout.fragment_listing_detail, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v == mLine1Label) {
            Intent line1Intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mListing.getLine1()));
            startActivity(line1Intent);
        }

        if (v == mSaveListingButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference listingRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_LISTINGS)
                    .child(uid);

            DatabaseReference pushRef = listingRef.push();
            String pushId = pushRef.getKey();
            mListing.setPushId(pushId);
            pushRef.setValue(mListing);
//            listingRef.push().setValue(mListing);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
