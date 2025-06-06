package com.grupo8.audit.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnectionUtil {

    public static Connection getConnection() throws Exception {
        String walletPath = "/home/site/wwwroot/wallet";
        //String walletPath = "C:/Wallet_PB06O46XMPFJU1B9";
        
        System.setProperty("oracle.net.tns_admin", walletPath);
        System.setProperty("oracle.net.ssl_server_dn_match", "true");

        String jdbcUrl = "jdbc:oracle:thin:@pb06o46xmpfju1b9_tp?TNS_ADMIN=" + walletPath;
        String username = "APICONSUMER";
        String password = "!&fddE>X1hMagb3";
        
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

}
