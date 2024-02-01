package com.asset.ccat.gateway.models.requests.admin.dynamic_page;

import com.asset.ccat.gateway.models.admin.dynamic_page.DynamicPageModel;
import com.asset.ccat.gateway.models.requests.BaseRequest;

/**
 * @author assem.hassan
 */
public class UpdateDynamicPageRequest extends BaseRequest {

    private Integer pageId;
    private Integer privilegeId;
    private String pageName;
    private Boolean requireSubscriber;

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Boolean getRequireSubscriber() {
        return requireSubscriber;
    }

    public void setRequireSubscriber(Boolean requireSubscriber) {
        this.requireSubscriber = requireSubscriber;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Override
    public String toString() {
        return "UpdateDynamicPageRequest{" +
                "pageId=" + pageId +
                ", privilegeId=" + privilegeId +
                ", pageName='" + pageName + '\'' +
                ", requireSubscriber=" + requireSubscriber +
                '}';
    }
}
