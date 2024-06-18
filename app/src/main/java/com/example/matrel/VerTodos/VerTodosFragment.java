package com.example.matrel.VerTodos;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class VerTodosFragment extends Fragment implements VerTodosInterface {

    RecyclerView todosItensRec;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<DestaquesModel> destaquesModelList;
    VerTodosAdapter verTodosAdapter;
    long quantDocs;
    TextView categText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ver_todos, container, false);

        db = FirebaseFirestore.getInstance();
        todosItensRec = view.findViewById(R.id.todosItensRec);
        auth = FirebaseAuth.getInstance();
        todosItensRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        destaquesModelList = new ArrayList<>();
        verTodosAdapter = new VerTodosAdapter(getActivity(), destaquesModelList, this);
        todosItensRec.setAdapter(verTodosAdapter);
        categText = view.findViewById(R.id.categoria_text);

        Bundle b = this.getArguments();

        if (b.getString("categoria")!= null && b.getString("categoria").equals("destaque")){
            categText.setText("Destaques");
            db.collection("TodosProdutos").whereEqualTo("destaque", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DestaquesModel destaquesModel = document.toObject(DestaquesModel.class);
                                    destaquesModelList.add(destaquesModel);
                                    verTodosAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else if (b.getString("categoria")!= null && b.getString("categoria").equals("maisProcurado")){
            categText.setText("Mais Procurados");
            db.collection("TodosProdutos").whereEqualTo("procurado", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DestaquesModel destaquesModel = document.toObject(DestaquesModel.class);
                                    destaquesModelList.add(destaquesModel);
                                    verTodosAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else if(b.getString("departamento")!= null){
            categText.setText(b.getString("departamento"));
            query("type",b.getString("departamento"));
        }



        return view;
    }

    private void query(String campo, String valor){
        db.collection("TodosProdutos").whereEqualTo(campo, valor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DestaquesModel destaquesModel = document.toObject(DestaquesModel.class);
                                destaquesModelList.add(destaquesModel);
                                verTodosAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void loadFragment(Fragment fragment, Bundle b){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onItemClick(int position, int index) {
        switch (index){
            case 0:
                Bundle b = new Bundle();
                b.putString("nome",destaquesModelList.get(position).getNome().toString());
                loadFragment(new ProdutoFragment(), b);
                break;
            case 1:
                final HashMap<String, Object> favMap = new HashMap<>();
                favMap.put("nome", destaquesModelList.get(position).getNome());
                favMap.put("preco", destaquesModelList.get(position).getPreco());
                favMap.put("img_url", destaquesModelList.get(position).getImg_url());
                favMap.put("avaliacoes", destaquesModelList.get(position).getAvaliacoes());
                favMap.put("type", destaquesModelList.get(position).getType());
                favMap.put("destaque", destaquesModelList.get(position).getDestaque());
                favMap.put("procurado", destaquesModelList.get(position).getProcurado());
                if (auth.getCurrentUser() != null) {

                    Query query = db.collection("Favoritos").document(auth.getCurrentUser().getUid()).collection("CurrentUser").whereEqualTo("nome", destaquesModelList.get(position).getNome());
                    AggregateQuery countQuery = query.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                AggregateQuerySnapshot snapshot = task.getResult();
                                quantDocs = snapshot.getCount();
                                if (quantDocs == 0) {
                                    db.collection("Favoritos").document(auth.getCurrentUser().getUid())
                                            .collection("CurrentUser").add(favMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    Toast.makeText(getContext(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    Toast.makeText(getContext(), "O produto já foi adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Crie uma conta para utilizar os Favoritos", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                final HashMap<String, Object> carrinhoMap = new HashMap<>();
                carrinhoMap.put("nome", destaquesModelList.get(position).getNome());
                carrinhoMap.put("preco", destaquesModelList.get(position).getPreco());
                carrinhoMap.put("img_url", destaquesModelList.get(position).getImg_url());
                carrinhoMap.put("quantidade", 1);

                if (auth.getCurrentUser() != null) {
                    final HashMap<String, Object> valorTotalMap = new HashMap<>();
                    valorTotalMap.put("valorTotal", 0);
                    db.collection("AddToCart").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            task.getResult().getReference().set(valorTotalMap);
                        }
                    });
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
                }else {
                    Toast.makeText(getContext(), "Crie uma conta para utilizar o Carrinho", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}