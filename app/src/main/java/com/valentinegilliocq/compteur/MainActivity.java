package com.valentinegilliocq.compteur;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.valentinegilliocq.compteur.compteur.Compteur;
import com.valentinegilliocq.compteur.compteur.CompteurActivity;
import com.valentinegilliocq.compteur.compteur.CompteurAdapter;
import com.valentinegilliocq.compteur.compteur.CreateCompteurFormActivity;
import com.valentinegilliocq.compteur.database.CompteurManager;
import com.valentinegilliocq.compteur.user.UserHelper;
import com.valentinegilliocq.compteur.utils.AppUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String USER_PSEUDO_KEY = "userPseudo";
    public static final String USER_PREF = "userPref";


    private int limit = 15;
    private boolean isLastItemReached;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling;
    private RecyclerView recyclerView;
    private ArrayList<Compteur> list;
    private CompteurAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserPseudo();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateCompteurFormActivity.class));
            }
        });


        recyclerView = findViewById(R.id.compteur_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        productAdapter = new CompteurAdapter(this, list);
        recyclerView.setAdapter(productAdapter);

    }

    private void setupCompteurList() {
        final CollectionReference compteursRef = CompteurManager.getCollection();
        final Query query = compteursRef.whereEqualTo("creatorPseudo", AppUtils.getCreatorPseudo(this)).orderBy("lastModified", Query.Direction.DESCENDING).limit(limit);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Compteur compteur = documentChange.getDocument().toObject(Compteur.class);
                        switch (documentChange.getType()) {
                            case ADDED:
                                productAdapter.add(compteur);
                                break;
                            case REMOVED:
                                productAdapter.remove(compteur);
                                break;
                            case MODIFIED:
                                productAdapter.update(compteur);
                                break;
                        }
                    }
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        lastVisible = null;
                    } else {
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.getDocuments().size() - 1);
                    }

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                Query nextQuery = compteursRef.orderBy("lastModified", Query.Direction.DESCENDING).startAfter(lastVisible).limit(limit);
                                nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot qDs, @Nullable FirebaseFirestoreException e) {
                                        if (queryDocumentSnapshots != null) {
                                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                                Compteur compteur = documentChange.getDocument().toObject(Compteur.class);
                                                switch (documentChange.getType()) {
                                                    case ADDED:
                                                        productAdapter.add(compteur);
                                                        break;
                                                    case REMOVED:
                                                        productAdapter.remove(compteur);
                                                        break;
                                                    case MODIFIED:
                                                        productAdapter.update(compteur);
                                                        break;
                                                }
                                            }
                                            if (qDs.getDocuments().isEmpty()) {
                                                lastVisible = null;
                                            } else {
                                                lastVisible = qDs.getDocuments().get(qDs.getDocuments().size() - 1);

                                            }
                                            if (qDs.getDocuments().size() < limit) {
                                                isLastItemReached = true;
                                            }
                                        } else {
                                            isLastItemReached = true;
                                        }
                                    }
                                });
                            }
                        }
                    };
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            }
        });
    }

    private void checkUserPseudo() {
        String userPseudo = AppUtils.getCreatorPseudo(this);
        if (userPseudo == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(getApplicationContext());
            builder.setTitle(getResources().getString(R.string.alert_user_pseudo));
            builder.setView(edittext);
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            builder.setNegativeButton(getString(R.string.close_application), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    AppUtils.closeApplication(MainActivity.this);
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // write check code
                    if (TextUtils.isEmpty(edittext.getText().toString()) || edittext.getText().length() < 2) {
                        edittext.setError(getResources().getString(R.string.error_edit_text));
                        return;
                    }
                    final String pseudo = edittext.getText().toString();

                    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher m = p.matcher(pseudo);
                    // boolean b = m.matches();
                    boolean b = m.find();
                    if (b) {
                        edittext.setError(getResources().getString(R.string.error_need_only_lowercase_and_digits));
                        return;
                    }

                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "",
                            getString(R.string.checking_pseudo), true);
                    progressDialog.show();
                    UserHelper.checkUniquePseudo(pseudo)
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            edittext.setError(getResources().getString(R.string.error_pseudo_already_taken));
                                        } else {
                                            final ProgressDialog progressDialog2 = ProgressDialog.show(MainActivity.this, "",
                                                    getString(R.string.creating_user), true);
                                            progressDialog2.show();
                                            UserHelper.createPseudo(pseudo)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            progressDialog2.dismiss();
                                                            if(task.isSuccessful()) {
                                                                getSharedPreferences(USER_PREF, MODE_PRIVATE).edit().putString(USER_PSEUDO_KEY, pseudo).commit();
                                                                dialog.dismiss();
                                                                setupCompteurList();
                                                            }else{
                                                                edittext.setError(getString(R.string.error_network));
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog2.dismiss();
                                                            edittext.setError(getString(R.string.error_network));
                                                        }
                                                    });

                                        }
                                    } else {
                                        edittext.setError(getResources().getString(R.string.error_network));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    edittext.setError(getResources().getString(R.string.error_network));
                                }
                            });
                    // if every thing is Ok then dismiss dialog

                }
            });
        } else {
            setupCompteurList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
