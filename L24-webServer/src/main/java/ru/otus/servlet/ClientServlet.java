package ru.otus.servlet;

import ru.otus.model.Client;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientServlet extends HttpServlet {

    private static final String CLIENT_PAGE_TEMPLATE = "client.html";
    private static final String CLIENTS_TEMPLATES = "clients";

    private final DBServiceClient clientService;
    private final TemplateProcessor templateProcessor;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = clientService.findAll();
        paramsMap.put(CLIENTS_TEMPLATES, clients);

        response.setContentType("text/html");
        String page = templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap);
        response.getWriter().println(page);
    }
}
