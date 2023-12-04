package com.asset.ccat.lookup_service.models.requests.call_activity_admin;

import com.asset.ccat.lookup_service.constants.ActivityType;

import com.asset.ccat.lookup_service.models.requests.BaseRequest;

public class GetAllActivitiesWithTypeRequest extends BaseRequest {

    private ActivityType activityType;

    private Integer parentId;

    @Override
    public String toString() {
        return "GetAllActivitiesWithTypeRequest{" +
                "activityType='" + activityType + '\'' +
                ", parentId=" + parentId +
                '}';
    }

    public Enum<ActivityType> getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
