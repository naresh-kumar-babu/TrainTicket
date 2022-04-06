package com.example.trainticket;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth;
    private Spinner spinner_from,spinner_to;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter recyclerAdapter;
    private DatabaseReference databaseReference;
    private String train,date;
    private TextView journey_date,no_trains_found;
    private Button searchBtn;
    private ScrollView display_trains;
    int year,month,day;
    String train_name, train_no, train_departure, train_arrival, train_2s, train_sl, train_3a, train_2a, train_1a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trains");
        bottomNavigationView = findViewById(R.id.bottom_nav_dash);
        bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
        journey_date = findViewById(R.id.dash_date);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        searchBtn = findViewById(R.id.dashboard_searchbtn);
        display_trains = findViewById(R.id.display_trains);
        no_trains_found = findViewById(R.id.no_trains_found);
        recyclerView = findViewById(R.id.trains_recyclerview);
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        String[] items_from = new String[]{"From","Tambaram","Karur","Tuticorin","Tiruvallur","Mayiladuturai","Erode","Tiruppur","Chennai"};
        String[] items_to = new String[]{"To","Tambaram","Karur","Tuticorin","Tiruvallur","Mayiladuturai","Erode","Tiruppur","Chennai"};
        ArrayAdapter<String> adapter_from = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_from);
        spinner_from.setAdapter(adapter_from);
        ArrayAdapter<String> adapter_to = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_to);
        spinner_to.setAdapter(adapter_to);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,1);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        date = day + "/" + month + "/" + year;
        journey_date.setText(date);

        journey_date.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    DashboardActivity.this,
                    android.R.style.Theme_Material_Light_Dialog,
                    mDateSetListener,
                    year,month,day
            );
            Calendar cal1 = Calendar.getInstance();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
            cal1.add(Calendar.DAY_OF_YEAR,120);
            dialog.getDatePicker().setMaxDate(cal1.getTimeInMillis());
            dialog.show();
        });
        mDateSetListener = (datePicker, i, i1, i2) -> {
            year = i;
            month = i1+1;
            day = i2;
            date = day + "/" + month + "/" + year;
            journey_date.setText(date);
        };

        searchBtn.setOnClickListener(view -> {
            String from_place = spinner_from.getSelectedItem().toString();
            String to_place = spinner_to.getSelectedItem().toString();
            String path = from_place+"_"+to_place;
            if(TextUtils.isEmpty(from_place) || TextUtils.isEmpty(to_place) || TextUtils.isEmpty(date)){
                Toast.makeText(DashboardActivity.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                return;
            }
            if(from_place.equals("From")){
                Toast.makeText(DashboardActivity.this,"Choose boarding point",Toast.LENGTH_SHORT).show();
                return;
            }
            if(to_place.equals("To")){
                Toast.makeText(DashboardActivity.this,"Choose destination point",Toast.LENGTH_SHORT).show();
                return;
            }
            if(from_place.equals(to_place)){
                Toast.makeText(DashboardActivity.this,"Boarding and Destination points can't be same",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!(TextUtils.isEmpty(from_place) && TextUtils.isEmpty(to_place) && TextUtils.isEmpty(date))){
                Query query= databaseReference.orderByChild("train_path")
                        .equalTo(path);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.hasChildren()){
                            no_trains_found.setVisibility(View.VISIBLE);
                        }else{
                            no_trains_found.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                display_trains.setVisibility(View.VISIBLE);
                FirebaseRecyclerOptions<Train> options =
                        new FirebaseRecyclerOptions.Builder<Train>()
                                .setQuery(query,Train.class)
                                .build();

                System.out.println(options);

                recyclerAdapter = new FirebaseRecyclerAdapter<Train,TrainViewHolder>(options){
                    @NonNull
                    @Override
                    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        final View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.train_view_layout,parent,false);

                        view.findViewById(R.id.seats_view).setOnClickListener(mview -> {
                            LinearLayout train_seats = view.findViewById(R.id.train_seats);
                            TextView seats_view = view.findViewById(R.id.seats_view);
                            if(train_seats.getVisibility()==View.VISIBLE){
                                train_seats.setVisibility(View.GONE);
                                seats_view.setText("View More");
                            }
                            else if(train_seats.getVisibility()==View.GONE){
                                train_seats.setVisibility(View.VISIBLE);
                                seats_view.setText("View Less");
                            }

                                view.findViewById(R.id.seats_2s).setOnClickListener(sview -> {
                                    if(Integer.parseInt(train_2s)>0) {
                                    Intent intent = new Intent(DashboardActivity.this, PassengerDetailsActivity.class);
                                    intent.putExtra("seat_availability", train_2s);
                                    intent.putExtra("seat_type","2s");
                                    intent.putExtra("train_name",train_name);
                                    intent.putExtra("train_no",train_no);
                                    intent.putExtra("train_arrival",train_arrival);
                                    intent.putExtra("train_departure",train_departure);
                                    intent.putExtra("from_place",from_place);
                                    intent.putExtra("to_place",to_place);
                                    intent.putExtra("journey_date",date);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(DashboardActivity.this,"No seats available",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            view.findViewById(R.id.seats_sl).setOnClickListener(sview -> {
                                if(Integer.parseInt(train_sl)>0) {
                                Intent intent = new Intent(DashboardActivity.this, PassengerDetailsActivity.class);
                                    intent.putExtra("seat_availability", train_sl);
                                    intent.putExtra("seat_type","sl");
                                    intent.putExtra("train_name",train_name);
                                    intent.putExtra("train_no",train_no);
                                    intent.putExtra("train_arrival",train_arrival);
                                    intent.putExtra("train_departure",train_departure);
                                    intent.putExtra("from_place",from_place);
                                    intent.putExtra("to_place",to_place);
                                    intent.putExtra("journey_date",date);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DashboardActivity.this,"No seats available",Toast.LENGTH_SHORT).show();
                                }
                        });
                            view.findViewById(R.id.seats_3a).setOnClickListener(sview -> {
                                    if(Integer.parseInt(train_3a)>0) {
                                Intent intent = new Intent(DashboardActivity.this, PassengerDetailsActivity.class);
                                        intent.putExtra("seat_availability", train_3a);
                                        intent.putExtra("seat_type","3a");
                                        intent.putExtra("train_name",train_name);
                                        intent.putExtra("train_no",train_no);
                                        intent.putExtra("train_arrival",train_arrival);
                                        intent.putExtra("train_departure",train_departure);
                                        intent.putExtra("from_place",from_place);
                                        intent.putExtra("to_place",to_place);
                                        intent.putExtra("journey_date",date);
                                startActivity(intent);
                                }else{
                                    Toast.makeText(DashboardActivity.this,"No seats available",Toast.LENGTH_SHORT).show();
                                    }
                            });
                            view.findViewById(R.id.seats_2a).setOnClickListener(sview -> {
                                if(Integer.parseInt(train_2a)>0) {
                                Intent intent = new Intent(DashboardActivity.this, PassengerDetailsActivity.class);
                                    intent.putExtra("seat_availability", train_2a);
                                    intent.putExtra("seat_type","2a");
                                    intent.putExtra("train_name",train_name);
                                    intent.putExtra("train_no",train_no);
                                    intent.putExtra("train_arrival",train_arrival);
                                    intent.putExtra("train_departure",train_departure);
                                    intent.putExtra("from_place",from_place);
                                    intent.putExtra("to_place",to_place);
                                    intent.putExtra("journey_date",date);
                                startActivity(intent);
                                }else{
                                    Toast.makeText(DashboardActivity.this,"No seats available",Toast.LENGTH_SHORT).show();
                                }
                            });
                            view.findViewById(R.id.seats_1a).setOnClickListener(sview -> {
                                if(Integer.parseInt(train_1a)>0) {
                                Intent intent = new Intent(DashboardActivity.this, PassengerDetailsActivity.class);
                                    intent.putExtra("seat_availability", train_1a);
                                    intent.putExtra("seat_type","1a");
                                    intent.putExtra("train_name",train_name);
                                    intent.putExtra("train_no",train_no);
                                    intent.putExtra("train_arrival",train_arrival);
                                    intent.putExtra("train_departure",train_departure);
                                    intent.putExtra("from_place",from_place);
                                    intent.putExtra("to_place",to_place);
                                    intent.putExtra("journey_date",date);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DashboardActivity.this,"No seats available",Toast.LENGTH_SHORT).show();
                                }
                            });
                        });

                        return new TrainViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TrainViewHolder holder, int position, @NonNull Train model) {

                        train = getRef(position).getKey();
                        databaseReference.child(train).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {
                                    train_name = snapshot.child("train_name").getValue().toString();
                                    train_no = snapshot.child("train_no").getValue().toString();
                                    train_departure = snapshot.child("train_departure").getValue().toString();
                                    train_arrival = snapshot.child("train_arrival").getValue().toString();
                                    train_2s = snapshot.child("train_2s").getValue().toString();
                                    train_sl = snapshot.child("train_sl").getValue().toString();
                                    train_3a = snapshot.child("train_3a").getValue().toString();
                                    train_2a = snapshot.child("train_2a").getValue().toString();
                                    train_1a = snapshot.child("train_1a").getValue().toString();

                                    holder.setTrain_name(train_name);
                                    holder.setTrain_no(train_no);
                                    holder.setTrain_departure(train_departure);
                                    holder.setTrain_arrival(train_arrival);
                                    holder.setTrain_2s(train_2s);
                                    holder.setTrain_sl(train_sl);
                                    holder.setTrain_3a(train_3a);
                                    holder.setTrain_2a(train_2a);
                                    holder.setTrain_1a(train_1a);                        }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                };
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setReverseLayout(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.startListening();
            }
        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_dashboard:
                        return true;
                    case R.id.menu_transactions:
                        Intent tIntent = new Intent(DashboardActivity.this, prevJourneyActivity.class);
                        startActivity(tIntent);
                        return true;
                    case R.id.menu_wallet:
                        Intent wIntent = new Intent(DashboardActivity.this, WalletActivity.class);
                        startActivity(wIntent);
                        return true;
                    case R.id.menu_profile:
                        Intent pIntent = new Intent(DashboardActivity.this, ProfileActivity.class);
                        startActivity(pIntent);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (recyclerAdapter != null)
            recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recyclerAdapter != null)
            recyclerAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerAdapter != null)
            recyclerAdapter.startListening();
    }


    class TrainViewHolder extends RecyclerView.ViewHolder{

        View mview;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTrain_no(String train_no) {
            TextView t = mview.findViewById(R.id.train_no);
            t.setText(train_no);
        }

        public void setTrain_name(String train_name) {
            TextView t = mview.findViewById(R.id.train_name);
            t.setText(train_name);
        }

        public void setTrain_departure(String train_departure) {
            TextView t = mview.findViewById(R.id.train_departure);
            t.setText(train_departure);
        }

        public void setTrain_arrival(String train_arrival) {
            TextView t = mview.findViewById(R.id.train_arrival);
            t.setText(train_arrival);
        }

        public void setTrain_2s(String train_2s) {
            TextView t = mview.findViewById(R.id.train_2s);
            t.setText("AVL "+train_2s);
        }

        public void setTrain_sl(String train_sl) {
            TextView t = mview.findViewById(R.id.train_sl);
            t.setText("AVL "+train_sl);
        }

        public void setTrain_3a(String train_3a) {
            TextView t = mview.findViewById(R.id.train_3a);
            t.setText("AVL "+train_3a);
        }

        public void setTrain_2a(String train_2a) {
            TextView t = mview.findViewById(R.id.train_2a);
            t.setText("AVL "+train_2a);
        }

        public void setTrain_1a(String train_1a) {
            TextView t = mview.findViewById(R.id.train_1a);
            t.setText("AVL "+train_1a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changePassword:
                Intent wIntent = new Intent(DashboardActivity.this, ChangePassswordActivity.class);
                startActivity(wIntent);
                return true;
            case R.id.logout:
                mAuth.signOut ();
                finish ();
                startActivity ( new Intent ( this, LoginActivity.class ) );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}