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

public class ProcuradosAdapter  extends RecyclerView.Adapter<ProcuradosAdapter.ViewHolder>{

    private final ProcuradosInterface procuradosInterface;
    private Context context;
    private List<DestaquesModel> destaquesModelList;

    public ProcuradosAdapter(Context context, List<DestaquesModel> destaquesModelList, ProcuradosInterface procuradosInterface) {
        this.context = context;
        this.destaquesModelList = destaquesModelList;
        this.procuradosInterface = procuradosInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.destaques_card, parent, false), procuradosInterface);
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
        public ViewHolder(@NonNull View itemView, ProcuradosInterface procuradosInterface) {
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
                    if(procuradosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            procuradosInterface.onItemClickProcurado(position, 0);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(procuradosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            procuradosInterface.onItemClickProcurado(position, 0);
                        }
                    }
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(procuradosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            procuradosInterface.onItemClickProcurado(position, 1);
                        }
                    }
                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(procuradosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            procuradosInterface.onItemClickProcurado(position, 2);
                        }
                    }
                }
            });
        }
    }
}
