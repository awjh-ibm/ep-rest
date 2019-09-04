package com.wetrade.eprest;

import java.util.Collection;

import com.wetrade.assets.FinanceRequest;
import com.wetrade.assets.FinanceRequestGroup;

import org.json.JSONObject;

public interface FinanceRequestService {
    public Collection<FinanceRequest> getFinanceRequests(String behalfOfId) throws Exception;

    public FinanceRequest getFinanceRequest(String id) throws Exception;

    public FinanceRequest getFinanceRequestByHash(String hash) throws Exception;

    public Collection<FinanceRequest> getFinanceRequestsByGroupHash(String hash) throws Exception;

    public FinanceRequestGroup createFinanceRequest(JSONObject financeRequest) throws Exception;

    public void acceptFinanceRequest(String id) throws Exception;

    public void withdrawFinanceRequest(String id) throws Exception;
}
