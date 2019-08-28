package com.wetrade.eprest;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.assets.FinanceRequest;
import com.wetrade.common.FabricProxy;
import com.wetrade.common.FabricProxyConfig;
import com.wetrade.common.FabricProxyException;

import org.json.JSONObject;

public class FinanceRequestServiceFabricImpl implements FinanceRequestService {
    private String subContractName = "FinanceRequestContract";
    private FabricProxy proxy;
    String format = "EEE MMM d HH:mm:ss Z yyy";
    private Gson gson = new GsonBuilder().setDateFormat(format).create();

    public FinanceRequestServiceFabricImpl(FabricProxyConfig config) throws FabricProxyException {
        this.proxy = new FabricProxy(config);
    }

    @Override
    public void acceptFinanceRequest(String id) throws Exception {
        this.proxy.submitTransaction("admin", subContractName, "acceptFinanceRequest");
    }

    @Override
    public void approveFinanceRequest(String id) throws Exception {
        this.proxy.submitTransaction("admin", subContractName, "approveFinanceRequest");
    }

    @Override
    public FinanceRequest[] createFinanceRequest(JSONObject financeRequest) throws Exception {
        String requesterId = financeRequest.getString("requesterId");
        String financierIds = financeRequest.getJSONArray("financierIds").toString();
        String purchaseOrderId = financeRequest.getString("purchaseOrderId");
        String amount = Double.toString(financeRequest.getDouble("amount"));
        String interest = Double.toString(financeRequest.getDouble("interest"));
        String monthLength = Integer.toString(financeRequest.getInt("monthLength"));

        String fcn = "createFinanceRequest";
        String response = this.proxy.submitTransaction("admin", subContractName, fcn, requesterId, financierIds, purchaseOrderId, amount, interest, monthLength);

        return gson.fromJson(response, FinanceRequest[].class);
    }

    @Override
    public FinanceRequest getFinanceRequest(String id) throws Exception {
        String fcn = "getFinanceRequest";
        String response = this.proxy.evaluateTransaction("admin", subContractName, fcn, new String[]{id});

        FinanceRequest request = gson.fromJson(response, FinanceRequest.class);
        return request;
    }

    @Override
    public FinanceRequest getFinanceRequestByHash(String hash) throws Exception {
        return null;
    }

    @Override
    public Collection<FinanceRequest> getFinanceRequests(String behalfOfId) throws Exception {
        String fcn = "getFinanceRequests";
        String response = this.proxy.evaluateTransaction("admin", subContractName, fcn);

        FinanceRequest[] requests = gson.fromJson(response, FinanceRequest[].class);
        return Arrays.asList(requests);
    }

    @Override
    public void rejectFinanceRequest(String id) throws Exception {

    }

}
