package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateClientServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;
    private final DBServiceClient clientService;

    private static final String CREATE_CLIENT_TEMPLATE = "create.html";
    private static final String ERROR_TEMPLATE = "error.html";

    public CreateClientServlet(TemplateProcessor templateProcessor, DBServiceClient clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        resp.setContentType("text/html");
        paramsMap.put("name", "");
        paramsMap.put("address", "");
        paramsMap.put("phones", "");
        resp.getWriter().println(templateProcessor.getPage(CREATE_CLIENT_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phones = req.getParameter("phones");

        if (name == null || name.isBlank()) {
            Map<String, Object> paramsMap = new HashMap<>();
            resp.setContentType("text/html");
            paramsMap.put("error", "Client name must not be null");
            resp.getWriter().println(templateProcessor.getPage(ERROR_TEMPLATE, paramsMap));
        }
        var phoneList = Arrays.stream(phones.split(",")).map(Phone::new).toList();

        var phoneWithWrongNumber = phoneList.stream()
                .filter(phone -> phone.getNumber().length() != 10)
                .findFirst()
                .orElse(null);

        if (phoneWithWrongNumber != null) {
            Map<String, Object> paramsMap = new HashMap<>();
            resp.setContentType("text/html");
            paramsMap.put("error", "Wrong number, must be 10 digits. \n Number: " + phoneWithWrongNumber.getNumber());
            resp.getWriter().println(templateProcessor.getPage(ERROR_TEMPLATE, paramsMap));
        } else {
            Client client = new Client(name);
            client.setAddress(new Address(address));
            client.setPhones(phoneList);
            clientService.saveClient(client);
            resp.sendRedirect("/client");
        }
    }
}
