package br.edu.unifip.ecommerceapi.constants;

public class SecurityConstants {
    public static final String SECRET = "2B4B6250645367566B5970337336763979244226452948404D6351665468576D";
    public static final long EXPIRATION_TIME = 5 * 60 * 60 * 100; // 30 minutos
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users/login";
    public static final String SIGN_IN_URL = "/api/users/register";
    public static final String IMAGES_URL = "/api/images/**";
}
