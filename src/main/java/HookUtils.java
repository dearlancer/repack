import bean.Property;

import java.io.*;

public class HookUtils {

    /**
     * 获取应用签名信息
     *
     * @param property
     * @return
     */
    public static boolean getAppSign(Property property) {
        try {
            long time = System.currentTimeMillis();
            System.out.println("第一步==> 获取apk文件签名信息");
            String sign = ApkSign.getApkSignInfo(property.getSrcApkPath());
            Constant.appSign = sign;
            System.out.println("signed:" + sign);
            System.out.println("获取apk签名信息成功===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Exception e) {
            System.out.println("获取apk签名信息失败，退出！:" + e.toString());
            return false;
        }
    }

    /**
     * 获取应用入口类
     *
     * @param property
     */
    public static boolean getAppEnter(Property property) {
        try {
            long time = System.currentTimeMillis();
            System.out.println("第二步==> 获取apk文件入口信息");
            String enter = AnalysisApk.getAppEnterApplication(property.getSrcApkPath());
            Constant.entryClassName = enter.replace(".", "/");
            System.out.println("应用入口:" + enter);
            System.out.println("获取apk入口类信息成功===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Exception e) {
            System.out.println("获取apk入口类信息失败，退出！:" + e.toString());
            FileUtils.printException(e);
            return false;
        }
    }

    /**
     * 解压apk
     */
    public static boolean zipApkWork(File srcApkFile, String unZipDir) {
        try {
            long time = System.currentTimeMillis();
            System.out.println("第三步==> 解压apk文件:" + srcApkFile.getAbsolutePath());
            FileUtils.decompressDexFile(srcApkFile.getAbsolutePath(), unZipDir);
            System.out.println("解压apk文件结束===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Throwable e) {
            System.out.println("解压apk文件失败，退出！:" + e.toString());
            return false;
        }
    }

    /**
     * 删除签名文件
     */
    public static boolean deleteMetaInf(String unZipDir, String srcApkPath) {
        try {
            long time = System.currentTimeMillis();
            File metaFile = new File(unZipDir + Constant.METAINFO);
            System.out.println("第四步==> 删除签名文件:" + metaFile.getAbsolutePath());
            if (metaFile.exists()) {
                File[] metaFileList = metaFile.listFiles();
                if (metaFileList == null) {
                    return false;
                }
                StringBuilder cmd = new StringBuilder();
                cmd.append("aapt remove ").append(new File(srcApkPath).getAbsolutePath());
                for (File f : metaFileList) {
                    if (f.isDirectory()) {
                        continue;
                    }
                    cmd.append(" " + Constant.METAINFO).append(f.getName());
                }
                System.out.println("删除签名文件命令:" + cmd);
                execCmd(cmd.toString(), true);
            }
            System.out.println("删除签名文件结束===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Throwable e) {
            System.out.println("删除签名文件失败，退出！:" + e.toString());
            return false;
        }
    }

    /**
     * 将dex转化成smali
     */
    public static boolean dexToSmali(String dexFile, String smaliDir) {
        File smaliDirF = new File(smaliDir);
        if (smaliDirF.exists()) {
            smaliDirF.delete();
        }
        smaliDirF.mkdirs();
        System.out.println("第五步==> 将dex转化成smali");
        String javaCmd = "baksmali disassemble -o " + smaliDir + " " + dexFile;
        System.out.println(javaCmd);
        long startTime = System.currentTimeMillis();
        try {
            Process pro = Runtime.getRuntime().exec(javaCmd);
            int status = pro.waitFor();
            if (status == 0) {
                System.out.println("dex转化smali成功===耗时:" + ((System.currentTimeMillis() - startTime) / 1000) + "s\n\n");
                return true;
            }
            System.out.println("dex转化smali失败,status:" + status);
            return false;
        } catch (Exception e) {
            System.out.println("dex转化smali失败:" + e.toString());
            return false;
        }
    }

    /**
     * 替换原始签名和包名
     */
    public static boolean setSignAndPkgName(String filePath) {
        System.out.println("第六步==> 代码中替换原始签名和包名信息");
        File pmsSmaliDirF = new File(filePath + Constant.smaliTmpDir + File.separator + Constant.pmsSmaliDir);
        if (!pmsSmaliDirF.exists()) {
            pmsSmaliDirF.mkdirs();
        }
        FileReader reader = null;
        BufferedReader br = null;
        FileWriter writer = null;
        try {
            long startTime = System.currentTimeMillis();
            FileUtils.fileCopy("./smali" + File.separator + Constant.smaliFileHandler, pmsSmaliDirF.getAbsolutePath() + File.separator + Constant.smaliFileHandler);
            writer = new FileWriter(pmsSmaliDirF.getAbsolutePath() + File.separator + Constant.smaliFilePMS);
            reader = new FileReader("./smali" + File.separator + Constant.smaliFilePMS);
            br = new BufferedReader(reader);
            String str;
            while ((str = br.readLine()) != null) {
                if (str.contains(Constant.signLineTag)) {
                    writer.write(str + "\n");
                    String signStr = "\tconst-string v0, \"" + Constant.appSign + "\"";
                    writer.write(signStr + "\n");
                    System.out.println("写入签名信息:" + signStr);
                    String skipString = br.readLine();
                    System.out.println("忽略行:" + skipString);
                }
                if (str.contains(Constant.pkgNameLineTag)) {
                    System.out.println("写入packageName:" + Constant.appPkgName);
                    String pkgNameStr = "\tconst-string v1, \"" + Constant.appPkgName + "\"";
                    writer.write(pkgNameStr + "\n");
                    String skipString = br.readLine();
                    System.out.println("忽略行:" + skipString);
                } else {
                    writer.write(str + "\n");
                }
            }
            System.out.println("设置签名和包名成功===耗时:" + ((System.currentTimeMillis() - startTime) / 1000) + "s\n\n");
            return true;
        } catch (Exception e) {
            System.out.println("设置签名和包名失败:" + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignore) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ignore) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {
                }
            }
        }
        return false;
    }

    /**
     * 插入hook代码
     */
    public static boolean insertHookCode(String filePath) {
        System.out.println("第七步==> 添加hook代码");
        long startTime = System.currentTimeMillis();
        String enterFile = filePath + Constant.smaliTmpDir + File.separator + Constant.entryClassName.replace(".", File.separator) + ".smali";
        String enterFileTmp = filePath + Constant.smaliTmpDir + File.separator + Constant.entryClassName.replace(".", File.separator) + "_tmp.smali";
        boolean isWorkSucc = false;
        try (FileReader reader = new FileReader(enterFile); BufferedReader br = new BufferedReader(reader); FileWriter writer = new FileWriter(enterFileTmp)) {
            String str;
            boolean isSucc = false;
            int isEntryMethod = -1;
            while ((str = br.readLine()) != null) {
                if (isSucc) {
                    writer.write(str + "\n");
                    continue;
                }
                if (Constant.isApplicationEntry) {
                    if (str.contains(Constant.applicationAttachLineTag)) {
                        isEntryMethod = 0;
                    } else if (str.contains(Constant.applicationCreateLineTag)) {
                        isEntryMethod = 1;
                    }
                } else if (str.contains(Constant.activityCreateLineTag)) {
                    isEntryMethod = 2;
                }

                if (str.contains(Constant.methodEndStr)) {
                    isEntryMethod = -1;
                }

                writer.write(str + "\n");
                if (isEntryMethod == 0) {
                    writer.write(Constant.hookAttachCodeStr);
                    isSucc = true;
                    System.out.println("插入位置=====" + enterFile + " >>>> attachBaseContext");
                } else if (isEntryMethod == 1 || isEntryMethod == 2) {
                    writer.write(Constant.hookCreateCodeStr);
                    isSucc = true;
                    System.out.println("插入位置=====onCreate");
                }

            }
            System.out.println("插入hook代码成功===耗时" + ((System.currentTimeMillis() - startTime) / 1000) + "s\n\n");
            isWorkSucc = true;
        } catch (Exception e) {
            System.out.println("插入hook代码失败:" + e.toString());
        }

        File entryFile = new File(enterFile);
        entryFile.delete();
        File entryFileTmp = new File(enterFileTmp);
        entryFileTmp.renameTo(new File(enterFile));
        return isWorkSucc;
    }

    /**
     * 将smali转化成dex
     */
    public static boolean smaliToDex(String smaliDir, String dexFile) {
        System.out.println("第八步==> 将smali转化成dex");
        File dexFileF = new File(dexFile);
        if (dexFileF.exists()) {
            dexFileF.delete();
        }
        String javaCmd = "smali assemble " + smaliDir + " -o " + dexFile;
        System.out.println(javaCmd);
        long startTime = System.currentTimeMillis();
        try {
            Process pro = Runtime.getRuntime().exec(javaCmd);
            int status = pro.waitFor();
            Thread.sleep(2000);
            if (status == 0) {
                System.out.println("smali转化dex成功===耗时:" + ((System.currentTimeMillis() - startTime) / 1000) + "s\n\n");
                return true;
            }
            System.out.println("smali转化dex失败,status:" + status);
            return false;
        } catch (Exception e) {
            System.out.println("smali转化dex失败:" + e.toString());
            return false;
        }
    }


    /**
     * 使用aapt命令添加dex文件到apk中
     */
    public static boolean addDexToApk(String unZipDir, String srcApkPath) {
        try {
            System.out.println("第九步==> 将dex文件添加到源apk中");
            long time = System.currentTimeMillis();
            File classDir = new File(unZipDir);
            File[] classListFile = classDir.listFiles();
            StringBuilder removeCmd = new StringBuilder();
            removeCmd.append("aapt remove ").append(new File(srcApkPath).getAbsolutePath());
            for (File file : classListFile) {
                if (file.getName().endsWith("classes.dex")) {
                    removeCmd.append(" ").append(file.getName());
                }
            }
            System.out.println("cmd:" + removeCmd.toString());
            if (!execCmd(removeCmd.toString(), true)) {
                System.out.println("添加dex文件到apk中失败，退出！");
                return false;
            }
            StringBuilder addCmd = new StringBuilder();
            addCmd.append("aapt add ").append(new File(srcApkPath).getAbsolutePath());
            for (File file : classListFile) {
                if (file.getName().endsWith(".dex")) {
                    addCmd.append(" ").append(file.getName());
                }
            }
            System.out.println("cmd:" + addCmd);
            if (!execCmd(addCmd.toString(), true)) {
                System.out.println("添加dex文件到apk中失败，退出！");
                return false;
            }
            System.out.println("添加dex文件到apk中结束===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Throwable e) {
            System.out.println("添加dex文件到apk中失败，退出！:" + e.toString());
            return false;
        }
    }

    /**
     * 签名apk文件
     */
    public static boolean signApk(String unSignedApkPath, Property property) {
        try {
            System.out.println("第十步==> 开始签名apk文件:" + unSignedApkPath);
            long time = System.currentTimeMillis();
            String keystore = property.getKeystore();
            File ketStoreFile=new File(keystore);
            File signFile = new File(keystore);
            if (!signFile.exists()) {
                System.out.println("签名文件:" + signFile.getAbsolutePath() + " 不存在，需要自己手动签名");
                return false;
            }
            StringBuilder signCmd = new StringBuilder("jarsigner");
            signCmd.append(" -verbose -keystore ");
            signCmd.append(property.getKeystore());
            signCmd.append(" -storepass ");
            signCmd.append(property.getPassword());
            signCmd.append(" -signedjar ");
            signCmd.append(property.getOutPakPath());
            signCmd.append(" ");
            signCmd.append(unSignedApkPath).append(" ");
            signCmd.append(ketStoreFile.getName());
            signCmd.append(" -digestalg SHA1 -sigalg MD5withRSA");
            System.out.println(signCmd.toString());
            execCmd(signCmd.toString(), false);
            System.out.println("签名apk文件结束===耗时:" + ((System.currentTimeMillis() - time) / 1000) + "s\n\n");
            return true;
        } catch (Throwable e) {
            System.out.println("重新签名apk文件失败，退出！:" + e.toString());
            return false;
        }
    }

    /**
     * 清理删除工作
     */
    public static void deleteTmpFile(String rootPath) {
        //删除解压之后的目录
        FileUtils.deleteDirectory(rootPath + Constant.unZipDir);
        //删除smali目录
        FileUtils.deleteDirectory(rootPath + Constant.smaliTmpDir);
        //删除临时dex文件
        FileUtils.deleteFile(rootPath + File.separator + "classes.dex");
    }

    /**
     * 执行命令
     *
     * @param cmd
     * @param isOutputLog
     * @return
     */
    public static boolean execCmd(String cmd, boolean isOutputLog) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (isOutputLog)
                    System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("cmd error:" + e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}