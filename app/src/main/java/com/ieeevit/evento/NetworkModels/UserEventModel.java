package com.ieeevit.evento.networkmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEventModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_coordinator")
    @Expose
    private Boolean isCoordinator;
    @SerializedName("encrypted_id")
    @Expose
    private String encryptedId;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsCoordinator() {
        return isCoordinator;
    }

    public void setIsCoordinator(Boolean isCoordinator) {
        this.isCoordinator = isCoordinator;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }
}
