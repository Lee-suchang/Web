package ui

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import site.*
import tornadofx.box

class Controller {
    private lateinit var driver : WebDriver
    private lateinit var element: WebDriverWait

    private lateinit var box4u: BoxForYou
    private lateinit var mjBox: MjBox
    private lateinit var boxCorea: BoxCorea
    private lateinit var ybMall: YbMall
    private lateinit var thirdBox: ThirdBox
    private lateinit var xnc: Xnc

    fun InitDriver(driverPath: String)    {
        System.setProperty("webdriver.chrome.driver", driverPath)
        val options = ChromeOptions()
        options.setBinary("")
        options.addArguments("--start-maximized")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-default-apps")
        options.addArguments("--window-size=1920,1080")
        driver = ChromeDriver(options)
        element = WebDriverWait(driver, 10)

        box4u = BoxForYou(driver)
        mjBox = MjBox(driver)
        boxCorea = BoxCorea(driver)
        ybMall = YbMall(driver)
        thirdBox = ThirdBox(driver)
        xnc = Xnc(driver)
    }

    fun GetDriver() = driver
    fun GetElement() = element
    fun GetBox4u() = box4u
    fun GetmjBox() = mjBox
    fun GetBoxCorea() = boxCorea
    fun GetYbMall() = ybMall
    fun GetThirdBox() = thirdBox
    fun GetXnc() = xnc

}