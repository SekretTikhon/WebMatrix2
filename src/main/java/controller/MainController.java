package controller;

import operations.Operations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String addmatrix_get(Model model) {
        return "index";
    }

    @PostMapping(value = "/")
    public String addmatrix_post(@RequestBody String note, HttpServletResponse response) {
        Map<String, String> params = parseParams(note);
        String out_filename = Operations.Process(params);
        response.setContentType("application/csv");
        getResultFile(out_filename, response);
        return "index";
    }

    Map<String, String> parseParams(String str) {
        Map<String, String> result = new HashMap<>();
        for (String param : str.split("&")) {
            String[] name_value = param.split("=");
            String name = name_value[0];
            String value = name_value.length < 2 ? "" : name_value[1];
            result.put(name, value);
        }
        return result;
    }

    void getResultFile(String filename, HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(filename); //...;
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was " + filename + "\n" + ex);
            //throw new RuntimeException("IOError writing file to output stream");
        }
    }

}