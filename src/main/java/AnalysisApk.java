import org.xmlpull.v1.XmlPullParser;
import xmlparse.AXmlResourceParser;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AnalysisApk {

    private static String enterActivityName = "";
    private static String actionName = "";
	private static String pkgName = "";
    private static String androidManifest = "AndroidManifest.xml";

    private final static String CATE_MAIN = "android.intent.action.MAIN";
    private final static String CATE_LAUNCHER = "android.intent.category.LAUNCHER";

	public static String getAppEnterApplication(String apkUrl) {
		boolean isLauncher = false;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(apkUrl));
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
			ZipEntry zipEntry;
			while (enumeration.hasMoreElements()) {
				zipEntry =  enumeration.nextElement();
				if (!zipEntry.isDirectory() && androidManifest.equals(zipEntry.getName())) {
					try {
						AXmlResourceParser parser = new AXmlResourceParser();
						parser.open(zipFile.getInputStream(zipEntry));
						while (true) {
							int type = parser.next();
							if (type == XmlPullParser.END_DOCUMENT) {
								break;
							}
							switch (type) {
								case XmlPullParser.START_TAG: {
									String tagName = parser.getName();
									if ("manifest".equals(tagName)) {
										for (int i = 0; i != parser.getAttributeCount(); ++i) {
											String attrName = parser.getAttributeName(i);
											if ("package".equals(attrName)) {
												pkgName = parser.getAttributeValue(i);
												Constant.appPkgName = pkgName;
											}
										}
									} else if ("application".equals(tagName)) {
										for (int i = 0; i != parser.getAttributeCount(); ++i) {
											String attrName = parser.getAttributeName(i);
											if ("name".equals(attrName)) {
												String appName = parser.getAttributeValue(i);
												if (appName.startsWith(".")) {
													Constant.isApplicationEntry = true;
													return pkgName + appName;
												}
												return appName;
											}
										}
									} else if ("activity".equals(tagName)) {
										isLauncher = false;
										for (int i = 0; i != parser.getAttributeCount(); ++i) {
											String attrName = parser.getAttributeName(i);
											if ("name".equals(attrName)) {
												enterActivityName = parser.getAttributeValue(i);
												break;
											}
										}
									} else if ("action".equals(tagName)) {
										for (int i = 0; i != parser.getAttributeCount(); ++i) {
											String attrName = parser.getAttributeName(i);
											if ("name".equals(attrName)) {
												actionName = parser.getAttributeValue(i);
												break;
											}
										}
									} else if ("category".equals(tagName)) {
										for (int i = 0; i != parser.getAttributeCount(); ++i) {
											String attrName = parser.getAttributeName(i);
											if ("name".equals(attrName)) {
												String categoryName = parser.getAttributeValue(i);
												if (CATE_LAUNCHER.equals(categoryName)) {
													isLauncher = true;
												}
												break;
											}
										}
									}
								}
								break;

								case XmlPullParser.END_TAG: {
									String tagName = parser.getName();
									if ("intent-filter".equals(tagName)) {
										if (CATE_MAIN.equals(actionName) && isLauncher) {
											if (enterActivityName.startsWith(".")) {
												Constant.isApplicationEntry = false;
												return pkgName + enterActivityName;
											}
											Constant.isApplicationEntry = false;
											return enterActivityName;
										}
									}
								}
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
		}
		return null;
	}
}