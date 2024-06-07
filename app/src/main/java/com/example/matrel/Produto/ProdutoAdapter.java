package com.example.matrel.Produto;

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
import com.example.matrel.Departamentos.DepartamentosInterface;
import com.example.matrel.Departamentos.DepartamentosModel;
import com.example.matrel.R;

import java.util.HashMap;
import java.util.List;

public class ProdutoAdapter  extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder>{

    private ProdutoInterface produtoInterface;
    private Context context;
    private List<ProdutoModel> produtoModelList;

    public ProdutoAdapter(Context context, List<ProdutoModel> produtoModelList, ProdutoInterface produtoInterface) {
        this.context = context;
        this.produtoModelList = produtoModelList;
        this.produtoInterface = produtoInterface;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_model, parent, false), produtoInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(produtoModelList.get(position).getImg_url()).into(holder.prodImg);
        holder.nome.setText(produtoModelList.get(position).getNome());
        holder.descricao.setText(produtoModelList.get(position).getDescricao());
        holder.preco.setText("R$ " + produtoModelList.get(position).getPreco());
        holder.avaliacoes.setText("("+produtoModelList.get(position).getAvaliacoes()+")");
    }

    @Override
    public int getItemCount() {
        return produtoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView prodImg;
        TextView nome, descricao, preco, avaliacoes;
        Button btn;
        public ViewHolder(@NonNull View itemView, ProdutoInterface produtoInterface) {
            super(itemView);

            prodImg = itemView.findViewById(R.id.imgProd);
            nome = itemView.findViewById(R.id.nomeProd);
            descricao = itemView.findViewById(R.id.descProd);
            preco = itemView.findViewById(R.id.precoProd);
            avaliacoes = itemView.findViewById(R.id.avalProd);
            btn = itemView.findViewById(R.id.btnComprar);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(produtoInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            produtoInterface.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}