package com.example.nexustutor.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.Fragment.AddOfferFragment;
import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import java.util.List;

public class OfferRecyclerViewAdapter extends RecyclerView.Adapter<OfferRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Offer> mData;

    public OfferRecyclerViewAdapter(Context mContext, List<Offer> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       /* View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_offer,parent,false);
        return new MyViewHolder(view);*/
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*holder.etTitle.setText(mData.get(position).getTitle());
        holder.etDescription.setText(mData.get(position).getDescription());
        holder.etSubject.setText(mData.get(position).getSubject());
        holder.etLocation.setText(mData.get(position).getLocationCity() + ", " + mData.get(position).getLocationState());
        holder.etGender.setText(mData.get(position).getGender());
        holder.img_view_profile.setImageResource(mData.get(position).getThumbnail());
*/      holder.cardViewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
       public TextView etTitle, etDescription, etSubject, etLocation, etLocationCity, etLocationState, etGender;
       public ImageView img_view_profile;
       public CardView cardViewOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            etTitle = (TextView) itemView.findViewById(R.id.tv_title);
            etDescription= (TextView)itemView.findViewById(R.id.tv_description);
            etSubject= (TextView)itemView.findViewById(R.id.tv_subject_preferences);
            etLocation= (TextView)itemView.findViewById(R.id.tv_location);
            etGender =(TextView) itemView.findViewById(R.id.tv_gender);
            img_view_profile = (ImageView) itemView.findViewById(R.id.img_tutor);
            cardViewOffer = (CardView) itemView.findViewById(R.id.cardview_offer);
        }
    }
}
