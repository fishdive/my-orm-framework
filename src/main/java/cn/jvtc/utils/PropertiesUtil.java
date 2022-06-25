package cn.jvtc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Properties util.
 *
 * @author 雷族
 */
public class PropertiesUtil {
    private final Properties PROP = new Properties();

    private String propertiesName;

    /**
     * Instantiates a new Properties util.
     *
     * @param propertiesName 资源文件全路径名
     * @throws IOException the io exception
     */
    public PropertiesUtil(String propertiesName) {
        this.propertiesName = propertiesName;
        try {
            InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(this.propertiesName);
            PROP.load(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Gets prop value.
     *
     * @param propParam 键名
     * @return 值
     * @throws IOException the io exception
     */
    public String getPropValue(String propParam) {
        return PROP.getProperty(propParam);
    }
}
