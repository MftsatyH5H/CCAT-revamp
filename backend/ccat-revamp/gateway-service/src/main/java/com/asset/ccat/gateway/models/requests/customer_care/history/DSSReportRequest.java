package com.asset.ccat.gateway.models.requests.customer_care.history;

import com.asset.ccat.gateway.models.requests.SubscriberRequest;
import com.asset.ccat.gateway.models.shared.PaginationModel;

/**
 * @author wael.mohamed
 */
public class DSSReportRequest extends SubscriberRequest {

    private Long dateFrom;
    private Long dateTo;

    private Integer flag;
    private PaginationModel pagination;

    public Long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
        this.dateTo = dateTo;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DSSReportRequest{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", flag=" + flag +
                ", msisdn='" + msisdn + '\'' +
                '}';
    }
}
