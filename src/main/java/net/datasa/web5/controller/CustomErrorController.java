package net.datasa.web5.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMsg = "";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "요청하신 페이지를 찾을 수 없습니다.";
                model.addAttribute("errorCode", "404 Not Found");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMsg = "서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                model.addAttribute("errorCode", "500 Internal Server Error");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMsg = "해당 페이지에 접근할 권한이 없습니다.";
                 model.addAttribute("errorCode", "403 Forbidden");
            } else {
                errorMsg = "알 수 없는 오류가 발생했습니다.";
                model.addAttribute("errorCode", statusCode);
            }
        } else {
            errorMsg = "오류가 발생했습니다.";
            model.addAttribute("errorCode", "Unknown");
        }
        model.addAttribute("errorMessage", errorMsg);
        return "error"; // templates/error.html
    }
}
