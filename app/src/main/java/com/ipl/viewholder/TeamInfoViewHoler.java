package com.ipl.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipl.R;

/**
 * Created by bridgeit on 2/6/16.
 */

public class TeamInfoViewHoler extends RecyclerView.ViewHolder {
    public TextView teamName, teamCoach, teamCaptain, teamHomeVenue, teamOwner;
    public ImageView teamLogo;

    public TeamInfoViewHoler(View itemView) {
        super(itemView);
        teamName = (TextView) itemView.findViewById(R.id.team_name);
        teamCoach = (TextView) itemView.findViewById(R.id.coachName);
        teamCaptain = (TextView) itemView.findViewById(R.id.captianName);
        teamHomeVenue = (TextView) itemView.findViewById(R.id.venueName);
        teamOwner = (TextView) itemView.findViewById(R.id.teamOwnerName);
        teamLogo = (ImageView) itemView.findViewById(R.id.team_logo);
    }
}
