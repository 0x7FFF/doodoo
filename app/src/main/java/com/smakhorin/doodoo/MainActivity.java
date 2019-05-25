package com.smakhorin.doodoo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FoodCard foodCard_data[];
    private FoodCardAdapter FoodCardAdapter;
    private int i;
    private Uri uri;
    int index = 0;
    private FirebaseAuth mAuth;
    //@InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    ListView listView;
    List<FoodCard> foodCardItems;

    List<String> foodDb = new ArrayList<>(); // List of food in Database
    HashMap<String,HashMap<String,String>> foodData = new HashMap<>(); // Data for each food on the list (Place - Price and one row for Image)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_swipe);

        mAuth = FirebaseAuth.getInstance();

        checkUserSex();

        foodCardItems = new ArrayList<FoodCard>();

        FoodCardAdapter = new FoodCardAdapter(this, R.layout.activity_main_win, foodCardItems);

        //Fill up each food item
        final int test = 0;
        DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
        //mFirebase.keepSynced(true);
        mFirebase.child("Cached").child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Fill up each food item
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    foodDb.add(childSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Fill up each food item with Place-Price and image

        for (int i = 0; i < foodDb.size(); i++) {
            final String foodName = foodDb.get(i);
            mFirebase.child("Cached").child(foodName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Fill up each food item
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(childSnapshot.getKey(),childSnapshot.getValue().toString());
                        foodData.put(foodName,temp);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //old code - DO NOT TOUCH
        foodCardItems.add(new FoodCard("Vyzamin Inc.", "TEST nuber","99","https://pp.userapi.com/c830209/v830209514/1e0827/tiyBnn-XxrI.jpg"));
        foodCardItems.add(new FoodCard("TESTE", "TEST ","78","https://pp.userapi.com/c830209/v830209514/1e081e/EfyGLl-YRqc.jpg"));
        foodCardItems.add(new FoodCard("KOKO", "uiuiuiui","777","https://pp.userapi.com/c830209/v830209514/1e0815/PkK9JN36e40.jpg"));

        //try
        for(String name : foodData.keySet()) {
            HashMap<String,String> item = foodData.get(name);
            Integer medium = 0;
            Integer count = item.size();
            String imageUrl = item.get("Image");
            for(String placeName : item.keySet()) {
                if(!placeName.equals("Image")) {
                    medium += Integer.parseInt(item.get(placeName));
                }
            }
            medium /= count;
            foodCardItems.add(new FoodCard(name,count.toString(),medium.toString(),imageUrl));
        }

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(FoodCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                foodCardItems.remove(0);
                FoodCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
               Toast.makeText(MainActivity.this,"left",Toast.LENGTH_SHORT).show();//makeToast(MainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this,"right",Toast.LENGTH_SHORT).show();//
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // we don't have worry about that anymore
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                /*View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);*/
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_SHORT).show();//
            }
        });

    }

    private String userSex;
    private String oppositeUserSex;

    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userSex = "Male";
                    oppositeUserSex = "Female";
                    getOppositeSexUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userSex = "Female";
                    oppositeUserSex = "Male";
                    getOppositeSexUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getOppositeSexUsers() {
        // oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex);
        DatabaseReference foodDb = FirebaseDatabase.getInstance().getReference().child("Food").child("Pizza");
        foodDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                   String imageURL = "default";
                   imageURL = dataSnapshot.getValue().toString();
                   FoodCard item = new FoodCard("Vyzamin Inc.", "TEST nuber","99", imageURL);
                    foodCardItems.add(item);
                    FoodCardAdapter.notifyDataSetChanged();
                    index++;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToMaps(View view) {
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    /*
    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.right)
    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }*/



}
