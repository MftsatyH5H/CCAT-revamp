package com.asset.ccat.air_service.models.requests.pam_info;

import com.asset.ccat.air_service.models.requests.BaseRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 *
 * @author nour.ihab
 */
public class AddPamInformationRequest extends BaseRequest {

    private String msisdn;
    private Integer pamId;
    private Integer pamClassId;
    private Integer pamScheduleId;
    private Integer currentPamPeriodId;
    private Date deferredToDate;
    private Integer pamServicePriorityId;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getPamId() {
        return pamId;
    }

    public void setPamId(Integer pamId) {
        this.pamId = pamId;
    }

    public Integer getPamClassId() {
        return pamClassId;
    }

    public void setPamClassId(Integer pamClassId) {
        this.pamClassId = pamClassId;
    }

    public Integer getPamScheduleId() {
        return pamScheduleId;
    }

    public void setPamScheduleId(Integer pamScheduleId) {
        this.pamScheduleId = pamScheduleId;
    }

    public Integer getCurrentPamPeriodId() {
        return currentPamPeriodId;
    }

    public void setCurrentPamPeriodId(Integer currentPamPeriodId) {
        this.currentPamPeriodId = currentPamPeriodId;
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public Date getDeferredToDate() {
        return deferredToDate;
    }

    public void setDeferredToDate(Date deferredToDate) {
        this.deferredToDate = deferredToDate;
    }

    public Integer getPamServicePriorityId() {
        return pamServicePriorityId;
    }

    public void setPamServicePriorityId(Integer pamServicePriorityId) {
        this.pamServicePriorityId = pamServicePriorityId;
    }

    @Override
    public String toString() {
        return "AddPamInformationRequest{" +
                "msisdn='" + msisdn + '\'' +
                ", pamId=" + pamId +
                ", pamClassId=" + pamClassId +
                ", pamScheduleId=" + pamScheduleId +
                ", currentPamPeriodId=" + currentPamPeriodId +
                ", deferredToDate=" + deferredToDate +
                ", pamServicePriorityId=" + pamServicePriorityId +
                '}';
    }
}
