package com.ipl.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ipl.R;
import com.ipl.constant.ProjectConstant;
import com.ipl.model.PlayerInfo;
import com.ipl.model.TeamInfo;
import com.ipl.utility.FireBaseEvent;
import com.ipl.utility.PictureUtil;
import com.ipl.viewholder.PlayerInfoViewHolder;

import java.io.FileNotFoundException;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class PlayerView extends AppCompatActivity {
    public static String TAG = "TeamView";
    CollapsingToolbarLayout collapsingToolbar;
    FirebaseRecyclerAdapter adapter;
    StorageReference storageRef;
    private DatabaseReference mRef;
    private Query mQuery;
    private String mTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_player_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toolbar back button enabled
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        //Receiving team information
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            TeamInfo teamInfo = (TeamInfo) bundle.getSerializable(ProjectConstant.TEAM_KEY);
            ImageView teamLogo = (ImageView) findViewById(R.id.team_logo);
            mTeamName = teamInfo.getTeam_name().replaceAll("\\s+", "");
            toolbarTextAppernce();
            collapsingToolbar.setTitle(teamInfo.getTeam_name());
            try {
                //Setting toolbar image
                Bitmap bitmap = PictureUtil.loadFileFromLocalStorage(ProjectConstant.TEAM_LOGO, teamInfo.getTeam_name());
                teamLogo.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            //Creating the reference of Firebase Data base
            mRef = FirebaseDatabase.getInstance().getReference();
            mRef.keepSynced(true);

            //setting child to the DataBase Reference
            mRef = mRef.child(mTeamName);

            //setting quary ro get only last 50 details.
            mQuery = mRef.limitToLast(50);

            //Setting adapter with mQuery reference
            adapter = new FirebaseRecyclerAdapter<PlayerInfo, PlayerInfoViewHolder>(PlayerInfo.class, R.layout.adapter_player_view, PlayerInfoViewHolder.class, mQuery) {
                @Override
                public void populateViewHolder(final PlayerInfoViewHolder playerInfoViewHolder, final PlayerInfo playerInfo, int position) {

                    playerInfoViewHolder.playerName.setText(playerInfo.getPlayer_name());
                    playerInfoViewHolder.playerRole.setText(playerInfo.getPlayer_role());
                    playerInfoViewHolder.battingStyle.setText(playerInfo.getPlayer_batting_style());
                    playerInfoViewHolder.bowlingStyle.setText(playerInfo.getPlayer_bowling_style());
                    playerInfoViewHolder.dob.setText(playerInfo.getPlayer_dob());
                    playerInfoViewHolder.nationality.setText(playerInfo.getPlayer_nationality());

                    //Try to load image from local storage
                    PictureUtil.LoadImageAsync loadImageAsync = new PictureUtil.LoadImageAsync() {
                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            if (bitmap != null)
                                playerInfoViewHolder.playerImage.setImageBitmap(bitmap);
                            else {
                                //Image is not present in file system.
                                //Download Image from the google cloud
                                storageRef = storageRef.getRoot();
                                storageRef = storageRef.child(playerInfo.getPlayer_img_url());
                                //Downloading image as byte array
                                storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        playerInfoViewHolder.playerImage.setImageBitmap(bitmap);

                                        //Store Image to the persistence storage
                                        PictureUtil.storeImageInFile(bitmap, mTeamName, playerInfo.getPlayer_name());

                                        //Send event to the firebase.
                                        FireBaseEvent.sendImageDownloadEvent(PlayerView.this, playerInfo.getPlayer_name(), mTeamName);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.e(TAG, "onFailure: ", exception);
                                        // Send Crash reports to the firebase analytics
                                        FirebaseCrash.log(exception.getMessage());
                                        FirebaseCrash.report(exception);
                                    }
                                });
                            }
                        }
                    };
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        loadImageAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mTeamName, playerInfo.getPlayer_name());
                    else
                        loadImageAsync.execute(mTeamName, playerInfo.getPlayer_name());
                }
            };

            //Setting recycle view
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.playerRecycleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);

            // Connect the recycler to the scroller (to let the scroller scroll the list)
            fastScroller.setRecyclerView(recyclerView);

            // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
            recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

            //Setting recycle view animation
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
            alphaAdapter.setInterpolator(new OvershootInterpolator());
            alphaAdapter.setInterpolator(new OvershootInterpolator(.5f));

            //setting adapter to recycle view
            recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
    }

    //Applay palette color
    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
    }

    //setting toolbar text apperence
    private void toolbarTextAppernce() {
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }


}
