package com.example.matrel;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrel.Auths.ContaFragment;
import com.example.matrel.Auths.LoginFragment;
import com.example.matrel.Carrinho.CarrinhoFragment;
import com.example.matrel.Departamentos.DepartamentosFragment;
import com.example.matrel.Destaques.DestaquesModel;
import com.example.matrel.Destaques.HomeFragment;
import com.example.matrel.Favoritos.FavoritosFragment;
import com.example.matrel.Produto.ProdutoFragment;
import com.example.matrel.VerTodos.VerTodosAdapter;
import com.example.matrel.VerTodos.VerTodosInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VerTodosInterface {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    TextView tvHeader;
    EditText searchBox;
    FirebaseFirestore db;
    private List<DestaquesModel> destaquesModelList;
    private RecyclerView recyclerViewSearch;
    private VerTodosAdapter verTodosAdapter;
    ImageView carrinho;
    FirebaseAuth auth;
    LinearLayout searchLinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        tvHeader = findViewById(R.id.textView);
        searchBox = findViewById(R.id.search_box);
        carrinho = findViewById(R.id.carrinho);
        searchLinear = findViewById(R.id.linearSearch);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                int itemId = menuItem.getItemId();

                if (itemId == R.id.home){
                    searchBox.setVisibility(View.VISIBLE);
                    carrinho.setVisibility(View.VISIBLE);
                    searchLinear.setVisibility(View.VISIBLE);
                    tvHeader.setVisibility(View.INVISIBLE);
                }else{

                    searchBox.setText("");
                    searchBox.setVisibility(View.INVISIBLE);
                    carrinho.setVisibility(View.INVISIBLE);
                    searchLinear.setVisibility(View.INVISIBLE);
                    tvHeader.setVisibility(View.VISIBLE);
                }


                if (itemId == R.id.home){
                    homeClicked();
                }else if (itemId == R.id.departamentos){
                    tvHeader.setText("Departamentos");
                    departamentosClicked();
                } else if (itemId == R.id.favoritos && auth.getCurrentUser() != null){
                    tvHeader.setText("Favoritos");
                    favoritosClicked();

                }else if (itemId == R.id.conta){
                    tvHeader.setText("Conta");
                    if(auth.getCurrentUser() != null){

                        loadFragment(new ContaFragment());
                    }else{
                        loginClicked();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Crie uma conta para usar os Favoritos", Toast.LENGTH_SHORT).show();
                    homeClicked();
                }
                if (itemId == R.id.home){
                    return true;
                }else if (itemId == R.id.departamentos){
                    return true;
                } else if (itemId == R.id.favoritos && auth.getCurrentUser() != null){
                    return true;
                }else if (itemId == R.id.conta) {
                    return true;
                }
                return true;
            }

        });


       homeClicked();

        recyclerViewSearch = findViewById(R.id.search_rec);
        destaquesModelList = new ArrayList<>();
        verTodosAdapter = new VerTodosAdapter(MainActivity.this, destaquesModelList, this);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewSearch.setAdapter(verTodosAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    destaquesModelList.clear();
                    verTodosAdapter.notifyDataSetChanged();
                }else{
                    searchProduct(s.toString());
                }
            }


        });

carrinho.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(auth.getCurrentUser() != null) {
            loadFragment(new CarrinhoFragment());
        }else{
            Toast.makeText(MainActivity.this, "Crie uma conta para utilizar o Carrinho", Toast.LENGTH_SHORT).show();
        }
    }
});

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void loadFragmentBundle(Fragment fragment, Bundle b){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void homeClicked(){
        loadFragment(new HomeFragment());
    }
    private void departamentosClicked(){
        loadFragment(new DepartamentosFragment());
    }
    private void favoritosClicked(){
        loadFragment(new FavoritosFragment());
    }
    private void loginClicked(){
        loadFragment(new LoginFragment());
    }

    private void searchProduct(String nome) {
        if (!nome.isEmpty()){
            db.collection("TodosProdutos").whereGreaterThanOrEqualTo("nome", nome).whereLessThanOrEqualTo("nome", nome+'\uf8ff').get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                 if (task.isSuccessful() && task.getResult() != null){
                                     destaquesModelList.clear();
                                     verTodosAdapter.notifyDataSetChanged();
                                     for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                         DestaquesModel destaquesModel = doc.toObject(DestaquesModel.class);
                                         destaquesModelList.add(destaquesModel);
                                         verTodosAdapter.notifyDataSetChanged();
                                     }
                                 }
                        }
                    });
        }
    }

    @Override
    public void onItemClick(int position, int index) {
        switch (index){
            case 0:
                Bundle b = new Bundle();
                b.putString("nome",destaquesModelList.get(position).getNome().toString());
                loadFragmentBundle(new ProdutoFragment(), b);
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
                    db.collection("Favoritos").document(auth.getCurrentUser().getUid())
                            .collection("CurrentUser").add(favMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(MainActivity.this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

            case 2:
                final HashMap<String, Object> carrinhoMap = new HashMap<>();
                carrinhoMap.put("nome", destaquesModelList.get(position).getNome());
                carrinhoMap.put("preco", destaquesModelList.get(position).getPreco());
                carrinhoMap.put("img_url", destaquesModelList.get(position).getImg_url());
                carrinhoMap.put("quantidade", 1);
                if (auth.getCurrentUser() != null) {
                    db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                            .collection("CurrentUser").add(carrinhoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(MainActivity.this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

        }
    }
}


