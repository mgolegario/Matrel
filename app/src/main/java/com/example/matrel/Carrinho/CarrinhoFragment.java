package com.example.matrel.Carrinho;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrel.Favoritos.FavoritosFragment;
import com.example.matrel.Pagamento.PaymentMethodFragment;
import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CarrinhoFragment extends Fragment implements CarrinhoInterface{
    RecyclerView carrinhoRec;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<CarrinhoModel> carrinhoModelList;
    CarrinhoAdapter carrinhoAdapter;
    Button btnCompra;
    long quantDocs;
    TextView total, carrinhoVazio;


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
        carrinhoAdapter = new CarrinhoAdapter(getActivity(), carrinhoModelList, this);
        carrinhoRec.setAdapter(carrinhoAdapter);
        carrinhoVazio = view.findViewById(R.id.carrinhoVazio);
        total = view.findViewById(R.id.tv_Total);

        if (auth.getCurrentUser() != null) {
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
        }
        if (auth.getCurrentUser() != null) {
            db.collection("AddToCart").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    String valor = value.get("valorTotal").toString();
                    Float valorConv = Math.round(Float.parseFloat(valor) * 100) / 100.0F;
                    total.setText("R$ " + valorConv);
                }
            });
        }else{
            total.setText("R$ 0");
        }
        Query query = db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CurrentUser");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    quantDocs = snapshot.getCount();
                    if (quantDocs == 0) {
                        carrinhoVazio.setVisibility(VISIBLE);
                    }
                }
            }
        });
        btnCompra = view.findViewById(R.id.btn_compra_carrinho);

        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CurrentUser");
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            quantDocs = snapshot.getCount();
                            if (auth.getCurrentUser() != null && quantDocs != 0) {
                                loadFragment(new PaymentMethodFragment());
                            } else {
                                Toast.makeText(getContext(), "O carrinho está vazio", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });


        return view;
    }
    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void loadFragmentBundle(Fragment fragment, Bundle b){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onItemClick(int position, int index) {
        switch (index){
            case 0:
                Bundle b = new Bundle();
                b.putString("nome",carrinhoModelList.get(position).getNome().toString());
                loadFragmentBundle(new ProdutoFragment(), b);
                break;
            case 1:
                if (auth.getCurrentUser() != null) {
                    db.collection("AddToCart").document(auth.getUid()).collection("CurrentUser").whereEqualTo("nome", carrinhoModelList.get(position).getNome().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            task.getResult().getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                                @Override
                                public void accept(DocumentSnapshot documentSnapshot) {
                                    documentSnapshot.getReference().delete();
                                    Toast.makeText(getContext(), "Produto removido com sucesso", Toast.LENGTH_SHORT).show();
                                    db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                            .update("valorTotal", carrinhoModelList.get(position).getQuantidade() * carrinhoModelList.get(position).getPreco() - carrinhoModelList.get(position).getPreco());
                                    loadFragment(new CarrinhoFragment());
                                }
                            });
                        }
                    });
                }
                break;
        }
    }
}