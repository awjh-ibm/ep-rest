package com.wetrade.eprest;

import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.wetrade.assets.PurchaseOrder;
import com.wetrade.common.FabricProxy;
import com.wetrade.common.FabricProxyConfig;
import com.wetrade.common.FabricProxyException;

import org.json.JSONObject;

public class PurchaseOrderServiceFabricImpl implements PurchaseOrderService {
    private FabricProxyConfig config;
    private FabricProxy proxy;

    public PurchaseOrderServiceFabricImpl(FabricProxyConfig config) throws FabricProxyException {
        this.config = config;
        this.proxy = new FabricProxy(this.config);
    }

    public Collection<PurchaseOrder> getPurchaseOrders() throws FabricProxyException {
        Gson gson = new Gson();
        String response = this.proxy.evaluateTransaction("admin", "getPurchaseOrders");
        PurchaseOrder[] purchaseOrders = gson.fromJson(response, PurchaseOrder[].class);
        return Arrays.asList(purchaseOrders);
    }

    public PurchaseOrder getPurchaseOrder(String id) throws FabricProxyException {
        Gson gson = new Gson();
        String response = this.proxy.evaluateTransaction("admin", "getPurchaseOrder", new String[]{id});
        PurchaseOrder purchaseOrder = gson.fromJson(response, PurchaseOrder.class);
        return purchaseOrder;
    }

    public PurchaseOrder getPurchaseOrderByHash(String hash) {
        return null;
    }

    public PurchaseOrder createPurchaseOrder(JSONObject purchaseOrder) throws FabricProxyException {
        String buyerId = purchaseOrder.getString("buyerId");
        String sellerId = purchaseOrder.getString("sellerId");
        String price = String.valueOf(purchaseOrder.getDouble("price"));
        String units = String.valueOf(purchaseOrder.getInt("units"));
        String productDescriptor = purchaseOrder.getString("productDescriptor");

        String response = this.proxy.submitTransaction("admin", "createPurchaseOrder", buyerId, sellerId, price, units, productDescriptor);
        Gson gson = new Gson();

        return gson.fromJson(response, PurchaseOrder.class);
    }

    public void acceptPurchaseOrder(String id) throws FabricProxyException {
        this.proxy.submitTransaction("admin", "acceptPurchaseOrder", id);
    }

    public void closePurchaseOrder(String id) throws FabricProxyException {
        this.proxy.submitTransaction("admin", "closePurchaseOrder", id);
    }
}
