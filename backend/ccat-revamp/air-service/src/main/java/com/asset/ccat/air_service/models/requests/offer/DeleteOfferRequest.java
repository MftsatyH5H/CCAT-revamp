/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.ccat.air_service.models.requests.offer;

import com.asset.ccat.air_service.models.requests.BaseRequest;

/**
 * @author wael.mohamed
 */
public class DeleteOfferRequest extends BaseRequest {

    private String msisdn;
    private Integer offerId;

    public DeleteOfferRequest() {
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "DeleteOfferRequest{" +
                "msisdn='" + msisdn + '\'' +
                ", offerId=" + offerId +
                '}';
    }
}
