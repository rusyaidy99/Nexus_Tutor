package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nexustutor.R;
import com.example.nexustutor.activity_view_tutor_profile;
import com.example.nexustutor.tutor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.nexustutor.Adapter.RecyclerViewAdapter.*;

public class SearchTutorFragment extends Fragment {

    List<tutor> tutorList;

    DatabaseReference ref;

    private FirebaseRecyclerOptions<tutor> options;
    private FirebaseRecyclerAdapter<tutor, MyViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View searchview = inflater.inflate(R.layout.fragment_search_tutor, container, false);

        ref = FirebaseDatabase.getInstance().getReference().child("Tutors");



        /*tutorList = new ArrayList<>();
        tutorList.add(new tutor(id, "Encik A", "Kajang, Selangor", "Mathematics", R.drawable.profile_image));
        tutorList.add(new tutor(id, "Encik B", "Chendering, Terengganu", "English", R.drawable.profile_image));*/

        recyclerView = searchview.findViewById(R.id.recyclerview_tutor);
        recyclerView.setHasFixedSize(true);
        //RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getActivity(), tutorList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        options = new FirebaseRecyclerOptions.Builder<tutor>().setQuery(ref, tutor.class).build();
        adapter = new FirebaseRecyclerAdapter<tutor, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull final tutor tutor) {
                myViewHolder.tv_tutorname.setText(""+ tutor.getFullname());
                myViewHolder.tv_gender.setText(""+ tutor.getGender());
                Picasso.get().load(tutor.getImage()).into(myViewHolder.img_tutor);

                myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_view_tutor_profile.class);
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
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment


        return searchview;
    }

}