package com.pizza4u.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pizza4u.activities.CusPizzaListActivity;
import com.pizza4u.activities.CusPizzaViewActivity;
import com.pizza4u.R;
import com.pizza4u.models.PizzaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PizzasRecycleAdapter extends RecyclerView.Adapter<PizzasRecycleAdapter.PizzasViewHolder>{

    Context mContext;
    private final List<PizzaModel> pizzaModelList;
    String userEmail;

    public PizzasRecycleAdapter(CusPizzaListActivity mContext, List<PizzaModel> pizzaModelList,String userEmail) {
        this.mContext=mContext;
        this.pizzaModelList=pizzaModelList;
        this.userEmail=userEmail;
    }


    @NonNull
    @Override
    public PizzasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_cus_pizzas,viewGroup,false);
        return new PizzasRecycleAdapter.PizzasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzasViewHolder pholder, @SuppressLint("RecyclerView") int position) {

        pholder.txtName.setText(pizzaModelList.get(position).getName());
        pholder.txtDescription.setText(pizzaModelList.get(position).getDescription());
        Picasso.get().load(pizzaModelList.get(position).getPhoto_url()).into(pholder.img);

        pholder.pLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CusPizzaViewActivity.class);
            intent.putExtra("name",pizzaModelList.get(position).getName());
            intent.putExtra("description",pizzaModelList.get(position).getDescription());
            intent.putExtra("price",pizzaModelList.get(position).getPrice().toString());
            intent.putExtra("photo",pizzaModelList.get(position).getPhoto_url());
            intent.putExtra("userEmail",userEmail);

            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return pizzaModelList.size();
    }

    public static class PizzasViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtDescription;
        public ImageView img;
        ConstraintLayout pLayout;

        public PizzasViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtPizza);
            img = itemView.findViewById(R.id.imagePizza);
            txtDescription = itemView.findViewById(R.id.txtPizzaDescription);
            pLayout = itemView.findViewById(R.id.pizzaLayout);
        }
    }
}
