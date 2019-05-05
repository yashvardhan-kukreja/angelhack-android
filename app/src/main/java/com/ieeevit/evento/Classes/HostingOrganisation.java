package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HostingOrganisation implements Parcelable{

    @SerializedName("authorized")
    @Expose
    private Boolean authorized;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("orgName")
    @Expose
    private String orgName;
    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("concernedEmail")
    @Expose
    private String concernedEmail;
    @SerializedName("concernedContact")
    @Expose
    private String concernedContact;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("orgLogoUrl")
    @Expose
    private String orgLogoUrl;

    HostingOrganisation(Parcel in) {
        byte tmpAuthorized = in.readByte();
        authorized = tmpAuthorized == 0 ? null : tmpAuthorized == 1;
        id = in.readString();
        orgName = in.readString();
        college = in.readString();
        concernedEmail = in.readString();
        concernedContact = in.readString();
        password = in.readString();
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readInt();
        }
        orgLogoUrl = in.readString();
    }

    public static final Creator<HostingOrganisation> CREATOR = new Creator<HostingOrganisation>() {
        @Override
        public HostingOrganisation createFromParcel(Parcel in) {
            return new HostingOrganisation(in);
        }

        @Override
        public HostingOrganisation[] newArray(int size) {
            return new HostingOrganisation[size];
        }
    };

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getConcernedEmail() {
        return concernedEmail;
    }

    public void setConcernedEmail(String concernedEmail) {
        this.concernedEmail = concernedEmail;
    }

    public String getConcernedContact() {
        return concernedContact;
    }

    public void setConcernedContact(String concernedContact) {
        this.concernedContact = concernedContact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getOrgLogoUrl() {
        return orgLogoUrl;
    }

    public void setOrgLogoUrl(String orgLogoUrl) {
        this.orgLogoUrl = orgLogoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (authorized == null ? 0 : authorized ? 1 : 2));
        dest.writeString(id);
        dest.writeString(orgName);
        dest.writeString(college);
        dest.writeString(concernedEmail);
        dest.writeString(concernedContact);
        dest.writeString(password);
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(v);
        }
        dest.writeString(orgLogoUrl);
    }
}