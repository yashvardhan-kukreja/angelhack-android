package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {

    @SerializedName("coordinatorEmails")
    @Expose
    private List<String> coordinatorEmails = null;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("eventSessions")
    @Expose
    private List<EventSession> eventSessions = null;
    @SerializedName("faqs")
    @Expose
    private List<Faq> faqs = null;
    @SerializedName("speakers")
    @Expose
    private List<Speaker> speakers = null;
    @SerializedName("sponsors")
    @Expose
    private List<Sponsor> sponsors = null;
    @SerializedName("fees")
    @Expose
    private List<Fee> fees = null;
    @SerializedName("pointOfContacts")
    @Expose
    private List<PointOfContact> pointOfContacts = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("eventStartDate")
    @Expose
    private String eventStartDate;
    @SerializedName("eventEndDate")
    @Expose
    private String eventEndDate;
    @SerializedName("eventLocation")
    @Expose
    private String eventLocation;
    @SerializedName("eventLogoUrl")
    @Expose
    private String eventLogoUrl;
    @SerializedName("hostingOrganisation")
    @Expose
    private HostingOrganisation hostingOrganisation;
    @SerializedName("__v")
    @Expose
    private Integer v;

    protected Event(Parcel in) {
        coordinatorEmails = new ArrayList<String>();
        in.readList(coordinatorEmails, null);
        eventId = in.readString();
        about = in.readString();
        eventSessions = new ArrayList<EventSession>();
        in.readList(eventSessions, null);
        faqs = new ArrayList<Faq>();
        in.readList(faqs, null);
        speakers = new ArrayList<Speaker>();
        in.readList(speakers, null);
        sponsors = new ArrayList<Sponsor>();
        in.readList(sponsors, null);
        fees = new ArrayList<Fee>();
        in.readList(fees, null);
        pointOfContacts = new ArrayList<PointOfContact>();
        in.readList(pointOfContacts, null);
        id = in.readString();
        eventName = in.readString();
        eventStartDate = in.readString();
        eventEndDate = in.readString();
        eventLocation = in.readString();
        eventLogoUrl = in.readString();
        hostingOrganisation = in.readParcelable(HostingOrganisation.class.getClassLoader());
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readInt();
        }
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };


    public List<String> getCoordinatorEmails() {
        return coordinatorEmails;
    }

    public void setCoordinatorEmails(List<String> coordinatorEmails) {
        this.coordinatorEmails = coordinatorEmails;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<EventSession> getEventSessions() {
        return eventSessions;
    }

    public void setEventSessions(List<EventSession> eventSessions) {
        this.eventSessions = eventSessions;
    }

    public List<Faq> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<Faq> faqs) {
        this.faqs = faqs;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public List<Fee> getFees() {
        return fees;
    }

    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    public List<PointOfContact> getPointOfContacts() {
        return pointOfContacts;
    }

    public void setPointOfContacts(List<PointOfContact> pointOfContacts) {
        this.pointOfContacts = pointOfContacts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventLogoUrl() {
        return eventLogoUrl;
    }

    public void setEventLogoUrl(String eventLogoUrl) {
        this.eventLogoUrl = eventLogoUrl;
    }

    public HostingOrganisation getHostingOrganisation() {
        return hostingOrganisation;
    }

    public void setHostingOrganisation(HostingOrganisation hostingOrganisation) {
        this.hostingOrganisation = hostingOrganisation;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(coordinatorEmails);
        dest.writeString(eventId);
        dest.writeString(about);
        dest.writeList(eventSessions);
        dest.writeList(faqs);
        dest.writeList(speakers);
        dest.writeList(sponsors);
        dest.writeList(fees);
        dest.writeList(pointOfContacts);
        dest.writeString(id);
        dest.writeString(eventName);
        dest.writeString(eventStartDate);
        dest.writeString(eventEndDate);
        dest.writeString(eventLocation);
        dest.writeString(eventLogoUrl);
        dest.writeParcelable(hostingOrganisation, flags);
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(v);
        }
    }
}
