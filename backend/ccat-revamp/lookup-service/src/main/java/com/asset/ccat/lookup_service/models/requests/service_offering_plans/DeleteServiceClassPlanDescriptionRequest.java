package com.asset.ccat.lookup_service.models.requests.service_offering_plans;


import com.asset.ccat.lookup_service.models.requests.BaseRequest;

public class DeleteServiceClassPlanDescriptionRequest extends BaseRequest {

    private Integer serviceClassId;

    private Integer planId;

    public DeleteServiceClassPlanDescriptionRequest(Integer serviceClassId, Integer planId) {
        this.serviceClassId = serviceClassId;
        this.planId = planId;
    }

    public DeleteServiceClassPlanDescriptionRequest() {
    }

    public Integer getServiceClassId() {
        return serviceClassId;
    }

    public void setServiceClassId(Integer serviceClassId) {
        this.serviceClassId = serviceClassId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return "DeleteServiceClassPlanDescriptionRequest{" +
                "classId=" + serviceClassId +
                ", planId=" + planId +
                '}';
    }
}
