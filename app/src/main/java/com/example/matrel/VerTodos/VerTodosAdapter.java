package com.example.matrel.VerTodos;

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

public class VerTodosAdapter extends RecyclerView.Adapter<VerTodosAdapter.ViewHolder>{

    private final VerTodosInterface verTodosInterface;
    private Context context;
    private List<DestaquesModel> destaquesModelList;

    public VerTodosAdapter(Context context, List<DestaquesModel> destaquesModelList, VerTodosInterface verTodosInterface) {
        this.context = context;
        this.destaquesModelList = destaquesModelList;
        this.verTodosInterface = verTodosInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_todos_card, parent, false), verTodosInterface);
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
        public ViewHolder(@NonNull View itemView, VerTodosInterface verTodosInterface) {
            super(itemView);

            destImg = itemView.findViewById(R.id.img_todos);
            nome = itemView.findViewById(R.id.nome_todos);
            avaliacoes = itemView.findViewById(R.id.avaliacoes_todos);
            preco = itemView.findViewById(R.id.preco_todos);
            btn = itemView.findViewById(R.id.btnComprarCard2);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(verTodosInterface != null){
                            int position = getAdapterPosition();

                            if (position != RecyclerView.NO_POSITION){
                                verTodosInterface.onItemClick(position);
                            }
                        }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(verTodosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            verTodosInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}