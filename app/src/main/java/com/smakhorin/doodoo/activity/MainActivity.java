package com.smakhorin.doodoo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.smakhorin.doodoo.R;
import com.smakhorin.doodoo.foodcard.FoodCard;
import com.smakhorin.doodoo.foodcard.FoodCardAdapter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private com.smakhorin.doodoo.foodcard.FoodCardAdapter foodCardAdapter;
    int index = 0;
    private FirebaseAuth mAuth;

    List<FoodCard> foodCardItems;
    List<FoodCard> foodCardsCache = new ArrayList<>();

    List<String> foodDb = new ArrayList<>(); // List of Food names in Database
    HashMap<String,List<HashMap<String,String>>> foodData = new HashMap<>(); // Data for each food on the list (Place - Price and one row for Image)

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
                    //TODO: Rewrite this whole structure in one DB call and put it into DAO
                    Object x = childSnapshot.getValue();
                    int z = 4;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.action_refresh:
                foodCardItems.addAll(foodCardsCache);
                Collections.shuffle(foodCardItems);
                foodCardAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void getFoodFromDatabase() {
        DatabaseReference foodDb = FirebaseDatabase.getInstance().getReference().child("Cached").child("Food");

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
                    foodCardsCache.addAll(foodCardItems);
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

}
