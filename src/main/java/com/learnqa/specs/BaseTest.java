package com.learnqa.specs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class BaseTest {
    protected static final String BROWSER = System.getProperty("BROWSER", "chrome");
    protected static final boolean REMOTE_DRIVER = Boolean.valueOf(System.getProperty("REMOTE_DRIVER", "false"));
    protected static final String SELENIUM_HOST = System.getProperty("SELENIUM_HOST", "localhost");
    protected static final int SELENIUM_PORT = Integer.valueOf(System.getProperty("SELENIUM_PORT", "4444"));
    protected static final boolean CHROME_HEADLESS_MODE = Boolean.valueOf(System.getProperty("CHROME_HEADLESS_MODE", "true"));
    protected static final boolean FIREFOX_HEADLESS_MODE = Boolean.valueOf(System.getProperty("FIREFOX_HEADLESS_MODE", "true"));
    public static final String WEB_SERVER = System.getProperty("WEB_SERVER", "https://duckduckgo.com/");
    public static ThreadLocal<RemoteWebDriver> driverTl = new ThreadLocal<RemoteWebDriver>();
    public static RemoteWebDriver driver;
    private BrowserMobProxy proxy = new BrowserMobProxyServer();
    private File harFile ;

    @BeforeClass
    public void setupWebDriver() throws MalformedURLException {
        // start the proxy
        harFile =  new File("./SeleniumEasy" + Thread.currentThread().getId() + ".har");
        proxy.start(new Random().nextInt(65534));

        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        if (REMOTE_DRIVER) {
            setupRemoteDriver(capabilities);
            getDriver().setFileDetector(new LocalFileDetector());
        } else {
            setupLocalDriver(capabilities);
        }
        getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        getDriver().manage().window().maximize();
    }

    private void setupLocalDriver(DesiredCapabilities caps) {
        if (("firefox").equals(BROWSER)) {
            System.setProperty("webdriver.gecko.driver", copyDriver("geckodriver"));
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            if(FIREFOX_HEADLESS_MODE){
                firefoxBinary.addCommandLineOptions("--headless");
            }
            FirefoxOptions firefoxOptions = new FirefoxOptions(caps);
            firefoxOptions.setBinary(firefoxBinary);
            driver = new FirefoxDriver(firefoxOptions);
        } else if (("chrome").equals(BROWSER)) {
            System.setProperty("webdriver.chrome.driver", copyDriver("chromedriver"));
            ChromeOptions options = new ChromeOptions();
            caps.setAcceptInsecureCerts(true);
            options.merge(caps);
            if(CHROME_HEADLESS_MODE){
                options.addArguments("--headless");
            }
            options.addArguments("window-size=1024,768", "--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--allow-insecure-localhost");
            driver = new ChromeDriver(options);
        } else {
            throw new RuntimeException("Browser type unsupported");
        }
        driverTl.set(driver);
        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        // create a new HAR with the label "login"
        proxy.newHar("login");
//        getDriver().get(WEB_SERVER);
    }

    public static RemoteWebDriver getDriver(){
        return driverTl.get();
    }

    private String copyDriver(String driverName) {
        String OS = getOSName();
        String tempDir = System.getProperty("java.io.tmpdir");
        Path resolve = Paths.get(tempDir).resolve(driverName + OS);
        if(!resolve.toFile().exists()) {
            try (
                    InputStream stream = getClass().getResourceAsStream("/drivers/" + driverName + OS);
            ) {
                Files.copy(stream, resolve);
                File file = resolve.toFile();
                file.setExecutable(true);
                file.deleteOnExit();
            } catch (Exception e) {
                File file = resolve.toFile();
                file.setExecutable(true);
                file.deleteOnExit();
//                throw new IllegalStateException(e);
            }
        }
        return resolve.toString();
    }

    private String getOSName(){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            OS = ".exe";
        } else if (OS.indexOf("mac") >= 0) {
            OS = "-mac";
        } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0
                || OS.indexOf("sunos") >= 0) {
            OS = "-linux";
        } else {
            throw new RuntimeException("OS type unsupported");
        }
        return OS;
    }

    private void setupRemoteDriver(DesiredCapabilities caps) throws MalformedURLException {
        DesiredCapabilities capabilities;
        if ("firefox".equals(BROWSER)) {
            capabilities = DesiredCapabilities.firefox();
        } else if ("internetExplorer".equals(BROWSER)) {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        } else if ("chrome".equals(BROWSER)) {
            capabilities = DesiredCapabilities.chrome();
        } else {
            throw new RuntimeException("Browser type unsupported");
        }
        driver = new RemoteWebDriver(
                new URL("http://" + SELENIUM_HOST + ":" + SELENIUM_PORT + "/wd/hub"),
                capabilities);
        driverTl.set(driver);
//        getDriver().get(WEB_SERVER);
    }

    @AfterClass
    public void suiteTearDown() {
        // Write HAR Data in a File
        Har har = proxy.getHar();
        try {
            har.writeTo(harFile);
        } catch (IOException ex) {
            System.out.println (ex.toString());
        }
        proxy.stop();
        getDriver().manage().deleteAllCookies();
        getDriver().close();
//        getDriver().quit();
    }

}
