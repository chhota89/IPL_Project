package com.ipl.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipl.R;

/**
 * Created by bridgeit on 4/6/16.
 */

public class PlayerInfoViewHolder extends RecyclerView.ViewHolder {

    public TextView playerName, playerRole, battingStyle, bowlingStyle, nationality, dob;
    public ImageView playerImage;

    public PlayerInfoViewHolder(View itemView) {
        super(itemView);
        playerName = (TextView) itemView.findViewById(R.id.player_name);
        playerRole = (TextView) itemView.findViewById(R.id.player_role);
        battingStyle = (TextView) itemView.findViewById(R.id.player_batting_style);
        bowlingStyle = (TextView) itemView.findViewById(R.id.player_bowling_style);
        nationality = (TextView) itemView.findViewById(R.id.player_nationality);
        dob = (TextView) itemView.findViewById(R.id.player_dob);
        playerImage = (ImageView) itemView.findViewById(R.id.player_image_view);
    }
}
