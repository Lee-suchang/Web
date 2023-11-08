package ui

import javafx.scene.control.ScrollPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import site.*
import tornadofx.*
import kotlin.system.exitProcess

val FILE_PATH = System.getProperty("user.dir")

class MainView : View() {
   // private lateinit var driver: WebDriver
    //private lateinit var element: WebDriverWait
    private val fileChooser = FileChooser()
    private var selectedFile: String? = ""

   /* private lateinit var box4u: BoxForYou
    private lateinit var mjBox: MjBox
    private lateinit var boxCorea: BoxCorea
    private lateinit var ybMall: YbMall
    private lateinit var thirdBox: ThirdBox
    private lateinit var xnc: Xnc*/
    private val controller = Controller()

    override val root =
        fieldset {

            button("드라이버 경로") {
                action {
                    selectedFile = fileChooser.showOpenDialog(Stage())?.path.toString()
                    println(selectedFile)
                    if (selectedFile == "")
                        selectedFile = "$FILE_PATH\\chromedriver.exe"
                    println("경로 : $selectedFile")
                    controller.InitDriver(selectedFile.toString())
                }
            }

            val textArea = textarea {
                prefHeight = 200.0
                prefWidth = 200.0
            }
            val scrollPane = scrollpane {
                content = textArea
                vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
                prefHeight = textArea.prefHeight
                isFitToWidth = true
                prefWidth = textArea.prefWidth
            }


            //비닐 실행
            hbox {
                button("vinyl") {
                    hboxConstraints {
                        marginTop = 10.0
                        marginLeft = 10.0
                        marginBottom = 10.0

                    }
                    runAsync {
                        action {
                           /* if (selectedFile == "")
                                selectedFile = "$FILE_PATH\\chromedriver.exe"
                            println("경로 : $selectedFile")
                            System.setProperty("webdriver.chrome.driver", selectedFile)
                            val options = ChromeOptions()
                            options.setBinary("")
                            options.addArguments("--start-maximized")
                            options.addArguments("--disable-popup-blocking")
                            options.addArguments("--disable-default-apps")
                            options.addArguments("--window-size=1920,1080")
                            driver = ChromeDriver(options)
                            element = WebDriverWait(driver, 10)

                            xnc = Xnc(driver)*/
                        }
                    }
                }

                button("box") {
                    hboxConstraints {
                        marginTop = 10.0
                        marginLeft = 10.0
                        marginBottom = 10.0

                    }
                    //mjBox boxCorea ybMall thirdBox Xnc
                    runAsync {
                        action {
                           /* if (selectedFile == "")
                                selectedFile = "$FILE_PATH\\chromedriver.exe"
                            println("경로 : $selectedFile")
                            System.setProperty("webdriver.chrome.driver", selectedFile)
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
                            thirdBox = ThirdBox(driver)*/



                            /*mjBox.getList2().forEach {
                                textArea.appendText(
                                    "이름 : ${it.name}\n" +
                                            "사이즈 ; ${it.size}\n포장수 : ${it.paperNumber}\n" +
                                            "가격 : ${it.price}\n장당가격 : ${it.totalPrice}\n\n"
                                )
                            }*/
                            textArea.appendText("MJMALL START\n")
                            //
//                            mjBox.Start()
                            //
                            textArea.appendText("MJMALL END\n")
                            textArea.appendText("BOXCOREA START\n")
                            //
//                            boxCorea.start()
                            //
                            textArea.appendText("BOXCOREA END\n")
                            textArea.appendText("3SK START\n")
                            //
//                            thirdBox.start()
                            //
                            textArea.appendText("3SK END\n")
                            textArea.appendText("YBBOXMALL START\n")
                            //
//                            ybMall.start()
                            //
                            textArea.appendText("YBBOXMALL END\n")
                            textArea.appendText("BOXFORYOU START\n")
                            //
//                            box4u.start()
                            //
                            textArea.appendText("BOXFORYOU END\n")
                            //
                            textArea.appendText("Xncmall START\n")
                            controller.GetXnc().start()
                            textArea.appendText("Xncmall END\n")
                            //

                        }
                    }
                }

                button("종료") {
                    hboxConstraints {
                        marginTop = 10.0
                        marginLeft = 10.0
                        marginBottom = 10.0
                    }
                    action {
                        try {
                            exitProcess(0)
                        } catch (e: Exception) {
                            exitProcess(0)
                        }

                    }
                }
            }
        }
}