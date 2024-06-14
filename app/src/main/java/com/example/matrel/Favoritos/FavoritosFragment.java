package com.example.matrel.Favoritos;

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
import android.widget.Toast;

import com.example.matrel.Destaques.DestaquesModel;
import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.R;
import com.example.matrel.VerTodos.VerTodosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritosFragment extends Fragment implements FavoritosInterface{
FirebaseFirestore db;
FavoritosAdapter favoritosAdapter;
List<DestaquesModel> destaquesModelList;
RecyclerView favRec;
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
        favoritosAdapter = new FavoritosAdapter(getActivity(), destaquesModelList, this);
        favRec.setAdapter(favoritosAdapter);


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
        b.putString("nome",destaquesModelList.get(position).getNome().toString());
        loadFragment(new ProdutoFragment(), b);
    }
}