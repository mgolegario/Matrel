package com.example.matrel.Favoritos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matrel.Destaques.DestaquesModel;
import com.example.matrel.R;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder>{

    private final FavoritosInterface favoritosInterface;
    private Context context;
    private List<DestaquesModel> destaquesModelList;

    public FavoritosAdapter(Context context, List<DestaquesModel> destaquesModelList, FavoritosInterface favoritosInterface) {
        this.context = context;
        this.destaquesModelList = destaquesModelList;
        this.favoritosInterface = favoritosInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favoritos_card, parent, false), favoritosInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(destaquesModelList.get(position).getImg_url()).into(holder.destImg);
        holder.nome.setText(destaquesModelList.get(position).getNome());
        holder.avaliacoes.setText("("+destaquesModelList.get(position).getAvaliacoes()+")");
        holder.preco.setText("R$ " + destaquesModelList.get(position).getPreco());
    }

    @Override
    public int getItemCount() {
        return destaquesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView destImg;
        TextView nome, avaliacoes, preco;
        Button btn;
        ImageView delete, cart;
        public ViewHolder(@NonNull View itemView, FavoritosInterface favoritosInterface) {
            super(itemView);

            destImg = itemView.findViewById(R.id.img_todos);
            nome = itemView.findViewById(R.id.nome_todos);
            avaliacoes = itemView.findViewById(R.id.avaliacoes_todos);
            preco = itemView.findViewById(R.id.preco_todos);
            btn = itemView.findViewById(R.id.btnComprarCard2);
            delete = itemView.findViewById(R.id.del_fav);
            cart = itemView.findViewById(R.id.carrinho_fav);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoritosInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            favoritosInterface.onItemClick(position, 0);
                        }
                    }
                }
            });

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoritosInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            favoritosInterface.onItemClick(position, 1);
                        }
                    }
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(favoritosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            favoritosInterface.onItemClick(position, 2);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoritosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            favoritosInterface.onItemClick(position, 2);
                        }
                    }
                }
            });
        }
    }
}