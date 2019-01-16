package com.craftery.lovro.myapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Offer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private List<Offer> offers;
    private OnShowClickListener onShowClickListener;


    public OfferAdapter(List<Offer> offers){
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_offers,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ViewHolder holder, int position) {
        final Offer offer = offers.get(position);
        final ImageView offerImage = holder.itemView.findViewById(R.id.offer_picture);
        TextView offerName = holder.itemView.findViewById(R.id.offer_name);
        TextView offerPrice = holder.itemView.findViewById(R.id.offer_price);
        TextView more_styles = holder.itemView.findViewById(R.id.more_styles);

        Picasso.get().load(offer.getImageUrl()).into(offerImage);
        //offerImage.setImageResource(R.drawable.placeholder);
        offerName.setText(offer.getName());
        offerPrice.setText(offer.getPrice().toString());

        if(offer.getStyles().size() == 0){
            more_styles.setText("");
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowClickListener.onShowClick(offer);
            }
        };

        holder.itemView.setOnClickListener(listener);
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }
    }

    public void setListener(OnShowClickListener listener){
        this.onShowClickListener = listener;
    }


    public interface OnShowClickListener{
        void onShowClick(Offer offer);
    }
}
