package com.smakhorin.doodoo;

import android.content.Intent;
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
import com.smakhorin.doodoo.FoodCard.FoodCard;
import com.smakhorin.doodoo.FoodCard.FoodCardAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private com.smakhorin.doodoo.FoodCard.FoodCardAdapter foodCardAdapter;
    int index = 0;
    private FirebaseAuth mAuth;

    List<FoodCard> foodCardItems;

    List<String> foodDb = new ArrayList<>(); // List of bg_food in Database
    HashMap<String,List<HashMap<String,String>>> foodData = new HashMap<>(); // Data for each bg_food on the list (Place - Price and one row for Image)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_back);

        mAuth = FirebaseAuth.getInstance();

        //checkUserSex();
        //getFoodFromDatabase();

        foodCardItems = new ArrayList<FoodCard>();

        foodCardAdapter = new FoodCardAdapter(this, R.layout.activity_main_win, foodCardItems);

        //Fill up each bg_food item_food
        DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
        mFirebase.child("Cached").child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Fill up each bg_food item_food
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    foodDb.add(childSnapshot.getKey());
                }
                fillUpDatabase();
                getFoodFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(foodCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                foodCardItems.remove(0);
                foodCardAdapter.notifyDataSetChanged();
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
                FoodCard card = (FoodCard)dataObject;
                String name = card.getName();
                List<HashMap<String, String>> items = foodData.get(name);
                if(items != null) {
                    HashMap<String, String> placeData = new HashMap<>();
                    for (HashMap<String, String> listItem : items) {
                        Set<String> keys = listItem.keySet();
                        String key = keys.iterator().next();
                        if (!key.equals("Image")) {
                            placeData.put(key, listItem.get(key));
                        }
                    }
                    Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();//
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("placeData", placeData);
                    startActivity(intent);
                } else {
                   Toast.makeText(MainActivity.this,"There are no places for that bg_food",Toast.LENGTH_LONG).show();
                }

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

    public void fillUpDatabase() {
        DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
        for (int i = 0; i < foodDb.size(); i++) {
            final String foodName = foodDb.get(i);
            mFirebase.child("Cached").child("Food").child(foodName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Fill up each bg_food item_food
                    List<HashMap<String,String>> item = new ArrayList<>();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        HashMap<String,String> temp = new HashMap<>();
                        temp.put(childSnapshot.getKey(),childSnapshot.getValue().toString());
                        item.add(temp);
                    }
                    foodData.put(foodName,item);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
                    getFoodFromDatabase();
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
                    getFoodFromDatabase();
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

    public void getFoodFromDatabase() {
        DatabaseReference foodDb = FirebaseDatabase.getInstance().getReference().child("Cached").child("Food");
        /*
//        foodDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.exists()) {
//                    for(String name : foodData.keySet()) {
//                        List<HashMap<String, String>> items = foodData.get(name);
//                        Integer medium = 0;
//                        Integer count = items.size()-1;
//                        String imageUrl = "";
//                        for(HashMap<String, String> listItem : items) {
//                            Set<String> keys = listItem.keySet();
//                            String key = keys.iterator().next();
//                            if(key.equals("Image")) {
//                                imageUrl = listItem.get(key);
//                            }
//                            else {
//                                medium += Integer.parseInt(listItem.get(key));
//                            }
//                        }
//                        medium /= count;
//                        foodCardItems.add(new FoodCard(name,count.toString(),medium.toString(),imageUrl));
//                    }
//                    foodCardAdapter.notifyDataSetChanged();
//                    index++;
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
*/
        foodDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(String name : foodData.keySet()) {
                        List<HashMap<String, String>> items = foodData.get(name);
                        Integer medium = 0;
                        Integer count = items.size()-1;
                        String imageUrl = "";
                        for(HashMap<String, String> listItem : items) {
                            Set<String> keys = listItem.keySet();
                            String key = keys.iterator().next();
                            if(key.equals("Image")) {
                                imageUrl = listItem.get(key);
                            }
                            else {
                                medium += Integer.parseInt(listItem.get(key));
                            }
                        }
                        medium /= count;
                        foodCardItems.add(new FoodCard(name,count.toString(),medium.toString(),imageUrl));
                    }
                    foodCardAdapter.notifyDataSetChanged();
                    index++;
                }
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
