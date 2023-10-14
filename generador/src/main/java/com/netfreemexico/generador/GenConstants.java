package com.netfreemexico.generador;

/**
 * Utils for NetFreeMX Gen by Cristian Gonzalez
 * © NetFreeMexico 2023
 */

public interface GenConstants{
    public static final String

            PROXY_IP_KEY = "proxyRemoto",
            PROXY_PORTA_KEY = "proxyRemotoPorta",
            CUSTOM_PAYLOAD_KEY = "proxyPayload",
            PROXY_USAR_DEFAULT_PAYLOAD = "usarDefaultPayload"
                    ;


    // Vpn
    public static final String
            USER_HWID = "userorhwid",
            USER_LOGIN = "userlogin",
            PASSW_LOGIN = "passwlogin",
            BYPASS_KEY = "bypassKey",
            MODO_VIP = "modovip",
            SERVER_POSITION = "LastSelectedServer";

    //UDP
    public static final String
            UDP_SERVER = "udpserver",
            UDP_AUTH = "udpauth",
            UDP_OBFS = "udpobfs",
            UDP_DOWN = "udpdown",
            UDP_UP = "udpup",
            UDP_BUFFER = "udpbuffer",
            V2RAY_JSON = "v2rayjson";


    // SSH
    public static final String
            SERVIDOR_KEY = "sshServer",
            SERVIDOR_PORTA_KEY = "sshPort",
            USUARIO_KEY = "sshUser",
            SENHA_KEY = "sshPass",
            CHAVE_KEY = "chaveKey",
            NAMESERVER_KEY = "serverNameKey",
            DNS_KEY = "dnsKey";

    public static final String
            PAYLOAD_DEFAULT = "CONNECT [host_port] [protocol][crlf][crlf]",
            CUSTOM_SNI = "sslSNI";

    // Tunnel Type
    public static final String
            TUNNELTYPE_KEY = "tunnelType";
    ;

    public static final int
            bTUNNEL_TYPE_SSH_DIRECT = 1,
            bTUNNEL_TYPE_SSH_PROXY = 2,
            bTUNNEL_TYPE_SSH_SSL = 3,
            bTUNNEL_TYPE_SSL_PAYLOAD = 4,
            bTUNNEL_TYPE_SLOWDNS = 5,
            bTUNNEL_TYPE_UDP = 6,
            bTUNNEL_TYPE_V2RAY = 7;
    ;

    public static final int type_ssh = 0,
            type_slowdns = 1,
            type_udp = 2,
            type_v2ray = 3;

    public static final String tunnel_direct = "direct",
            tunnel_proxy = "sshproxy",
            tunnel_ssl = "sshssl",
            tunnel_sslpay = "sslpayload";
}