package com.ipl.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ipl.R;
import com.ipl.constant.ProjectConstant;
import com.ipl.model.TeamInfo;
import com.ipl.utility.FireBaseEvent;
import com.ipl.utility.PictureUtil;
import com.ipl.viewholder.TeamInfoViewHoler;

import java.io.FileNotFoundException;

public class TeamView extends AppCompatActivity {

    public static String TAG = "TeamView";
    FirebaseRecyclerAdapter adapter;
    StorageReference storageRef;
    private DatabaseReference mRef;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        storageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_team_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "InstanceID token: .................. " + FirebaseInstanceId.getInstance().getToken());

        //Creating the reference of Firebase Data base
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);

        //setting child to the DataBase Reference
        mRef = mRef.child("tean_info");

        //setting quary ro get only last 50 details.
        mQuery = mRef.limitToLast(50);

        //Setting adapter with mQuery reference
        adapter = new FirebaseRecyclerAdapter<TeamInfo, TeamInfoViewHoler>(TeamInfo.class, R.layout.adapter_team_view, TeamInfoViewHoler.class, mQuery) {
            @Override
            public void populateViewHolder(final TeamInfoViewHoler teamInfoViewHoler, final TeamInfo teamInfo, int position) {

                teamInfoViewHoler.teamLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeamView.this, PlayerView.class);
                        // Put team  information in the intent
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ProjectConstant.TEAM_KEY, teamInfo);
                        intent.putExtras(bundle);

                        //Adding activity transition for api 21 and +
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(TeamView.this, (View)teamInfoViewHoler.teamLogo, "TeamImage");

                        //Starting Player view class
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                            startActivity(intent,options.toBundle());
                        else
                            startActivity(intent);
                    }
                });

                teamInfoViewHoler.teamName.setText(teamInfo.getTeam_name());
                teamInfoViewHoler.teamCaptain.setText(teamInfo.getTeam_captain());
                teamInfoViewHoler.teamCoach.setText(teamInfo.getTeam_coach());
                teamInfoViewHoler.teamOwner.setText(teamInfo.getTeam_owner());
                teamInfoViewHoler.teamHomeVenue.setText(teamInfo.getTeam_home_venue());


                //Try to load image from local storage
                try {
                    teamInfoViewHoler.teamLogo.setImageBitmap(PictureUtil.loadFileFromLocalStorage(ProjectConstant.TEAM_LOGO, teamInfo.getTeam_name()));
                } catch (FileNotFoundException exception) {
                    Log.e(TAG, "populateViewHolder: ", exception);

                    storageRef = storageRef.getRoot();
                    storageRef = storageRef.child("teamlogo" + "/" + teamInfo.getTeam_img_url());
                    //Downloading image as byte array
                    storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            teamInfoViewHoler.teamLogo.setImageBitmap(bitmap);

                            //Store Image to the persistence storage
                            PictureUtil.storeImageInFile(bitmap, ProjectConstant.TEAM_LOGO, teamInfo.getTeam_name());

                            //Send event to the firebase.
                            FireBaseEvent.sendImageDownloadEvent(TeamView.this, teamInfo.getTeam_name(), "Team Logo");

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

        //Setting recycle view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.playerRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setting adapter to recycle view
        recyclerView.setAdapter(adapter);
    }
}
