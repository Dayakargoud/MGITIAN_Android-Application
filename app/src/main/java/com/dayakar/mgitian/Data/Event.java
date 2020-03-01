package com.dayakar.mgitian.Data;

import androidx.annotation.Keep;

import java.util.Comparator;

@Keep
public class Event implements Comparable<Event>{
    private String title,desc,image,contact_info,reg_fee,venue,branch,postId,time,postedBy;
    private Long postedTime;
    public Event(){

    }
    public Event(String title, String desc, String image, String contact_info, String reg_fee,
                 String venue, String branch, String id,String time,String postedBy,Long postedTime) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.contact_info = contact_info;
        this.reg_fee = reg_fee;
        this.venue = venue;
        this.branch = branch;
        this.postId =id;
        this.time=time;
        this.postedBy=postedBy;
        this.postedTime=postedTime;
    }

    public Long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Long postedTime) {
        this.postedTime = postedTime;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        if(postedBy!=null)
        this.postedBy = postedBy;else this.postedBy="Unknown";
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReg_fee() {
        return reg_fee;
    }

    public void setReg_fee(String reg_fee) {
        this.reg_fee = reg_fee;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public int compareTo(Event o) {

        return 0;
    }
    public static Comparator<Event> sortByLatest=new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            Long t1=o1.getPostedTime();
            Long t2=o2.getPostedTime();
            return t2.compareTo(t1);
        }
    };
}
