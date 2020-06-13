package com.example.magazinarticolesportive.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinarticolesportive.Interface.ItemClickListener;
import com.example.magazinarticolesportive.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtPrice, txtQuantity, txtTotalProductPrice;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.product_name_cart);
        txtPrice = itemView.findViewById(R.id.product_price_cart);
        txtQuantity = itemView.findViewById(R.id.product_quantity_cart);
        txtTotalProductPrice = itemView.findViewById(R.id.product_total_price_cart);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
