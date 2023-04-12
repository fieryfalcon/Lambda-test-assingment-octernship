package testcases;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class generateHAR {

	public static void main(String[] args) throws InterruptedException, IOException {
		// 1. Start the proxy on some port
		BrowserMobProxy myProxy = new BrowserMobProxyServer();
		myProxy.start(0);

		// 2. Set SSL and HTTP proxy in SeleniumProxy
		Proxy seleniumProxy = new Proxy();
		seleniumProxy.setHttpProxy("localhost:" + myProxy.getPort());
		seleniumProxy.setSslProxy("localhost:" + myProxy.getPort());

		// 3. Add Capability for PROXY in DesiredCapabilities
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(CapabilityType.PROXY, seleniumProxy);
		capability.acceptInsecureCerts();
		capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

		// 4. Set captureTypes
		EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
		captureTypes.addAll(CaptureType.getCookieCaptureTypes());
		captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
		captureTypes.addAll(CaptureType.getRequestCaptureTypes());
		captureTypes.addAll(CaptureType.getResponseCaptureTypes());

		// 5. setHarCaptureTypes with above captureTypes
		myProxy.setHarCaptureTypes(captureTypes);

		// 6. HAR name
		myProxy.newHar("MyHAR");

		// 7. Start browser and open URL
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.merge(capability);
		WebDriver driver = new ChromeDriver(options);

		// Print Driver Capabilities
		System.out.println(
				"Driver Capabilities===> \n" + ((RemoteWebDriver) driver).getCapabilities().asMap().toString());

		driver.get("https://www.lambdatest.com");

		driver.manage().window().maximize();
		Thread.sleep(5000);

		List<WebElement> navLinks = driver.findElements(
				By.xpath("//*[@id='header']/nav/div/div/div[2]/div/div[1]//a[contains(@class,'display:block')]"));

		for (WebElement navLink : navLinks) {
			navLink.click();
			System.out.println("Text of the clicked nav-link: " + navLink.getText());
		}

		// 8. Find the header elements containing nav-link button and click on them

		Har har = myProxy.getHar();

		File directory = new File(System.getProperty("user.dir"));
		File myHARFile = new File(directory + "/MyHAR.har");
		har.writeTo(myHARFile);

		System.out.println("==> HAR details have been successfully written to the file.....");

		driver.quit();
		myProxy.stop();
	}
}