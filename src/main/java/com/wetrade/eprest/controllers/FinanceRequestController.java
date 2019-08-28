package com.wetrade.eprest.controllers;

import java.text.DateFormat;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.assets.FinanceRequest;
import com.wetrade.common.FabricProxyException;
import com.wetrade.eprest.BaseResponse;
import com.wetrade.eprest.FinanceRequestService;
import com.wetrade.eprest.ResponseStatus;

import org.json.JSONObject;

import spark.Spark;

public class FinanceRequestController {
    public FinanceRequestController(FinanceRequestService service) {
        String format = "EEE MMM d HH:mm:ss Z yyy";
        Gson gson = new GsonBuilder().setDateFormat(format).create();

        Spark.get("/api/financerequests", (req, res) -> {
            res.type("application/json");

            BaseResponse response;
            Collection<FinanceRequest> requests;
            try {
                requests = (Collection<FinanceRequest>) service.getFinanceRequests("andy");
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(requests));
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });


        Spark.get("/api/financerequests/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");

            FinanceRequest request = service.getFinanceRequest(id);

            BaseResponse response;
            if (request == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Purchase Order not found with ID: " + id);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(request));
            }
            return gson.toJson(response);
        });

        Spark.post("/api/financerequests", (req, res) -> {
            res.type("application/json");
            String body = req.body();

            BaseResponse response;
            JSONObject financeRequestJSon = new JSONObject(body);
            try {
                FinanceRequest[] financeRequests = service.createFinanceRequest(financeRequestJSon);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(financeRequests));
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });
    }
}
