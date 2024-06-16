package com.example.matrel.Opcoes.MeusPedidos;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrel.Carrinho.CarrinhoAdapter;
import com.example.matrel.Carrinho.CarrinhoModel;
import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MeusPedidosFragment extends Fragment implements MeusPedidosInterface{
    RecyclerView pedidosRec;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<MeusPedidosModel> meusPedidosModelList;
    MeusPedidosAdapter meusPedidosAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_pedidos, container, false);


            db = FirebaseFirestore.getInstance();
            pedidosRec = view.findViewById(R.id.pedidosRec);
            auth = FirebaseAuth.getInstance();
            pedidosRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            meusPedidosModelList = new ArrayList<>();
            meusPedidosAdapter = new MeusPedidosAdapter(getActivity(), meusPedidosModelList, this);
            pedidosRec.setAdapter(meusPedidosAdapter);


            db.collection("Comprado").document(auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    MeusPedidosModel meusPedidosModel = document.toObject(MeusPedidosModel.class);
                                    meusPedidosModelList.add(meusPedidosModel);
                                    meusPedidosAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        return view;
    }
    private void loadFragment(Fragment fragment, Bundle b){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onItemClick(int position) {
        Bundle b = new Bundle();
        b.putString("nome",meusPedidosModelList.get(position).getNome().toString());
        loadFragment(new ProdutoFragment(), b);
    }
}