import bean.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class RePack {
    public static void main(String[] args) {
        try {
            Property property = Property.initWithConfig();
            System.out.print(property.getSrcApkPath());
            hookApk(property);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void hookApk(Property property) {
        File apkFile = new File(property.getSrcApkPath());
        String filePath = apkFile.getParentFile().getAbsolutePath() + File.separator;
        System.out.println(filePath);
        String unSignedApk = filePath + "unsigned.apk";
        if (!apkFile.exists()) {
            System.out.println("apk不存在 请检查路径是否正确");
            return;
        }
        System.out.println("apk 原签名:" + HookUtils.getAppSign(property));
        //查看有没有最新的签名信息配置
        File signFile = new File("apksign.txt");
        if (signFile.exists()) {
            System.out.println("配置了签名值,开始读取进行转化...");
            FileInputStream fis;
            try {
                fis = new FileInputStream("apksign.txt");
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                Constant.appSign = new String(buffer);
                if (Constant.appSign.length() != 0) {
                    System.out.println("获取签名配置信息成功：" + Constant.appSign);
                } else {
                    System.out.println("获取签名配置签名信息失败,默认采用apk自带的签名信息...\n");
                }
            } catch (Exception ignore) {
            }
        } else {
            System.out.println("没有找到配置签名信息,默认采用apk自带的签名信息...\n");
        }
        File unZipFile = new File(filePath + Constant.unZipDir);
        if (!unZipFile.exists()) {
            unZipFile.mkdirs();
        }

        //拷贝原始apk文件一份命名为unsigned.apk
        if (!FileUtils.fileCopy(property.getSrcApkPath(), unSignedApk)) {
            System.out.print("文件拷贝失败！！");
            return;
        }

        if (!HookUtils.getAppEnter(property)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.zipApkWork(apkFile, filePath + Constant.unZipDir)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.deleteMetaInf(filePath + Constant.unZipDir, unSignedApk)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.dexToSmali(filePath + Constant.unZipDir + "classes.dex", filePath + Constant.smaliTmpDir)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.setSignAndPkgName(filePath)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.insertHookCode(filePath)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.smaliToDex(filePath + Constant.smaliTmpDir, "classes.dex")) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.addDexToApk(filePath + Constant.unZipDir, unSignedApk)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        if (!HookUtils.signApk(unSignedApk, property)) {
            HookUtils.deleteTmpFile(filePath);
            return;
        }

        HookUtils.deleteTmpFile(filePath);

    }

}