package test;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by yuhp on 2017/3/21.
 */
public class helloworld {
    public static void main(String[] args) throws IOException {
        String test="{\"rId\":100018,\"ts\":1492739043866,\"cts\":1492739044964,\"brVD\":[360,519],\"brR\":[[360,640],[360,640],32,32],\"bI\":[\"https://osx.dianping.com/app/app-overseas-peon/detail.html?skuId=1169058&cityId=341&shopId=2037883&f=android&utm_source=huawei1&utm_medium=android&utm_term=493&version_name=7.9.3&utm_content=861575037001103&utm_campaign=AgroupBgroupC0E0&ci=10&msid=8615750370011031492736936299&uuid=825870E32687D9EC7ADE387B607E0F28532C5D6EC6C963A4573DDEE79121B367&userid=-1\",\"\"],\"mT\":[],\"kT\":[],\"aT\":[],\"tT\":[],\n" +
                "\"sign\":\"eJwNyksKgCAUQNG9NHjD8GUfHbwFtIoQPyWVilrQ7nNy4cDttCdkoH39VkN8RHCkgsnRG7hUJQZX2FtTg4v5JkQoR0xtHhhfhOBQzqcJcZZsEvDaXHwMW1C3paWXPe9+Mmce/g==\"}";
        String result=encodeRedirectFormat(test);
        System.out.println(result);
        System.out.println(getSign());
    }
    public static String encodeRedirectFormat( String samlXML ) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(os);
        deflaterOutputStream.write( samlXML.getBytes( "UTF-8" ) );
        deflaterOutputStream.close();
        os.close();
        String base64 = Base64.encodeBase64String(os.toByteArray());
        String result=URLEncoder.encode(base64, "UTF-8" );
        return result;
    }

    public static  String getSign() throws IOException {
        String sign="\"ci=10&cityId=341&f=android&lat=0&lng=0&platform=11&shopId=2037883&skuId=1169058&version_name=7.9.3\"";
        sign=encodeRedirectFormat(sign);
        return sign;
    }
}
