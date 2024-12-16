package com.netfreemexico.generador;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utils for NetFreeMX Gen by Cristian Gonzalez
 * © NetFreeMexico 2023
 */

public class NetFreeMXGen {
    private static NetFreeMXGen netFreeMXGen;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private boolean isUserToken = false;
    private String defaultTokenPassword = "";
    private Context context;

    public static NetFreeMXGen getInstance() {
        if (netFreeMXGen == null) {
            netFreeMXGen = new NetFreeMXGen();
        }
        return netFreeMXGen;
    }

    private NetFreeMXGen() {}

    public void init(Context context, SharedPreferences prefs, boolean isUserTokenApp) {
        this.context = context;
        this.prefs = prefs;
        edit = prefs.edit();
        isUserToken = isUserTokenApp;
        defaultTokenPassword = getTokenId();
    }

    public void setTokenPassword(String tokenPassword) {
        defaultTokenPassword = tokenPassword;
    }

    public void loadConfig(JSONArray jsonConfig) {
        try {
            final int position = prefs.getInt(GenConstants.SERVER_POSITION, 0);
            final JSONObject server = jsonConfig.getJSONObject(position);
            final int serverType = server.getInt("serverType");

            switch (serverType) {
                case GenConstants.type_ssh:
                    loadSSH(server);
                    Log.i(getClass().getName(), "SSH");
                    break;
                case GenConstants.type_slowdns:
                    loadSlowDNS(server);
                    Log.i(getClass().getName(), "SLOWDNS");
                    break;
                case GenConstants.type_udp:
                    loadUDP(server);
                    Log.i(getClass().getName(), "UDP");
                    break;
                case GenConstants.type_v2ray:
                    loadV2ray(server);
                    Log.i(getClass().getName(), "V2RAY");
                    break;
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    private void loadV2ray(JSONObject server) throws JSONException {
        String v2rayJson = server.getString("v2rayJson");
        edit.putString(GenConstants.V2RAY_JSON, v2rayJson);
        edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_V2RAY);
        edit.apply();
    }

    private void loadSSH(JSONObject server) throws JSONException {
        String ssh_server = server.getString("ServerIP");
        String remote_proxy = server.getString("ProxyIP");
        String proxy_port = server.getString("ProxyPort");
        String ssh_user = server.getString("ServerUser");
        String ssh_password = server.getString("ServerPass");
        String ssh_port = server.getString("ServerPort");
        String ssl_port = server.getString("SSLPort");
        String payload = server.getString("Payload");
        String sni = server.getString("SNI");
        String tunnelType = server.getString("isSSL");

        if (isUserToken) {
            if (isUserMode()) {
                edit.putString(GenConstants.USUARIO_KEY, getPref(GenConstants.USER_LOGIN));
                edit.putString(GenConstants.SENHA_KEY, getPref(GenConstants.PASSW_LOGIN));
            } else {
                edit.putString(GenConstants.USUARIO_KEY, getTokenId());
                edit.putString(GenConstants.SENHA_KEY, defaultTokenPassword);
            }
        } else {
            edit.putString(GenConstants.USUARIO_KEY, ssh_user );
            edit.putString(GenConstants.SENHA_KEY, ssh_password);
        }

        edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
        edit.putString(GenConstants.PROXY_IP_KEY, remote_proxy);
        edit.putString(GenConstants.PROXY_PORTA_KEY, proxy_port);

        switch (tunnelType) {
            case GenConstants.tunnel_direct:

                edit.putBoolean(GenConstants.PROXY_USAR_DEFAULT_PAYLOAD, false);
                edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_SSH_DIRECT);

                edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
                edit.putString(GenConstants.SERVIDOR_PORTA_KEY, ssh_port);

                edit.putString(GenConstants.PROXY_IP_KEY, remote_proxy);
                edit.putString(GenConstants.PROXY_PORTA_KEY, proxy_port);
                edit.putString(GenConstants.CUSTOM_PAYLOAD_KEY, payload);

                Log.i(getClass().getName(), "TUNNEL DIRECT");

                break;
            case GenConstants.tunnel_proxy:
                edit.putBoolean(GenConstants.PROXY_USAR_DEFAULT_PAYLOAD, false);
                edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_SSH_PROXY);

                edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
                edit.putString(GenConstants.SERVIDOR_PORTA_KEY, ssh_port);

                edit.putString(GenConstants.PROXY_IP_KEY, remote_proxy);
                edit.putString(GenConstants.PROXY_PORTA_KEY, proxy_port);
                edit.putString(GenConstants.CUSTOM_PAYLOAD_KEY, payload);
                Log.i(getClass().getName(), "TUNNEL PROXY");
                break;
            case GenConstants.tunnel_ssl:
                edit.putBoolean(GenConstants.PROXY_USAR_DEFAULT_PAYLOAD, true);
                edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_SSH_SSL);

                edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
                edit.putString(GenConstants.SERVIDOR_PORTA_KEY, ssl_port);

                edit.putString(GenConstants.PROXY_IP_KEY, remote_proxy);
                edit.putString(GenConstants.PROXY_PORTA_KEY, proxy_port);

                edit.putString(GenConstants.CUSTOM_PAYLOAD_KEY, payload);
                edit.putString(GenConstants.CUSTOM_SNI, sni);
                Log.i(getClass().getName(), "TUNNEL SSL");
                break;
            case GenConstants.tunnel_sslpay:
                edit.putBoolean(GenConstants.PROXY_USAR_DEFAULT_PAYLOAD, false);
                edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_SSL_PAYLOAD);

                edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
                edit.putString(GenConstants.SERVIDOR_PORTA_KEY, ssl_port);

                edit.putString(GenConstants.CUSTOM_PAYLOAD_KEY, payload);
                edit.putString(GenConstants.CUSTOM_SNI, sni);
                Log.i(getClass().getName(), "TUNNEL SSLPAY");
                break;
        }
        edit.apply();

    }

    private void loadSlowDNS(JSONObject server) throws JSONException {
        String chaveKey = server.getString("Slowchave");
        String serverNameKey = server.getString("Nameserver");
        String dnsKey = server.getString("Slowdns");
        String ssh_server = server.getString("ServerIP");
        String ssh_port = server.getString("ServerPort");

        edit.putString(GenConstants.CHAVE_KEY, chaveKey);

        edit.putString(GenConstants.NAMESERVER_KEY, serverNameKey);
        edit.putString(GenConstants.DNS_KEY, dnsKey);

        edit.putString(GenConstants.SERVIDOR_KEY, ssh_server);
        edit.putString(GenConstants.SERVIDOR_PORTA_KEY, ssh_port);

        edit.putBoolean(GenConstants.PROXY_USAR_DEFAULT_PAYLOAD, true);
        edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_SLOWDNS);

        edit.apply();
    }

    private void loadUDP(JSONObject server) throws JSONException {
        String udpserver = server.getString("udpServer");
        String udpauth = server.getString("udpAuth");
        String udpobfs = server.getString("udpObfs");
        String udpbuffer = server.getString("udpBuffer");
        String udpup = server.getString("udpUp");
        String udpdown = server.getString("udpDown");

        edit.putString(GenConstants.UDP_SERVER, udpserver);

        edit.putString(GenConstants.UDP_AUTH, udpauth);
        edit.putString(GenConstants.UDP_OBFS, udpobfs);


        edit.putString(GenConstants.UDP_BUFFER, udpbuffer);
        edit.putString(GenConstants.UDP_UP, udpup);

        edit.putString(GenConstants.UDP_DOWN, udpdown);
        edit.putInt(GenConstants.TUNNELTYPE_KEY, GenConstants.bTUNNEL_TYPE_UDP);
        edit.apply();
    }

    private String getPref(String key) {
        SharedPreferences myprefs = PreferenceManager.getDefaultSharedPreferences(context);
        return myprefs.getString(GenConstants.USER_LOGIN, "");
    }

    private boolean isUserMode() {
        SharedPreferences myprefs = PreferenceManager.getDefaultSharedPreferences(context);
        return myprefs.getBoolean(GenConstants.USER_HWID, false);
    }

    private String getTokenId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
