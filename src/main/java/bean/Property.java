package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private String srcApkPath;
    private String outPakPath;
    private String keystore;

    private String password;
    private String signAlias;
    private String signAliasPassword;

    public String getSrcApkPath() {
        return srcApkPath;
    }

    public String getOutPakPath() {
        return outPakPath;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getPassword() {
        return password;
    }

    public String getSignAlias() {
        return signAlias;
    }

    public String getSignAliasPassword() {
        return signAliasPassword;
    }

    public Property(String srcApkPath, String outPakPath, String keystore, String password, String signAlias, String signAliasPassword) {
        this.srcApkPath = srcApkPath;
        this.outPakPath = outPakPath;
        this.keystore = keystore;
        this.password = password;
        this.signAlias = signAlias;
        this.signAliasPassword = signAliasPassword;
    }

    public static Property initWithConfig() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.txt")) {
            properties.load(fis);
        }
        String curFilePath = new File("").getAbsolutePath() + File.separator;
        String srcApkPath = curFilePath + properties.getProperty("apk.src");
        String outApkPath = curFilePath + properties.getProperty("apk.out");

        String keystoreName = properties.getProperty("sign.file");
        String password = properties.getProperty("sign.password");
        String signAlias = properties.getProperty("sign.alias");
        String signAliasPassword = properties.getProperty("sign.aliasPassword");
        return new Property(srcApkPath, outApkPath, keystoreName, password, signAlias, signAliasPassword);
    }
}
