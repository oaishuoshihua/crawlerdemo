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
import test.utils.GetDriverInstance;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuhp
 * @version 1.0.0
 * @date 2017/4/26
 * @copyright ctrip
 */
public class SpotListAndDetail {
    private AndroidDriver<WebElement> driver;
    private String currentActivity;

    @Before
    public void setUp() throws Exception {
       this.driver= GetDriverInstance.getDriverInstance();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void calwer() throws InterruptedException {
        intoSpotList(driver);
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


    // 爬取景点列表页
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
                        System.out.println(((AndroidElement) e).getCenter().getX());
                        clickX = ((AndroidElement) e).getCenter().getX() == 0 ? clickX : ((AndroidElement) e).getCenter().getX();
                        System.out.println(((AndroidElement) e).getCenter().getY());
                        clickY = ((AndroidElement) e).getCenter().getX() == 0 ? clickY : ((AndroidElement) e).getCenter().getY();
                        spotCount++;
                    } else {
                        driver.swipe(clickX, clickY + range, clickX, clickY, 1500);
                        Thread.sleep(3000);
                        out = driver.findElements(By.xpath("//android.webkit.WebView//android.view.View"));
                    }
                    model.setListData(value);
                    System.out.println(value);

                    try {
                        System.out.println("执行tap方法");
                        new TouchAction(driver).tap(clickX, clickY).release().perform();
                    } catch (Exception e1) {
                        System.out.println("调用click方法");
                        e.click();
                    }
                    getSpotDeatil(driver);
                }

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
        boolean result = true;
        if(!scroll(3,"门票")){
            goBackFromResourceList();//返回景点列表
            return result;
        }
        Map<String, String> map = new HashMap<String, String>();
        Thread.sleep(2000);
        List<WebElement> types = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
        if (types != null && types.size() > 0) {
            if(types.size()>1)
                handleTicketTypeItem(driver, types.get(0), map,false);
            else
                handleTicketTypeItem(driver, types.get(0), map,true);

        } else {
            result = false;
        }
        return result;
    }

    //点击门票类型
    private void handleTicketTypeItem(AndroidDriver driver, WebElement type, Map<String, String> items,boolean flag) throws InterruptedException {
        if (type == null) return;
        if(flag){
            type.click();
        }
        Thread.sleep(1000);
        List<WebElement> priceTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_group_sku_content']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
        handlePriceTypeItem(driver,priceTypes,items);
    }

    //点击价格类型列表
    private void handlePriceTypeItem(AndroidDriver driver,List<WebElement> priceTypeList,Map<String,String> map) throws InterruptedException {
        int range = 269;
        if (priceTypeList != null) {//如果还有列表
            for (int i = 0; i <priceTypeList.size() ; i++) {
                WebElement priceItem=priceTypeList.get(i);
                if(map.keySet().contains(priceItem.getAttribute("name"))){
                    continue;
                }else {
                    map.put(priceItem.getAttribute("name"),"true");
                    priceItem.click();
                    Thread.sleep(1000);
                    goBackFromResourceDetail();
                }
            }
            driver.swipe(driver.manage().window().getSize().getWidth()/2,driver.manage().window().getSize().getHeight()*3/4,driver.manage().window().getSize().getWidth()/2,driver.manage().window().getSize().getHeight()-priceTypeList.size()>1?(priceTypeList.size()-1)*range:range,2000);
        } else {//需要判断是否有展示全部价格、有下一条票种、展开全部票种
                WebElement showAllPrice=null;
                for (int i = 0; i <3 ; i++) {
                    try{
                        showAllPrice = driver.findElement(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_sku_more']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_more_text']"));
                        if(showAllPrice!=null){
                            break;
                        }
                    }catch (Exception e){
                        System.out.println("没有查看全部报价");
                    }
                }
                if(showAllPrice!=null){
                    showAllPrice.click();
                    Thread.sleep(2000);
                    List<WebElement> priceTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_spu_group_sku_content']//android.widget.TextView[@resource-id='com.sankuai.meituan:id/oversea_ticket_title']"));
                    handlePriceTypeItem(driver, priceTypes,map);
                }else {
                    List<WebElement> types = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
                    if (types != null && types.size() > 0) {
                        handleTicketTypeItem(driver, types.get(0), map,true);
                    }else {
                        for (int i = 0; i < 3; i++) {
                            try{
                                WebElement allShow = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.sankuai.meituan:id/trip_oversea_show_all_text']"));
                                if(allShow!=null){
                                    allShow.click();
                                    swipeUp(driver,2000);
                                    List<WebElement> ticketTypes = driver.findElements(By.xpath("//android.widget.LinearLayout[@resource-id='com.sankuai.meituan:id/trip_oversea_scenic_spu']"));
                                    if (ticketTypes != null && ticketTypes.size() > 0) {
                                        handleTicketTypeItem(driver, ticketTypes.get(0), map,true);
                                    }
                                    break;
                                }
                            }catch (Exception e){
                                System.out.println("没有展开全部票种");
                            }
                        }

                    }
                }
        }
    }

    //滚动几次查找某一名称
    private boolean scroll(int num,String elementName){
        boolean flag=false;
        int k = num;
        while (k > 0) {
            WebElement element = null;
            try {
                Thread.sleep(3000);
                element = driver.findElement(By.name(elementName));
                if(element!=null){
                    driver.swipe(element.getLocation().getX(), element.getLocation().getY(), element.getLocation().getX(), driver.manage().window().getSize().getHeight()/4, 1500);
                    flag=true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("获取"+elementName+"失败");
            }
            swipeUp(driver, 1000);
            k--;
        }
        return flag;
    }

    private void intoSpotList(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(3000);
        boolean flag=true;
        while (flag){
            try {
                WebElement  address = driver.findElementByName("北京");
                if(address!=null){
                    address.click();
                    flag=false;
                }
            } catch (Exception e) {
                System.out.println("can't get address");
            }
        }
        Thread.sleep(2000);
       flag=true;
        while(flag){
            try {
                WebElement  all = driver.findElementByName("旅游出行");
                if(all!=null){
                    all.click();
                    flag=false;
                }
            } catch (Exception e) {
                System.out.println("can't get all classify");
            }
        }
        Thread.sleep(5000);
        flag=true;
        while(flag){
            try {
                List<WebElement> element = driver.findElements(By.xpath("//android.view.View[contains(@content-desc,'出境游')]"));
                if(element.size()>0){
                    WebElement jz=element.get(0);
                    if(jz.isDisplayed()){
                        jz.click();
                        flag=false;
                        System.out.println("点击出境游");
                    }
                }
            } catch (Exception e) {
                System.out.println("can't get outplay");
            }
        }
        Thread.sleep(5000);
        flag=true;
        while(flag){
            try {
                Thread.sleep(1000);
                WebElement element = driver.findElementByName("景点门票");
                if(element!=null){
                    element.click();
                    flag=false;
                }
            } catch (Exception e) {
                System.out.println("can't get spotTicket");
            }
        }
    }
}
