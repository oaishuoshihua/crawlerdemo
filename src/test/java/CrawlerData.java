import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import test.model.TicketModel;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuhp
 * @version 1.0.0
 * @date 2017/4/20
 */
public class CrawlerData {
    private AndroidDriver<WebElement> driver;
    private String currentActivity;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "HUAWEI");
        capabilities.setCapability("platformVersion", "6.0.1");
        capabilities.setCapability("appPackage", "com.sankuai.meituan");
        capabilities.setCapability("appActivity", ".activity.Welcome");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void calwer() throws InterruptedException {
        TicketModel model = new TicketModel();
        System.out.println("start.......");
        int count = 0;
        int spotCount = 0;
        boolean flag = true;
        while (flag) {
            try {
                List<WebElement> out = driver.findElements(By.xpath("//android.webkit.WebView//android.view.View"));
                System.out.println(driver.getPageSource());
                crawlerProcedure1(out, count, spotCount, model);
                // flag = false;
            } catch (Exception e) {
                System.out.println("无法获取景点列表");
            }
        }
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());
    }


    //获取详情页
    private void getDeatilData(TicketModel model, AndroidDriver driver) throws InterruptedException {
        int rangeContent = 1267;
        int k = 3;
        while (k > 0) {
            Thread.sleep(3000);
            WebElement element = null;
            try {
                System.out.println(driver.getPageSource());
                element = driver.findElement(By.name("门票"));
                System.out.println("门票坐标：" + element.getLocation().getX() + " ," + element.getLocation().getY());
            } catch (Exception e) {
                System.out.println("获取门票失败");
            }
            if (element != null) {
                driver.swipe(element.getLocation().getX(), element.getLocation().getY(), element.getLocation().getX(), 230, 1500);
                System.out.println("门票坐标：" + element.getLocation().getX() + " ," + element.getLocation().getY());
                Thread.sleep(1000);
//
                List<WebElement> type = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
//                List<WebElement> content=driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_click_sku']"));
//                List<WebElement> allShow = driver.findElements(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_show_all_text']"));

//                WebElement showAll = null;
//                if (allShow != null && allShow.size() > 0) {
//                    showAll = allShow.get(0);
//                }
                Thread.sleep(1000);
                if (type.size() > 0) {
                    WebElement item = type.get(0);
                    if (type.size() == 1) {

                    } else {
                        item.click();
                    }
                    Thread.sleep(1000);
                    List<WebElement> title = driver.findElements(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
                    Thread.sleep(1000);
                    for (int i = 0; title != null && i < title.size(); i++) {
                        WebElement cure = title.get(i);
                        clickEvent(cure);
                        Thread.sleep(1000);
                        goBackFromResourceDetail();
                    }
                    List<WebElement> showALLPrice = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_sku_more']"));
                    if (showALLPrice != null && showALLPrice.size() > 0) {
                        WebElement show = showALLPrice.get(0);
                        driver.swipe(show.getLocation().getX(), show.getLocation().getY(), show.getLocation().getX(), 230, 1500);
                        show.click();
                        List<WebElement> titles = driver.findElements(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
                        for (int i = 0; titles != null && i < title.size(); i++) {

                        }
                    }
                }
                break;
            }
            swipeUp(driver, 1000);
            k--;
        }
        goBackFromResourceList();
    }

    //等待activity
    public void waitforComplete(String currentActivity) throws InterruptedException {

        Thread.currentThread().sleep(2 * 1000);
        String nextAct = driver.currentActivity();
        //the activity is similar in the case
        for (int i = 0; nextAct.equalsIgnoreCase(currentActivity) && i < 4; i++) {
            Thread.currentThread().sleep(2 * 1000);
        }
    }

    //点击事件
    public boolean clickEvent(WebElement e277) throws InterruptedException {
        String currentActivity3 = driver.currentActivity();
        e277.click();
        waitforComplete(currentActivity3);
        if (driver.currentActivity().equals(currentActivity3)) {
            return false;
        } else {
            return true;
        }
    }

    //往上滑
    private void swipeUp(AndroidDriver driver, int during) {
        int time = during > 100 ? during : 1000;
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height * 3 / 5, width / 2, height / 5, time);
    }

    //列表页抓取
    private void crawlerProcedure(List<WebElement> out, int count, int spotCount, TicketModel model) throws InterruptedException {
        if (out != null) {
            for (; count < out.size(); count++) {
                WebElement e = out.get(count);
                String value = e.getAttribute("name").trim();
                System.out.println("滑动前：X:" + e.getLocation().getX() + " Y:" + e.getLocation().getY());
                String[] values = value.split(" ");

                if (values != null && values.length > 3) {
                    //WebElement temp = driver.findElement(By.xpath("//android.webkit.WebView//android.view.View[@content-desc='" + e.getAttribute("name") + "']"));
                    if (spotCount > 1) {

                        WebElement e1 = out.get(count - 1);
//                                driver.swipe(e.getLocation().getX(),e.getLocation().getY(),e.getLocation().getX(),2*e1.getLocation().getY()-e.getLocation().getY(),1000);
                        new TouchAction(driver).press(e).waitAction().moveTo(e1).release().perform();
                        Thread.sleep(5000);
                        out = driver.findElements(By.xpath("//android.webkit.WebView//android.view.View"));
                        e = out.get(count);
//                                e.getLocation().move(e.getLocation().getX(),e1.getLocation().getY());
                        Thread.sleep(2000);
                        System.out.println("滑动后：X:" + e.getLocation().getX() + " Y:" + e.getLocation().getY());
                        value = e.getAttribute("name");
                    }
                    model.setListData(value);
                    System.out.println(value);
                    currentActivity = driver.currentActivity();
                    int loop = 3;
                    //点击进入详情页
                    while (!clickEvent(e)) {
                        if (loop > 0) {
                            loop--;
                            System.out.println("循环景点列表出错!");
                        } else {
                            break;
                        }

                    }
                    getDeatilData(model, driver);
                    spotCount++;
                    Thread.sleep(2000);
                }

            }

        }
    }

    private void crawlerProcedure1(List<WebElement> out, int count, int spotCount, TicketModel model) throws InterruptedException {
        int range = 384;
        int clickX = 525;
        int clickY = 555;
        if (out != null) {
            for (; count < out.size(); count++) {
                WebElement e = out.get(count);
                String value = e.getAttribute("name").trim();
                System.out.println("滑动前：X:" + e.getLocation().getX() + " Y:" + e.getLocation().getY());
                String[] values = value.split(" ");

                if (values != null && values.length > 3) {
                    if (spotCount == 0) {
                        range = (((AndroidElement) e).getCenter().getY() - e.getLocation().getY()) * 2;
                        //driver.swipe(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getX(), 333, 1500);
                        System.out.println(((AndroidElement) e).getCenter().getX());
                        clickX = ((AndroidElement) e).getCenter().getX() == 0 ? clickX : ((AndroidElement) e).getCenter().getX();
                        System.out.println(((AndroidElement) e).getCenter().getY());
                        clickY = ((AndroidElement) e).getCenter().getX() == 0 ? clickY : ((AndroidElement) e).getCenter().getY();
                        spotCount++;
                    } else {
                        driver.swipe(clickX, clickY + range, clickX, clickY, 1500);
//                        driver.swipe(((AndroidElement) e).getCenter().getX(),((AndroidElement) e).getCenter().getY(),clickX,clickY,2000);
                        Thread.sleep(3000);
                        out = driver.findElements(By.xpath("//android.webkit.WebView//android.view.View"));
                    }
                    model.setListData(value);
                    System.out.println(value);
//                    int loop = 3;
                    //点击进入详情页
//                    while (!clickEvent(e)) {
//                        if (loop > 0) {
//                            loop--;
//                            System.out.println("循环景点列表出错!");
//                        } else {
//                            break;
//                        }
//
//                    }
//                    clickEvent(e);
                    try {
                        System.out.println("执行tap方法");
//                        new TouchAction(driver).tap(e).release().perform();
                        new TouchAction(driver).tap(clickX, clickY).release().perform();
                    } catch (Exception e1) {
//                        ((AndroidDriver)driver).tap(1, clickX, clickY, 1000);
                        System.out.println("调用click方法");
                        e.click();
                    }

                    getDeatilData(model, driver);
                    Thread.sleep(2000);
                }

            }

        }
    }

    //测试直接进入某个activity
    private void directActivity() throws InterruptedException {
        Thread.sleep(3000);
        WebElement el = driver.findElement(By.name("上海"));
        while (!el.isDisplayed()) {
            Thread.sleep(1000);
            el = driver.findElement(By.name("上海"));
        }
        el.click();
        Thread.sleep(3000);
        driver.startActivity("com.sankuai.meituan", "com.meituan.android.oversea.list.OverseaPoiListActivity");
    }

    //测试详情页demo
    private void detailDemo() throws InterruptedException {
        int k = 3;
        while (k > 0) {
            Thread.sleep(3000);
//            List<WebElement> element=driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
            List<WebElement> title = driver.findElements(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_title']"));

            if (title.size() > 0) {
                for (int j = 0; j < title.size(); j++) {//后面修改资源获取方式
                    System.out.println(title.get(j).getText());
                }
                break;
            }
            swipeUp(driver, 1000);
            k--;
        }

        boolean flag = true;
        while (flag) {
            WebElement goback = driver.findElementByAccessibilityId("转到上一层级");
            if (goback.isDisplayed()) {
                goback.click();
                flag = false;
            }
        }
    }

    //从资源列表页返回
    private void goBackFromResourceList() {
        boolean flag = true;
        while (flag) {
            WebElement goback = null;
            try {
                goback = driver.findElementByAccessibilityId("转到上一层级");
                if (goback.isDisplayed()) {
                    goback.click();
                    flag = false;
                }
            } catch (WebDriverException e) {
                System.out.println("返回失败");
            }

        }
    }

    //从资源详情页返回
    private void goBackFromResourceDetail() {
        boolean flag = true;
        while (flag) {
            WebElement goback = null;
            try {
                goback = driver.findElement(By.id("com.sankuai.meituan:id/button_ll"));
                if (goback.isDisplayed()) {
                    goback.click();
                    flag = false;
                }
            } catch (WebDriverException e) {
                System.out.println("返回失败");
            }
        }
    }

    //获取景点详情页
    private boolean getSpotDeatil(AndroidDriver driver) throws InterruptedException {
        scroll(3,"门票");
        Map<String, String> map = new HashMap<String, String>();
        Thread.sleep(2000);
        boolean result = true;
        List<WebElement> types = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
        if (types != null && types.size() > 0) {
            handleTicketTypeItem(driver, types.get(0), map);
        } else {
            result = false;
        }
        return result;
    }

    //点击门票类型
    private void handleTicketTypeItem(AndroidDriver driver, WebElement type, Map<String, String> items) throws InterruptedException {
        if (type == null) return;
        type.click();
        Thread.sleep(1000);
        List<WebElement> priceTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_group_sku_content']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
        handlePriceTypeItem(driver,priceTypes,items);
    }

    private void handlePriceTypeItem(AndroidDriver driver,List<WebElement> priceTypeList,Map<String,String> map){
        int range = 269;
        if (priceTypeList != null) {//如果还有列表
            for (int i = 0; i <priceTypeList.size() ; i++) {
                WebElement priceItem=priceTypeList.get(i);
                if(i==1){
                    range=priceItem.getRect().getHeight();
                }
                if(map.keySet().contains(priceItem.getAttribute("name"))){
                    continue;
                }else {
                    map.put(priceItem.getAttribute("name"),"true");
                    priceItem.click();
                }
            }
            driver.swipe(driver.manage().window().getSize().getWidth()/2,driver.manage().window().getSize().getHeight(),driver.manage().window().getSize().getWidth()/2,driver.manage().window().getSize().getHeight()-priceTypeList.size()>1?priceTypeList.size()-1*range:range,2000);
        } else {//需要判断是否有展示全部价格、有下一条票种、展开全部票种
            try {
                WebElement showAllPrice = driver.findElement(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_sku_more']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_more_text']"));
                if(showAllPrice!=null){
                    showAllPrice.click();
                    List<WebElement> priceTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_group_sku_content']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
                    handlePriceTypeItem(driver, priceTypes,map);
                }else {
                    List<WebElement> types = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
                    if (types != null && types.size() > 0) {
                        handleTicketTypeItem(driver, types.get(0), map);
                    }else {
                        try{
                            WebElement allShow = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_show_all_text']"));
                            allShow.click();
                            swipeUp(driver,2000);
                            List<WebElement> ticketTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
                            if (ticketTypes != null && ticketTypes.size() > 0) {
                                handleTicketTypeItem(driver, ticketTypes.get(0), map);
                            }
                        }catch (Exception e){
                            System.out.println("没有展开全部票种");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("没有查看全部报价");
            }
        }
    }

    //滚动几次查找某一名称
    private void scroll(int num,String elementName){
        int k = num;
        while (k > 0) {
            WebElement element = null;
            try {
                Thread.sleep(3000);
                element = driver.findElement(By.name("elementName"));
                if(element!=null){
                    break;
                }
            } catch (Exception e) {
                System.out.println("获取"+elementName+"失败");
            }
            k--;
        }
    }
}