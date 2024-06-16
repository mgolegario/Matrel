package com.example.matrel.Departamentos;

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

import com.example.matrel.R;
import com.example.matrel.VerTodos.VerTodosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DepartamentosFragment extends Fragment implements DepartamentosInterface{
    RecyclerView depRec;
    FirebaseFirestore db;
    List<DepartamentosModel> departamentosModelList;
    DepartamentosAdapter departamentosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_departamentos, container, false);

        db = FirebaseFirestore.getInstance();
        depRec = view.findViewById(R.id.dep_recV);

        depRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        departamentosModelList = new ArrayList<>();
        departamentosAdapter = new DepartamentosAdapter(getActivity(), departamentosModelList, this);
        depRec.setAdapter(departamentosAdapter);

        db.collection("Departamentos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DepartamentosModel departamentosModel = document.toObject(DepartamentosModel.class);
                                departamentosModelList.add(departamentosModel);
                                departamentosAdapter.notifyDataSetChanged();
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
            b.putString("departamento",departamentosModelList.get(position).getNome().toString());
            loadFragment(new VerTodosFragment(), b);
    }


}