package com.example.matrel.Destaques;

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
import com.example.matrel.R;

import java.util.List;

public class DestaquesAdapter  extends RecyclerView.Adapter<DestaquesAdapter.ViewHolder>{

    private final DestaquesInterface destaquesInterface;
    private Context context;
    private List<DestaquesModel> destaquesModelList;

    public DestaquesAdapter(Context context, List<DestaquesModel> destaquesModelList, DestaquesInterface destaquesInterface) {
        this.context = context;
        this.destaquesModelList = destaquesModelList;
        this.destaquesInterface = destaquesInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.destaques_card, parent, false), destaquesInterface);
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
        ImageView destImg, fav, cart;
        TextView nome, avaliacoes, preco;
        Button btn;
        public ViewHolder(@NonNull View itemView, DestaquesInterface destaquesInterface) {
            super(itemView);

            destImg = itemView.findViewById(R.id.destaques_img);
            nome = itemView.findViewById(R.id.destaques_text);
            avaliacoes = itemView.findViewById(R.id.avaliacoes);
            preco = itemView.findViewById(R.id.preco_rec);
            btn = itemView.findViewById(R.id.btnComprarCard1);
            fav = itemView.findViewById(R.id.fav_dest);
            cart = itemView.findViewById(R.id.dest_car);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(destaquesInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            destaquesInterface.onItemClick(position, 0);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(destaquesInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            destaquesInterface.onItemClick(position, 0);
                        }
                    }
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(destaquesInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            destaquesInterface.onItemClick(position, 1);
                        }
                    }
                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(destaquesInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            destaquesInterface.onItemClick(position, 2);
                        }
                    }
                }
            });
        }
    }
}
