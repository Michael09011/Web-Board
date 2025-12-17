package net.datasa.web5.common;

public interface PathConstants {

    public static final String BASE = "/";

    // 회원 관련 경로
    String MEMBER = "/member";
    String LOGOUT = "/member/logout";
    // 로그인, 회원가입, 비밀번호 재설정 등 경로
    String HOME = "/";
    String INDEX = "/index";
    String CHECK_ID = "/member/checkId";   

    String LOGIN = "/member/login";
    String LOGIN_PROCESS = "/member/loginProcess";
    String JOIN = "/member/join";
    String PASSWORD_RESET = "/passwordreset";
    // 필요에 따라 경로를 계속 추가
}