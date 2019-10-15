import java.io.File;

public final class Constant {
    public final static String METAINFO = "META-INF/";
    public final static String unZipDir = "unzipapk" + File.separator;

    public final static String smaliTmpDir = "smali_tmp";
    public final static String signLineTag = ".line 51";
    public final static String pkgNameLineTag = ".local v0, \"hookSign\":Ljava/lang/String;";
    public final static String pmsSmaliDir =  "cn" + File.separator + "wzc" + File.separator +"hookpms" + File.separator;
    public final static String smaliFileHandler = "PmsHookBinderInvocationHandler.smali";
    public final static String smaliFilePMS = "ServiceManagerWrapper.smali";
    public final static String applicationAttachLineTag = ".method protected attachBaseContext(Landroid/content/Context;)V";
//    public final static String applicationPublicAttachLineTag = ".method public attachBaseContext(Landroid/content/Context;)V";
    public final static String applicationCreateLineTag = ".method public onCreate()V";
    public final static String activityCreateLineTag = ".method protected onCreate(Landroid/os/Bundle;)V";
    public final static String methodEndStr = ".end method";
    public final static String hookAttachCodeStr = "\tinvoke-static {p1}, Lcn/wzc/hookpms/ServiceManagerWrapper;->hookPMS(Landroid/content/Context;)V\n";
    public final static String hookCreateCodeStr = "\tinvoke-static/range {p0 .. p0}, Lcn/wzc/hookpms/ServiceManagerWrapper;->hookPMS(Landroid/content/Context;)V\n";

    public static String entryClassName = "";
    public static String appSign = "";
    public static String appPkgName = "";
    public static boolean isApplicationEntry = true;
}
