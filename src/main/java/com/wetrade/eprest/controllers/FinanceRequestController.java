package com.wetrade.eprest.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.assets.FinanceRequest;
import com.wetrade.assets.FinanceRequestGroup;
import com.wetrade.common.FabricProxyException;
import com.wetrade.utils.BaseResponse;
import com.wetrade.eprest.FinanceRequestService;
import com.wetrade.utils.ResponseStatus;

import org.json.JSONObject;

import spark.Spark;

public class FinanceRequestController {
    public FinanceRequestController(FinanceRequestService service) {
        String format = "EEE MMM d HH:mm:ss Z yyy";
        Gson gson = new GsonBuilder().setDateFormat(format).create();

        Spark.get("/api/financerequests", (req, res) -> {
            res.type("application/json");
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            Collection<FinanceRequest> requests;
            try {
                requests = (Collection<FinanceRequest>) service.getFinanceRequests(user);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(requests));
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });


        Spark.get("/api/financerequests/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            FinanceRequest request = service.getFinanceRequest(id);

            BaseResponse response;
            if (request == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Finance Request not found with ID: " + id);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(request));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/financerequests/hash/:hash", (req, res) -> {
            res.type("application/json");
            String hash = req.params(":hash");
            String user = new JSONObject(req.body()).getString("user");

            FinanceRequest request = service.getFinanceRequestByHash(hash);

            BaseResponse response;
            if (request == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Finance Request not found with hash: " + hash);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(request));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/financerequests/group/hash/:hash", (req, res) -> {
            res.type("application/json");
            String hash = req.params(":hash");
            String user = new JSONObject(req.body()).getString("user");
            BaseResponse response;

            try {
                Collection<FinanceRequest> requests = service.getFinanceRequestsByGroupHash(hash);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(requests));
            } catch (FabricProxyException ex) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(ex));
            }

            return gson.toJson(response);
        });

        Spark.post("/api/financerequests", (req, res) -> {
            res.type("application/json");
            String body = req.body();
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            JSONObject financeRequestJSon = new JSONObject(body);
            financeRequestJSon.remove("user");
            try {
                FinanceRequestGroup financeRequestGroup = service.createFinanceRequest(financeRequestJSon);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(financeRequestGroup));
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.put("/api/financerequests/:requestId/accept", (req, res) -> {
            res.type("application/json");
            String requestId = req.params(":requestId");

            BaseResponse response;
            try {
                service.acceptFinanceRequest(requestId);
                response = new BaseResponse(ResponseStatus.SUCCESS);
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.put("/api/financerequests/:requestId/withdraw", (req, res) -> {
            res.type("application/json");
            String requestId = req.params(":requestId");

            BaseResponse response;
            try {
                service.withdrawFinanceRequest(requestId);
                response = new BaseResponse(ResponseStatus.SUCCESS);
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });
    }
}
