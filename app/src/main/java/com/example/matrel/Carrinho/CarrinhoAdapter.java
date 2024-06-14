package com.example.matrel.Carrinho;

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

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>{

    private FirebaseFirestore db;
    private String nomeProd;
    private Float total;
    private Integer quantidade_num;
    private Float precoNovo, tudo;
    private Float precoAntigo;
    private FirebaseAuth auth;
    private Context context;
    private List<CarrinhoModel> carrinhoModelList;

    public CarrinhoAdapter(Context context, List<CarrinhoModel> carrinhoModelList) {
        this.context = context;
        this.carrinhoModelList = carrinhoModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.carrinho_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(carrinhoModelList.get(position).getImg_url()).into(holder.cartImg);
        holder.nome.setText(carrinhoModelList.get(position).getNome());
        nomeProd = carrinhoModelList.get(position).getNome();
        holder.quantidade.setText(""+carrinhoModelList.get(position).getQuantidade());
        Integer quant;
        quant = carrinhoModelList.get(position).getQuantidade();
        precoAntigo = carrinhoModelList.get(position).getPreco() / quant;
        holder.preco.setText("R$ " + precoAntigo * quant);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (tudo== null){
            tudo = 0f;
        }
        total = carrinhoModelList.get(position).getPreco();

        tudo += total;
        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .update("valorTotal", tudo);
        holder.aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade_num = Integer.parseInt(holder.quantidade.getText().toString()) + 1;
                holder.quantidade.setText("" + quantidade_num);
                quantidadePreco(position);
                holder.preco.setText(""+precoNovo);
            }
        });

        holder.diminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade_num = Integer.parseInt(holder.quantidade.getText().toString());
                if (quantidade_num != 1) {
                    quantidade_num -= 1;
                    holder.quantidade.setText("" + quantidade_num);
                }
                quantidadePreco(position);
                holder.preco.setText(""+precoNovo);
            }
        });

    }


    @Override
    public int getItemCount() {
        return carrinhoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cartImg;
        TextView nome, preco, quantidade, precoTotal;
        ImageView aumentar, diminuir;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImg = itemView.findViewById(R.id.img_carrinho);
            nome = itemView.findViewById(R.id.nome_carrinho);
            preco = itemView.findViewById(R.id.preco_carrinho);
            quantidade = itemView.findViewById(R.id.tv_quant);
            aumentar = itemView.findViewById(R.id.aumentar);
            diminuir = itemView.findViewById(R.id.diminuir);

        }
    }

    private void quantidadePreco(int position){
        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").whereEqualTo("nome", carrinhoModelList.get(position).getNome()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String produto = document.getId();
                            db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                    .collection("CurrentUser").document(produto).update("quantidade", quantidade_num);
                            precoNovo = carrinhoModelList.get(position).getPreco()/ carrinhoModelList.get(position).getQuantidade() * quantidade_num;
                            db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                    .collection("CurrentUser").document(produto).update("preco", precoNovo);
                        }
                    }
                });
    }
    }


