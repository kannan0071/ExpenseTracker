package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeFragment extends Fragment {

    //Firebase DB
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //RecyclerView
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    //private FirebaseRecyclerAdapter adapter;
    private IncomeRecyclerAdapter incomeRecyclerAdapter;
    private List<Data> incomeList;
    private TextView incomeTotalSum;
    // income_recycler_data
    private TextView incomeDate;
    private TextView incomeType;
    private TextView incomeNote;
    private TextView incomeAmount;


    //Update editText..
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //Update and Delete button..
    private Button btnUpdate;
    private Button btnDelete;

    //Data item value..
    private String type;
    private String note;
    private int amount;
    private String post_key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            mIncomeDatabase = FirebaseDatabase.getInstance("https://expense-tracker-a0331-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("IncomeData").child(uid);
        }
        mIncomeDatabase.keepSynced(true);
        incomeTotalSum = myview.findViewById(R.id.income_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            //@SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalvalue = 0;
                for (DataSnapshot mySnapshot : snapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    assert data != null;
                    totalvalue += data.getAmount();
                    String stTotalValue = String.valueOf(totalvalue);
                    incomeTotalSum.setText(stTotalValue + ".00");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        incomeList = new ArrayList<>();
        incomeRecyclerAdapter = new IncomeRecyclerAdapter(incomeList);
        recyclerView.setAdapter(incomeRecyclerAdapter);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);

                    incomeList.add(data);
                }
                incomeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Calender view

        calendarView = myview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = getFormattedDate(year, month, dayOfMonth);
            fetchIncomeDataForDate(selectedDate);
       });

        return myview;
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void fetchIncomeDataForDate(String selectedDate) {
        //DatabaseReference incomeRef = FirebaseDatabase.getInstance("https://expense-tracker-a0331-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("IncomeData");

        Query query = mIncomeDatabase.orderByChild("date").equalTo(selectedDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Data> incomeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data data = snapshot.getValue(Data.class);
                    incomeList.add(data);
                }
                populateRecyclerView(incomeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors or canceled database queries
            }
        });
    }

    private void populateRecyclerView(List<Data> incomeList) {
        IncomeRecyclerAdapter adapter = new IncomeRecyclerAdapter(incomeList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

