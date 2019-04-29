package de.assertagile.gebstandalonedsl;

import geb.Browser;
import geb.Configuration;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.Script;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;

public class GebBaseScript extends Script {

    private final Configuration configuration = new Configuration();
    private final Browser browser = new Browser(configuration);

    @Override
    public Object run() {
        return true;
    }

    public void browser(Class<WebDriver> webDriverClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        WebDriverManager.getInstance(webDriverClass).setup();
        configuration.setDriver(webDriverClass.getConstructor().newInstance());
    }

    public void drive(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = Browser.class) Closure drive) {
        drive.setDelegate(browser);
        drive.call();
    }
}
