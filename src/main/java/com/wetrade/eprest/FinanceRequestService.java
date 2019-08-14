package com.wetrade.eprest;

import java.util.Collection;

import com.wetrade.assets.FinanceRequest;

public interface FinanceRequestService {
    public Collection<FinanceRequest> getFinanceRequests() throws Exception;

    public FinanceRequest getFinanceRequest(String id) throws Exception;

    public Collection<FinanceRequest> getFinanceRequestsByGroupId(String groupId) throws Exception;
}
