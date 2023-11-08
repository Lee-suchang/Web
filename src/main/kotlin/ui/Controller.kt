package ui

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import site.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class Controller {
    private lateinit var driver: WebDriver
    private lateinit var element: WebDriverWait

    private lateinit var box4u: BoxForYou
    private lateinit var mjBox: MjBox
    private lateinit var boxCorea: BoxCorea
    private lateinit var ybMall: YbMall
    private lateinit var thirdBox: ThirdBox
    private lateinit var xnc: Xnc
    private var driverPath: String = ""
    private var excelPath: String = ""

    fun getSettingFile() {
        try {
            val driverPathBufferReader = File(SETTING_FILE_PATH).bufferedReader()
            driverPath = driverPathBufferReader.readLine().toString()
            excelPath = driverPathBufferReader.readLine().toString()
            println(driverPath)
            println(excelPath)
        } catch (e: Exception) {
            val writer = BufferedWriter(FileWriter(File(FILE_PATH, "setting.txt")))
            writer.close()
        }
    }

    fun getDriverPath(): String = driverPath
    fun getExcelPath(): String = excelPath

    fun initDriver(driverPath: String) {
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

    fun getDriver() = driver
    fun getElement() = element
    fun getBox4u() = box4u
    fun getMjBox() = mjBox
    fun getBoxCorea() = boxCorea
    fun getYbMall() = ybMall
    fun getThirdBox() = thirdBox
    fun getXnc() = xnc
    fun quit() = driver.quit()


}