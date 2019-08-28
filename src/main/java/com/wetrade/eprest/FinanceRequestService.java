package com.wetrade.eprest;

import java.util.Collection;

import com.wetrade.assets.FinanceRequest;

import org.json.JSONObject;

public interface FinanceRequestService {
    public Collection<FinanceRequest> getFinanceRequests(String behalfOfId) throws Exception;

    public FinanceRequest getFinanceRequest(String id) throws Exception;

    public FinanceRequest getFinanceRequestByHash(String hash) throws Exception;

    public FinanceRequest[] createFinanceRequest(JSONObject financeRequest) throws Exception;

    public void approveFinanceRequest(String id) throws Exception;

    public void rejectFinanceRequest(String id) throws Exception;

    public void acceptFinanceRequest(String id) throws Exception;
}
