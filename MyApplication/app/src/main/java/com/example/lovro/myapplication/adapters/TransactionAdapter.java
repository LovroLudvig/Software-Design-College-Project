package com.example.lovro.myapplication.adapters;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Colors;
import com.example.lovro.myapplication.domain.Comment;
import com.example.lovro.myapplication.domain.Transaction;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions){
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_transaction,parent,false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder viewHolder, int i) {
        Transaction transaction = transactions.get(i);

        String[] credit = transaction.getOrder().getCardNumber().split(" ");

        TextView address = viewHolder.itemView.findViewById(R.id.order_adress);
        TextView town = viewHolder.itemView.findViewById(R.id.order_town);
        TextView postal = viewHolder.itemView.findViewById(R.id.order_postal);
        TextView article_name = viewHolder.itemView.findViewById(R.id.order_article_name);
        TextView article_style = viewHolder.itemView.findViewById(R.id.order_article_style);
        TextView article_dimension = viewHolder.itemView.findViewById(R.id.order_article_dimension);
        ImageView article_image = viewHolder.itemView.findViewById(R.id.article_image);
        TextView price = viewHolder.itemView.findViewById(R.id.priceeeee);
        TextView credit_type = viewHolder.itemView.findViewById(R.id.credit_type);
        TextView hash = viewHolder.itemView.findViewById(R.id.hash_order);
        TextView name = viewHolder.itemView.findViewById(R.id.customerName);
        TextView username = viewHolder.itemView.findViewById(R.id.customerUsername);

        String token = transaction.getOrder().getId().toString();
        String encoded_token = Base64.encodeToString(token.getBytes(),0);
        String encode = Base64.encodeToString(encoded_token.getBytes(),0);
        hash.setText(encode.trim());


        name.setText(transaction.getOrder().getName());
        address.setText(transaction.getOrder().getAddress());
        town.setText(transaction.getOrder().getTown().getName());
        postal.setText(String.valueOf(transaction.getOrder().getTown().getPostCode()));
        article_name.setText(transaction.getOrder().getOffer().getName());
        article_style.setText(transaction.getOrder().getStyle().getDescription());
        article_dimension.setText(transaction.getOrder().getDimension().getDescription());
        price.setText(transaction.getOrder().getPrice().toString());
        credit_type.setText(credit[0]);
        Picasso.get().load(transaction.getOrder().getOffer().getImageUrl()).into(article_image);
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }
    }

}
