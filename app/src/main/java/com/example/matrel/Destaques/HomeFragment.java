package com.example.matrel.Destaques;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.R;
import com.example.matrel.VerTodos.VerTodosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements DestaquesInterface {

    RecyclerView destaquesRec;
    FirebaseFirestore db;
    List<DestaquesModel> destaquesModelList;
    DestaquesAdapter destaquesAdapter;
    LinearLayout verTodosdstq, verTodosmsprd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        destaquesRec = v.findViewById(R.id.destaques_rec);

        destaquesRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        destaquesModelList = new ArrayList<>();
        destaquesAdapter = new DestaquesAdapter(getActivity(), destaquesModelList, this);
        destaquesRec.setAdapter(destaquesAdapter);
        verTodosdstq = v.findViewById(R.id.verTodosLLdstq);
        verTodosmsprd = v.findViewById(R.id.verTodosLLmsprd);

        db.collection("TodosProdutos").whereEqualTo("destaque", true)
                .limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DestaquesModel destaquesModel = document.toObject(DestaquesModel.class);
                                destaquesModelList.add(destaquesModel);
                                destaquesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        verTodosdstq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("categoria","destaque");
                loadFragment(new VerTodosFragment(), b);

            }
        });

        verTodosmsprd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("categoria","maisProcurado");
                loadFragment(new VerTodosFragment(), b);
            }
        });

        return v;
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
