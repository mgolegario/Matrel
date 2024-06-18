package com.example.matrel.Favoritos;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrel.Destaques.DestaquesModel;
import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.R;
import com.example.matrel.VerTodos.VerTodosAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FavoritosFragment extends Fragment implements FavoritosInterface{
FirebaseFirestore db;
FavoritosAdapter favoritosAdapter;
List<DestaquesModel> destaquesModelList;
RecyclerView favRec;
long quantDocs;
TextView favVazio;
FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        db = FirebaseFirestore.getInstance();
        favRec = view.findViewById(R.id.recFav);
        auth = FirebaseAuth.getInstance();
        favRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        destaquesModelList = new ArrayList<>();
        favVazio = view.findViewById(R.id.favVazio);
        favoritosAdapter = new FavoritosAdapter(getActivity(), destaquesModelList, this);
        favRec.setAdapter(favoritosAdapter);

        if (auth.getCurrentUser() != null) {
            db.collection("Favoritos").document(auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DestaquesModel destaquesModel = document.toObject(DestaquesModel.class);
                                    destaquesModelList.add(destaquesModel);
                                    favoritosAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        Query query = db.collection("Favoritos").document(auth.getCurrentUser().getUid()).collection("CurrentUser");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    quantDocs = snapshot.getCount();
                    if (quantDocs == 0) {
                        favVazio.setVisibility(VISIBLE);
                    }
                }
            }
        });

        return view;
    }
    private void loadFragmentBundle(Fragment fragment, Bundle b){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onItemClick(int position, int index) {

        switch (index){
            case 0:
                if (auth.getCurrentUser() != null) {
                    db.collection("Favoritos").document(auth.getUid()).collection("CurrentUser").whereEqualTo("nome", destaquesModelList.get(position).getNome().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            task.getResult().getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                                @Override
                                public void accept(DocumentSnapshot documentSnapshot) {
                                    documentSnapshot.getReference().delete();
                                    Toast.makeText(getContext(), "Produto removido com sucesso", Toast.LENGTH_SHORT).show();
                                    loadFragment(new FavoritosFragment());
                                }
                            });
                        }
                    });
                }
                break;
            case 1:
                final HashMap<String, Object> carrinhoMap = new HashMap<>();
                carrinhoMap.put("nome", destaquesModelList.get(position).getNome());
                carrinhoMap.put("preco", destaquesModelList.get(position).getPreco());
                carrinhoMap.put("img_url", destaquesModelList.get(position).getImg_url());
                carrinhoMap.put("quantidade", 1);
                if (auth.getCurrentUser() != null) {
                    final HashMap<String, Object> valorTotalMap = new HashMap<>();
                    valorTotalMap.put("valorTotal", 0);
                    db.collection("AddToCart").document(auth.getCurrentUser().getUid()).update(valorTotalMap);
                    Query query = db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CurrentUser").whereEqualTo("nome", destaquesModelList.get(position).getNome());
                    AggregateQuery countQuery = query.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                AggregateQuerySnapshot snapshot = task.getResult();
                                quantDocs = snapshot.getCount();
                                if (quantDocs == 0) {
                                    db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                            .collection("CurrentUser").add(carrinhoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    Toast.makeText(getContext(), "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    Toast.makeText(getContext(), "O produto já foi adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                break;

            case 2:
                Bundle b = new Bundle();
                b.putString("nome",destaquesModelList.get(position).getNome().toString());
                loadFragmentBundle(new ProdutoFragment(), b);
                break;

        }


    }
}