package com.example.progass2;

import java.util.Date;

public class Access {
    private long accessId;
    private long profileId;
    private String accessType;
    private Date timestamp;

    public Access(long accessId, long profileId, String accessType, Date timestamp) {
        this.accessId = accessId;
        this.profileId = profileId;
        this.accessType = accessType;
        this.timestamp = timestamp;
    }

    public long getAccessId() {
        return accessId;
    }

    public void setAccessId(long accessId) {
        this.accessId = accessId;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
