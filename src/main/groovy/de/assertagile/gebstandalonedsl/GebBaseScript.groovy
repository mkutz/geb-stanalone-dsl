package de.assertagile.gebstandalonedsl

import geb.Browser
import geb.Configuration
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver

import java.lang.reflect.InvocationTargetException

class GebBaseScript extends Script {

    private final Configuration configuration = new Configuration()

    @Delegate
    private final Browser browser = new Browser(configuration)

    @Override
    Object run() {
        return true
    }

    void baseUrl(String baseUrl) {
        configuration.setBaseUrl(baseUrl)
    }

    void browser(Class<WebDriver> webDriverClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        WebDriverManager.getInstance(webDriverClass).setup()
        configuration.setDriver(webDriverClass.getConstructor().newInstance())
    }

    void drive(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = Browser.class) Closure driveClosure) {
        driveClosure.setDelegate(browser)
        driveClosure.call()
    }
}
