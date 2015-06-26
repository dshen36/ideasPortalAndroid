package com.example.brich200.mobileideasportal;

import android.content.Context;

public class Idea {

    private static Idea instance;
    private final Context context;

    private String title, tags, issue, description, customerExperienceImpact,
            metricsImpact, email, additionalTeamMemberEmail;
    private int status, intelectualPropertyStatus, id, upvotes, downvotes;

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

    public static synchronized Idea getInstance(Context context){

        if(instance == null) {
            instance = new Idea(context);
        }

        return instance;
    }

    public Idea(Context context) {
        setTitle("");
        setTags("");
        setIssue("");
        setDescription("");
        setCustomerExperienceImpact("");
        setMetricsImpact("");
        setStatus(0);
        setIntelectualPropertyStatus(0);
        setEmail("");
        setAdditionalTeamMemberEmail("");
        setId(0);
        setUpvotes(0);
        setDownvotes(0);
        this.context = context;
    }

    public Idea(Context context, String title, String tags, String issue, String description, String customerExperienceImpact,
                String metricsImpact, int status, int intelectualPropertyStatus, String additionalTeamMemberEmail,
                int id, int upvotes, int downvotes){
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
        setUpvotes(upvotes);
        setDownvotes(downvotes);
        this.context = context;
    }

    public Idea(Idea database, Context context) {
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
        this.context = context;
    }
}