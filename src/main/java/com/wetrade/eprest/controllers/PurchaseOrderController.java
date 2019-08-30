package com.wetrade.eprest.controllers;

import java.text.DateFormat;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.utils.BaseResponse;
import com.wetrade.eprest.PurchaseOrderService;
import com.wetrade.utils.ResponseStatus;

import org.json.JSONObject;

import com.wetrade.assets.PurchaseOrder;
import com.wetrade.common.FabricProxyException;

import spark.Spark;

public class PurchaseOrderController {
    public PurchaseOrderController(PurchaseOrderService service) {
        String format = "EEE MMM d HH:mm:ss Z yyy";
        Gson gson = new GsonBuilder().setDateFormat(format).create();

        Spark.get("/api/purchaseorders", (req, res) -> {
            res.type("application/json");
            String user = new JSONObject(req.body()).getString("user");


            BaseResponse response;
            Collection<PurchaseOrder> purchaseOrders;
            try {
                purchaseOrders = (Collection<PurchaseOrder>) service.getPurchaseOrders(user);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(purchaseOrders));
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/purchaseorders/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            PurchaseOrder purchaseOrder = service.getPurchaseOrder(id);

            BaseResponse response;
            if (purchaseOrder == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Purchase Order not found with ID: " + id);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(purchaseOrder));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/purchaseorders/hash/:hash", (req, res) -> {
            res.type("application/json");
            String hash = req.params(":hash");
            String user = new JSONObject(req.body()).getString("user");

            PurchaseOrder purchaseOrder = service.getPurchaseOrderByHash(hash);

            BaseResponse response;
            if (purchaseOrder == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Purchase Order not found with hash: " + hash);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(purchaseOrder));
            }
            return gson.toJson(response);
        });

        Spark.post("/api/purchaseorders", (req, res) -> {
            res.type("application/json");
            String body = req.body();

            BaseResponse response;
            JSONObject purchaseOrderJson = new JSONObject(body);
            String user = purchaseOrderJson.getString("user");
            purchaseOrderJson.remove("user");
            try {
                PurchaseOrder purchaseOrder = service.createPurchaseOrder(purchaseOrderJson);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(purchaseOrder));
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.put("api/purchaseorders/:id/accept", (req, res) -> {
            res.type("application/json");
            String purchaseOrderId = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            try {
                service.acceptPurchaseOrder(purchaseOrderId);
                response = new BaseResponse(ResponseStatus.SUCCESS);
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR);
            }
            return gson.toJson(response);
        });

        Spark.put("api/purchaseorders/:id/close", (req, res) -> {
            res.type("application/json");
            String purchaseOrderId = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            try {
                service.closePurchaseOrder(purchaseOrderId);
                response = new BaseResponse(ResponseStatus.SUCCESS);
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR);
            }
            return gson.toJson(response);
        });

    }
}
