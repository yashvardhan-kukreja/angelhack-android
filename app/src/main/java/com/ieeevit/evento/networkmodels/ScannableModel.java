package com.ieeevit.evento.networkmodels;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ieeevit.evento.classes.Scannable;

public class ScannableModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("scannables")
    @Expose
    private List<Scannable> scannables = null;

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

    public List<Scannable> getScannables() {
        return scannables;
    }

    public void setScannables(List<Scannable> scannables) {
        this.scannables = scannables;
    }

}
