package com.wetrade.eprest;

import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.assets.FinanceRequest;
import com.wetrade.assets.FinanceRequestGroup;
import com.wetrade.common.FabricProxy;
import com.wetrade.common.FabricProxyConfig;
import com.wetrade.common.FabricProxyException;

import org.json.JSONObject;

public class FinanceRequestServiceFabricImpl implements FinanceRequestService {
    private String subContractName = "FinanceRequestContract";
    private FabricProxy proxy;
    String format = "EEE MMM d HH:mm:ss Z yyy";
    private Gson gson = new GsonBuilder().setDateFormat(format).create();
    private String identity;
    private String targetPeer;

    public FinanceRequestServiceFabricImpl(FabricProxyConfig config, String identity, String targetPeer) throws FabricProxyException {
        this.proxy = new FabricProxy(config);
        this.identity = identity;
        this.targetPeer = targetPeer;
    }

    @Override
    public FinanceRequestGroup createFinanceRequest(JSONObject financeRequest) throws FabricProxyException {
        String requesterId = financeRequest.getString("requesterId");
        String financierIds = financeRequest.getJSONArray("financierIds").toString();
        String purchaseOrderId = financeRequest.getString("purchaseOrderId");
        String amount = Double.toString(financeRequest.getDouble("amount"));
        String interest = Double.toString(financeRequest.getDouble("interest"));
        String monthLength = Integer.toString(financeRequest.getInt("monthLength"));

        String fcn = "createFinanceRequest";
        String response = this.proxy.submitTransaction(new String[] {this.targetPeer}, this.identity, subContractName, fcn, requesterId, financierIds, purchaseOrderId, amount, interest, monthLength);

        return gson.fromJson(response, FinanceRequestGroup.class);
    }

    @Override
    public FinanceRequest getFinanceRequest(String id) throws FabricProxyException {
        String fcn = "getFinanceRequest";
        String response = this.proxy.evaluateTransaction(this.identity, subContractName, fcn, new String[]{id});

        FinanceRequest request = gson.fromJson(response, FinanceRequest.class);
        return request;
    }

    @Override
    public FinanceRequest getFinanceRequestByHash(String hash) throws FabricProxyException {
        return null;
    }

    @Override
    public Collection<FinanceRequest> getFinanceRequests(String behalfOfId) throws FabricProxyException {
        String fcn = "getFinanceRequests";
        String response = this.proxy.evaluateTransaction(identity, subContractName, fcn, new String[]{behalfOfId});

        FinanceRequest[] requests = gson.fromJson(response, FinanceRequest[].class);
        return Arrays.asList(requests);
    }

    @Override
    public Collection<FinanceRequest> getFinanceRequestsByGroupHash(String hash) throws FabricProxyException {
        String fcn = "getFinanceRequestsByGroupHash";
        String response = this.proxy.evaluateTransaction(identity, subContractName, fcn, new String[]{hash});
        FinanceRequest[] requests = gson.fromJson(response, FinanceRequest[].class);
        return Arrays.asList(requests);
    }

    @Override
    public void acceptFinanceRequest(String id) throws Exception {
        String fcn = "acceptFinanceRequest";
        this.proxy.submitTransaction(this.identity, subContractName, fcn, id);
    }

    @Override
    public void withdrawFinanceRequest(String id) throws FabricProxyException {
        String fcn = "withdrawFinanceRequest";
        this.proxy.submitTransaction(identity, subContractName, fcn, id);
    }
}
