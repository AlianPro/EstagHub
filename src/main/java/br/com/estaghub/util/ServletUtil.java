package br.com.estaghub.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

    public static String getContextParameter(HttpServletRequest req, String name) {
        return req.getServletContext().getInitParameter(name);
    }

}
