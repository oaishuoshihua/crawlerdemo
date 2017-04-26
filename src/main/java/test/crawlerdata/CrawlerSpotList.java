package test.crawlerdata;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import test.model.TicketModel;

import java.util.List;

/**
 * @author yuhp
 * @version 1.0.0
 * @date 2017/4/20
 *
 */
public class CrawlerSpotList {
    private AndroidDriver driver;
    CrawlerSpotList(AndroidDriver driver){
        this.driver=driver;
    }
    public void calwer() throws InterruptedException {
        TicketModel model=new TicketModel();
        System.out.println("start.......");
        boolean flag=true;
        while(flag) {
            try {
                List<WebElement> out = driver.findElements(By.xpath("//android.webkit.WebView//android.view.View"));
                if(out!=null){
                    for (int i = 0; i < out.size(); i++) {
                        String value=out.get(i).getAttribute("name");
                        model.setListData(value);
                        System.out.println(value);
                        //点击进入详情页
                        out.get(i).click();
                        Thread.sleep(2000);
                        getDeatilData(model,driver);
                    }
                    flag=false;
                }
            } catch (Exception e) {
                System.out.println("无法获取");
            }
        }
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());
    }
    public void getDeatilData(TicketModel model,AndroidDriver driver){

    }
}
