package com.example.matrel.Carrinho;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrel.Produto.ProdutoAdapter;
import com.example.matrel.Produto.ProdutoModel;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoFragment extends Fragment{
    RecyclerView carrinhoRec;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<CarrinhoModel> carrinhoModelList;
    CarrinhoAdapter carrinhoAdapter;
    Button btnCompra;
    TextView total;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        db = FirebaseFirestore.getInstance();
        carrinhoRec = view.findViewById(R.id.carrinho_rec);
        auth = FirebaseAuth.getInstance();
        carrinhoRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        carrinhoModelList = new ArrayList<>();
        carrinhoAdapter = new CarrinhoAdapter(getActivity(), carrinhoModelList);
        carrinhoRec.setAdapter(carrinhoAdapter);
        total = view.findViewById(R.id.tv_Total);


        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CarrinhoModel carrinhoModel = document.toObject(CarrinhoModel.class);
                                    carrinhoModelList.add(carrinhoModel);
                                    carrinhoAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        db.collection("AddToCart").document(auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String valor = task.getResult().get("valorTotal").toString();
                        Float valorConv = Math.round(Float.parseFloat(valor) * 100) / 100.0F;
                        total.setText("R$ "+valorConv);
                    }
                });

        return view;
    }
}