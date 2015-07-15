package com.example.brich200.mobileideasportal;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

public class Idea {

    private static Idea instance;
    //private final Context context;

    private String title, tags, issue, description, customerExperienceImpact,
            metricsImpact, email, additionalTeamMemberEmail, lastModified, organization, imageIds;
    private int status, intelectualPropertyStatus, id, upvotes, downvotes, views;
    private ArrayList<byte []> arrayOfImages;
    private View view;

    public String getTitle(){return title;}
    public String getTags(){return tags;}
    public String getIssue(){return issue;}
    public String getDescription(){return description;}
    public String getCustomerExperienceImpact(){return customerExperienceImpact;}
    public String getMetricsImpact(){return metricsImpact;}
    public int getStatus(){return status;}
    public int getIntelectualPropertyStatus(){return intelectualPropertyStatus;}
    public String getEmail(){return email;}
    public String getAdditionalTeamMemberEmail(){return additionalTeamMemberEmail;}
    public int getId(){return id;}
    public int getUpVotes(){return upvotes;}
    public int getDownVotes(){return downvotes;}
    public String getLastModified(){return lastModified;}
    public int getViews(){return views;}
    public String getOrganization(){return organization;}
    public ArrayList<byte []> getArrayOfImages(){return arrayOfImages;}
    public View getView(){return view;}
    public String getImageIds(){return imageIds;}

    public void setTitle(String title) {this.title = title;}
    public void setTags(String tags) {this.tags = tags;}
    public void setIssue(String issue) {this.issue = issue;}
    public void setDescription(String description) {this.description = description;}
    public void setCustomerExperienceImpact(String customerExperienceImpact) {this.customerExperienceImpact = customerExperienceImpact;}
    public void setMetricsImpact(String metricsImpact) {this.metricsImpact = metricsImpact;}
    public void setStatus(int status) {this.status = status;}
    public void setIntelectualPropertyStatus(int intelectualPropertyStatus) {this.intelectualPropertyStatus = intelectualPropertyStatus;}
    public void setEmail(String email) {this.email = email;}
    public void setAdditionalTeamMemberEmail(String additionalTeamMemberEmail) {this.additionalTeamMemberEmail = additionalTeamMemberEmail;}
    public void setId(int id) {this.id = id;}
    public void setUpvotes(int upvotes) {this.upvotes = upvotes;}
    public void setDownvotes(int downvotes) {this.downvotes = downvotes;}
    public void setLastModified(String lastModified) {this.lastModified = lastModified;}
    public void setViews(int views) {this.views = views;}
    public void setOrganization(String organization) {this.organization = organization;}
    public void setArrayOfImages(ArrayList<byte []> arrayOfImages){this.arrayOfImages = arrayOfImages;}
    public void setView(View view) {this.view = view;}
    public void setImageIds(String imageIds) {this.imageIds = imageIds;}

    /*public static synchronized Idea getInstance(Context context){

        if(instance == null) {
            instance = new Idea(context);
        }

        return instance;
    }*/

    public Idea() {
        setTitle("");
        setTags("");
        setIssue("");
        setDescription("");
        setCustomerExperienceImpact("");
        setMetricsImpact("");
        setStatus(-1);
        setIntelectualPropertyStatus(-1);
        setEmail("");
        setAdditionalTeamMemberEmail("");
        setId(-1);
        setUpvotes(-1);
        setDownvotes(-1);
        setLastModified(null);
        setViews(0);
        setOrganization("");
        setArrayOfImages(null);
        setView(null);
        setImageIds("");
        //this.context = context;
    }

    public Idea(String title, String tags, String issue, String description, String customerExperienceImpact,
                String metricsImpact, int status, int intelectualPropertyStatus, String email, String additionalTeamMemberEmail,
                int id, int votes, String lastModified, int views, String organization){
        setTitle(title);
        setTags(tags);
        setIssue(issue);
        setDescription(description);
        setCustomerExperienceImpact(customerExperienceImpact);
        setMetricsImpact(metricsImpact);
        setStatus(status);
        setIntelectualPropertyStatus(intelectualPropertyStatus);
        setEmail(email);
        setAdditionalTeamMemberEmail(additionalTeamMemberEmail);
        setId(id);
        setUpvotes(votes);
        //setDownvotes(downvotes);
        setLastModified(lastModified);
        setViews(views);
        setOrganization(organization);
        //this.context = context;
    }

    public Idea(Idea database) {
        setTitle(database.title);
        setTags(database.tags);
        setIssue(database.issue);
        setDescription(database.description);
        setCustomerExperienceImpact(database.customerExperienceImpact);
        setMetricsImpact(database.metricsImpact);
        setStatus(database.status);
        setIntelectualPropertyStatus(database.intelectualPropertyStatus);
        setEmail(database.email);
        setAdditionalTeamMemberEmail(database.additionalTeamMemberEmail);
        setId(database.id);
        setUpvotes(database.upvotes);
        setDownvotes(database.downvotes);
        setLastModified(database.lastModified);
        setViews(database.getViews());
        setOrganization(database.getOrganization());
        setArrayOfImages(database.getArrayOfImages());
        setView(database.getView());
        setImageIds(database.getImageIds());
        //this.context = context;
    }
}