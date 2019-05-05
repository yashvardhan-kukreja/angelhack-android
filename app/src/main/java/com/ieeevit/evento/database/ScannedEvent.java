package com.ieeevit.evento.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "scanned_events")
class ScannedEvent {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "event_id")
    private String eventId;
    @ColumnInfo(name = "event_name")
    private String eventName;
    @ColumnInfo(name = "hosting_organisation")
    private String hostingOrganisation;

    @Ignore
    public ScannedEvent(String eventId, String eventName, String hostingOrganisation) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.hostingOrganisation = hostingOrganisation;
    }

    public ScannedEvent(int id, String eventId, String eventName, String hostingOrganisation) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.hostingOrganisation = hostingOrganisation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHostingOrganisation() {
        return hostingOrganisation;
    }

    public void setHostingOrganisation(String hostingOrganisation) {
        this.hostingOrganisation = hostingOrganisation;
    }
}
