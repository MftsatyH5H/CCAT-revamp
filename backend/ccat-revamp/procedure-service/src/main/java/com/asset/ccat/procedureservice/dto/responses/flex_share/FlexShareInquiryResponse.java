package com.asset.ccat.procedureservice.dto.responses.flex_share;

import com.asset.ccat.procedureservice.constants.FlexType;

public class FlexShareInquiryResponse {
    private FlexType flexType;
    private String parameterOut;

    public FlexShareInquiryResponse() {
    }

    public FlexShareInquiryResponse(FlexType flexType, String parameterOut) {
        this.flexType = flexType;
        this.parameterOut = parameterOut;
    }

    public FlexType getFlexType() {
        return flexType;
    }

    public void setFlexType(FlexType flexType) {
        this.flexType = flexType;
    }

    public String getParameterOut() {
        return parameterOut;
    }

    public void setParameterOut(String parameterOut) {
        this.parameterOut = parameterOut;
    }
}
