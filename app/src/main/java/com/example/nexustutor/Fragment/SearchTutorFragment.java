package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.SubjectRecyclerViewAdapter;
import com.example.nexustutor.Offer;
import com.example.nexustutor.R;
import com.example.nexustutor.Subjek;
import com.example.nexustutor.activity_comment_offer;
import com.example.nexustutor.activity_view_tutor_profile;
import com.example.nexustutor.tutor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nexustutor.Adapter.RecyclerViewAdapter.*;

public class SearchTutorFragment extends Fragment {
    Toolbar toolbar;
    List<tutor> tutorList;

    private Spinner catSpinner,yearSpinner;
    private TextView filter;
    private List<String> categories,year;
    private ArrayAdapter categoryAdapter,yearAdapter;
    private String catergoryString,yearString;

    DatabaseReference ref;

    private FirebaseRecyclerOptions<tutor> options;
    private FirebaseRecyclerAdapter<tutor, MyViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View searchview = inflater.inflate(R.layout.fragment_search_tutor, container, false);

        ref = FirebaseDatabase.getInstance().getReference().child("Tutors");

        toolbar = searchview.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

       /* catSpinner = searchview.findViewById(R.id.catspinner);
        yearSpinner = searchview.findViewById(R.id.datespinner);
        filter = searchview.findViewById(R.id.filter);*/

       /* categories = new ArrayList<>();
        year = new ArrayList<>();
        categories.add("Categories");
        year.add("year");
        categoryAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,categories);
        yearAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,year);
*/
        /*tutorList = new ArrayList<>();
        tutorList.add(new tutor(id, "Encik A", "Kajang, Selangor", "Mathematics", R.drawable.profile_image));
        tutorList.add(new tutor(id, "Encik B", "Chendering, Terengganu", "English", R.drawable.profile_image));*/

        recyclerView = searchview.findViewById(R.id.recyclerview_tutor);
        recyclerView.setHasFixedSize(true);
        //RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getActivity(), tutorList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        options = new FirebaseRecyclerOptions.Builder<tutor>().setQuery(ref, tutor.class).build();
        adapter = new FirebaseRecyclerAdapter<tutor, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final tutor tutor) {
                myViewHolder.tv_tutorname.setText(""+ tutor.getFullname());
                myViewHolder.tv_gender.setText(""+ tutor.getGender());
                Picasso.get().load(tutor.getImage()).into(myViewHolder.img_tutor);


                myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_view_tutor_profile.class);
                        intent.putExtra("uid", tutor.getUid());
                        intent.putExtra("Name", tutor.getFullname());
                        intent.putExtra("Gender", tutor.getGender());
                        intent.putExtra("AccType", tutor.getAcctype());
                        intent.putExtra("image", tutor.getImage());


                        /*intent.putExtra("Email", mData.get(position).getEmail());*/
                        startActivity(intent);
                    }
                });
                DatabaseReference myrefSubject = FirebaseDatabase.getInstance().getReference();
                DatabaseReference subjectRef = myrefSubject.child("Tutors").child(tutor.getUid()).child("Subjects");
                final ArrayList subjects = new ArrayList<>();

                subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            String data = ds.child("mySubject").getValue(String.class);
                            subjects.add("" + String.valueOf(data));

                        }
                        String countt = String.valueOf(snapshot.getChildrenCount());
                        Log.d("subject", countt );
                        String subjectlist = Arrays.toString(subjects.toArray()).replace("[", "").replace("]", "").trim();
                        if (subjectlist.equals(""))
                            myViewHolder.tv_subject.setText("No Subject Added");
                        else
                        myViewHolder.tv_subject.setText(subjectlist);

                /*Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                Log.d("timestamp", ts);*/


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_search,parent, false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment

       /* catSpinner.setAdapter(categoryAdapter);
        yearSpinner.setAdapter(yearAdapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catergoryString = (String) parent.getItemAtPosition(position);
                catSpinner.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catergoryString = "";

            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearString = (String) parent.getItemAtPosition(position);
                yearSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                yearString = "";

            }
        });*/

        return searchview;
    }
    private void searchStudent(String SearchText) {
        Log.d("search", SearchText);
        Query searchQuery = ref.orderByChild("fullname").startAt(SearchText).endAt(SearchText + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<tutor>().setQuery(searchQuery, tutor.class).build();
        FirebaseRecyclerAdapter<tutor, MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<tutor, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull final tutor tutor) {
                myViewHolder.tv_tutorname.setText(""+ tutor.getFullname());
                myViewHolder.tv_gender.setText(""+ tutor.getGender());
                Picasso.get().load(tutor.getImage()).into(myViewHolder.img_tutor);


                myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_view_tutor_profile.class);
                        intent.putExtra("uid", tutor.getUid());
                        intent.putExtra("Name", tutor.getFullname());
                        intent.putExtra("Gender", tutor.getGender());
                        intent.putExtra("AccType", tutor.getAcctype());
                        intent.putExtra("image", tutor.getImage());

                        /*intent.putExtra("Email", mData.get(position).getEmail());*/
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_search,parent, false);
                return new MyViewHolder(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchStudent(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchStudent(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

       /* if (id == R.id.action_settings){
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}