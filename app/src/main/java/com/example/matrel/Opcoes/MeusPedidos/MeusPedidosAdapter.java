package com.example.matrel.Opcoes.MeusPedidos;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matrel.Carrinho.CarrinhoModel;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MeusPedidosAdapter extends RecyclerView.Adapter<MeusPedidosAdapter.ViewHolder>{

    private MeusPedidosInterface meusPedidosInterface;
    private Context context;
    private List<MeusPedidosModel> meusPedidosModelList;
    Integer quant;
    Float precoAntigo;

    public MeusPedidosAdapter(Context context, List<MeusPedidosModel> meusPedidosModelList, MeusPedidosInterface meusPedidosInterface) {
        this.context = context;
        this.meusPedidosModelList = meusPedidosModelList;
        this.meusPedidosInterface = meusPedidosInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meuspedidos_card, parent, false), meusPedidosInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(meusPedidosModelList.get(position).getImg_url()).into(holder.cartImg);
        holder.nome.setText(meusPedidosModelList.get(position).getNome());
        holder.quantidade.setText(""+meusPedidosModelList.get(position).getQuantidade());
        quant = meusPedidosModelList.get(position).getQuantidade();
        precoAntigo = meusPedidosModelList.get(position).getPreco() / quant;
        holder.preco.setText("R$ " + precoAntigo * quant);

    }


    @Override
    public int getItemCount() {
        return meusPedidosModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cartImg;
        TextView nome, preco, quantidade;

        public ViewHolder(@NonNull View itemView, MeusPedidosInterface meusPedidosInterface) {
            super(itemView);

            cartImg = itemView.findViewById(R.id.img_carrinho);
            nome = itemView.findViewById(R.id.nome_carrinho);
            preco = itemView.findViewById(R.id.preco_carrinho);
            quantidade = itemView.findViewById(R.id.tv_quant);

        }
    }

    }
