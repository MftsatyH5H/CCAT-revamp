/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.ccat.air_service.models.responses.offer;

import com.asset.ccat.air_service.models.OfferModel;
import java.util.List;

/**
 *
 * @author wael.mohamed
 */
public class GetAllOffersResponse {

    List<OfferModel> offers;

    public GetAllOffersResponse() {
    }

    public GetAllOffersResponse(List<OfferModel> offers) {
        this.offers = offers;
    }

    public List<OfferModel> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferModel> offers) {
        this.offers = offers;
    }
}
