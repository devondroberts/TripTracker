package org.pltw.examples.triptracker;

import java.util.Date;

public class Trip
{
    private String name;
    private String objectId;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean shared;

    public static final String EXTRA_TRIP_ID="";
    public static final String EXTRA_TRIP_PUBLIC_VIEW ="";
    public static final String EXTRA_TRIP_NAME = "";
    public static final String EXTRA_TRIP_DESC = "";
    public static final String EXTRA_TRIP_START_DATE ="";
    public static final String EXTRA_TRIP_END_DATE = "";
    public static final String EXTRA_TRIP_PUBLIC = "";

    public Trip() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
