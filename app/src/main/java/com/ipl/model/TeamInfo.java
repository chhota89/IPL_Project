package com.ipl.model;

import java.io.Serializable;

/**
 * Created by bridgeit on 2/6/16.
 */

public class TeamInfo implements Serializable {

    private String team_name;

    private String team_coach;

    private String team_captain;

    private String team_home_venue;

    private String team_owner;

    private String team_img_url;

    /**
     * @return The team_name
     */
    public String getTeam_name() {
        return team_name;
    }

    /**
     * @param team_name The team_name
     */
    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }


    /**
     * @param teamCoach The team_coach
     */
    public void setTeamCoach(String teamCoach) {
        this.team_coach = teamCoach;
    }

    /**
     * @return The team_captain
     */
    public String getTeam_captain() {
        return team_captain;
    }

    /**
     * @param team_captain The team_captain
     */
    public void setTeam_captain(String team_captain) {
        this.team_captain = team_captain;
    }

    /**
     * @return The team_home_venue
     */
    public String getTeam_home_venue() {
        return team_home_venue;
    }

    /**
     * @param team_home_venue The team_home_venue
     */
    public void setTeam_home_venue(String team_home_venue) {
        this.team_home_venue = team_home_venue;
    }

    /**
     * @return The team_owner
     */
    public String getTeam_owner() {
        return team_owner;
    }

    /**
     * @param team_owner The team_owner
     */
    public void setTeam_owner(String team_owner) {
        this.team_owner = team_owner;
    }

    public String getTeam_coach() {
        return team_coach;
    }

    public void setTeam_coach(String team_coach) {
        this.team_coach = team_coach;
    }

    public String getTeam_img_url() {
        return team_img_url;
    }

    public void setTeam_img_url(String team_img_url) {
        this.team_img_url = team_img_url;
    }
}
