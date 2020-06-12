package com.example.magazinarticolesportive.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinarticolesportive.Interface.ItemClickListener;
import com.example.magazinarticolesportive.R;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtSport, txtCategory, txtPrice;
    private ItemClickListener itemClickListener;

    public WishViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.product_name_wish);
        txtPrice = itemView.findViewById(R.id.product_price_wish);
        txtSport = itemView.findViewById(R.id.product_sport_wish);
        txtCategory = itemView.findViewById(R.id.product_category_wish);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
