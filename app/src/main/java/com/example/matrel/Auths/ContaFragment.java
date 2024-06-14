package com.example.matrel.Auths;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matrel.Favoritos.FavoritosFragment;
import com.example.matrel.MeusDadosFragment;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.List;

public class ContaFragment extends Fragment {

    TextView tvMeusDados, tvMeusPedidos, tvFavoritos, tvEnderecos, tvCartoes, tvSair, tvNome, tvEmail;
    Integer FragmentSelector;
    DatabaseReference db;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conta, container, false);
        auth  = FirebaseAuth.getInstance();
        tvMeusDados = view.findViewById(R.id.tvMeusDados);
        tvNome = view.findViewById(R.id.nomeCompleto);
        tvEmail = view.findViewById(R.id.emailDisplay);
        tvFavoritos = view.findViewById(R.id.tvFavoritos);
        tvSair = view.findViewById(R.id.tvSair);
        db = FirebaseDatabase.getInstance().getReference();

        db.child("Users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                   UserModel query = task.getResult().getValue(UserModel.class);

                   tvNome.setText(""+ query.getName());
                   tvEmail.setText(""+ query.getEmail());
                }
            }
        });

        tvMeusDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MeusDadosFragment());
            }
        });

        tvSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                loadFragment(new LoginFragment());
            }
        });

        tvFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FavoritosFragment());
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

}