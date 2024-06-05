package com.example.matrel.Auths;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matrel.R;
import com.google.firebase.auth.FirebaseAuth;

public class ContaFragment extends Fragment {

    TextView tvMeusDados, tvMeusPedidos, tvFavoritos, tvEnderecos, tvCartoes, tvSair;
    Integer FragmentSelector;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conta, container, false);
        auth  = FirebaseAuth.getInstance();
        tvMeusDados = view.findViewById(R.id.tvMeusDados);
        tvSair = view.findViewById(R.id.tvSair);
        tvSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                loadFragment(new LoginFragment());
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