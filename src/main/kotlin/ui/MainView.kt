package ui

import javafx.scene.control.ScrollPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import tornadofx.*
import kotlin.system.exitProcess

val SETTING_FILE_PATH = System.getProperty("user.dir").toString() + "\\setting.txt"
val FILE_PATH = System.getProperty("user.dir")

class MainView : View() {
    private val fileChooser = FileChooser()
    private var selectedFile: String? = ""
    private val controller = Controller()
    private var driverPath = ""
    private var excelPath = ""

    init {
        controller.getSettingFile()
        driverPath = controller.getDriverPath() + "\\chromedriver.exe"
        excelPath = controller.getExcelPath()
        if (driverPath != "\\chromedriver.exe")
            controller.initDriver(driverPath.toString())
        if (excelPath == "")
            excelPath = System.getProperty("user.dir").toString() + "\\excel"
    }

    override val root =
        fieldset {

            button(driverPath) {
                action {
                    selectedFile = fileChooser.showOpenDialog(Stage())?.path.toString()
                    println(selectedFile)
                    if (selectedFile == "")
                        selectedFile = "$FILE_PATH\\chromedriver.exe"
                    println("경로 : $selectedFile")
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

                    action {
                        runAsync {
                            try {
                                textArea.appendText("Xncmall START\n")
                                controller.getXnc().start()
                                textArea.appendText("Xncmall END\n")
                            } catch (e: Exception) {
                                textArea.appendText("Xncmall ERROR\n")
                            }
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

                    action {
                        runAsync {
                            /*textArea.appendText("MJMALL START\n")
                            textArea.appendText("MJMALL END\n")
                            textArea.appendText("BOXCOREA START\n")
                            textArea.appendText("BOXCOREA END\n")
                            textArea.appendText("3SK START\n")
                            textArea.appendText("3SK END\n")
                            textArea.appendText("YBBOXMALL START\n")
                            textArea.appendText("YBBOXMALL END\n")
                            textArea.appendText("BOXFORYOU START\n")
                            textArea.appendText("BOXFORYOU END\n")
                            textArea.appendText("Xncmall END\n")
                            */
                            try {
                                textArea.appendText("Box4u START\n")
                                controller.getBox4u().start()
                                textArea.appendText("Box4u END\n")
                            } catch (e: Exception) {
                                textArea.appendText("Box4u ERROR\n")}
                                //
                            try {
                                textArea.appendText("BoxCorea START\n")
                                controller.getBoxCorea().start()
                                textArea.appendText("BoxCorea END\n")
                            } catch (e: Exception) {
                                textArea.appendText("BoxCorea ERROR\n")}

                                //
                            try {
                                textArea.appendText("mj START\n")
                                controller.getMjBox().start()
                                textArea.appendText("mj END\n")
                            } catch (e: Exception) {
                                textArea.appendText("mj ERROR\n")}
                                //
                            try {
                                textArea.appendText("3sk START\n")
                                controller.getThirdBox().start()
                                textArea.appendText("3sk END\n")
                            } catch (e: Exception) {
                                textArea.appendText("3sk ERROR\n")}
                                //
                            try {
                                textArea.appendText("yd START\n")
                                controller.getYbMall().start()
                                textArea.appendText("yd END\n")
                            } catch (e: Exception) {
                                textArea.appendText("yd ERROR\n")}
                            }


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
                            controller.quit()
                            exitProcess(0)
                        } catch (e: Exception) {
                            exitProcess(0)
                        }

                    }
                }
            }
        }