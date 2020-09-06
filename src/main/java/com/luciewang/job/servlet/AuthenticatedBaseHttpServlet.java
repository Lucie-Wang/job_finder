package com.luciewang.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luciewang.job.entity.ResultResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticatedBaseHttpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        sessionValidation(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        sessionValidation(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        sessionValidation(req, resp);
    }

    private void sessionValidation(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            try {
                mapper.writeValue(response.getWriter(), new ResultResponse("Session Invalid"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }


}
