package com.ieeevit.evento.networkmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ieeevit.evento.classes.User;

public class UserModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
