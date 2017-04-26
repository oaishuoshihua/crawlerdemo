package test.utils;


import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

/**
 * @author yuhp
 * @version 1.0.0
 * @date 2017/4/19
 *
 */
public final class GetDriverInstance {
    private static AndroidDriver<WebElement> driver = null;

    public synchronized static AndroidDriver<WebElement> getDriverInstance() throws Exception {
        if (driver == null) {
            //设置自动化相关参数
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "MI 4W");
            //        capabilities.setCapability("deviceName", "android4.3");
            //        capabilities.setCapability("deviceName", "GALAXY S5");
            //设置安卓系统版本
//            capabilities.setCapability("platformVersion", "6.0.1");
                    capabilities.setCapability("platformVersion", "4.4.4");
            //设置app的主包名和主类名
            capabilities.setCapability("appPackage", "com.sankuai.meituan");
            capabilities.setCapability("appActivity", ".activity.Welcome");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        }
        return driver;
    }
}
