package site

import data.BoxForYouData
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions
import ui.FILE_PATH
import java.io.FileOutputStream

class BoxForYou(private val driver: WebDriver) {
    private val salesList = mutableListOf<BoxForYouData>()

    fun start() {
        println("---BOX4U START---")
        driver.get("https://www.box4u.co.kr/shop/goods/goods_list.php?category=001&sort=size1&page_num=30&s_word=&ea%5B%5D=1")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        salesList
        println("---BOX4U END---")
    }

    fun getList() {
        var pageIndex = 1
        var tableNumber = 10
        while (true) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table[2]/tbody/tr[2]/td/table/tbody/tr/td[2]/div[1]/form[1]/table[$tableNumber]")).text
                val productSplit: MutableList<String> = product.split("\n") as MutableList<String>

                productSplit[2] =
                    productSplit[2].substring(productSplit[2].indexOf("매") + 1, productSplit[2].indexOf("원") + 1)
                productSplit[2] = productSplit[2].replace("원", "").trim() // "원" 제거 및 공백 제거
                productSplit[1] = productSplit[1].replace(" ", "") // 공백 제거

                salesList.add(
                    BoxForYouData(
                        number = productSplit[0],
                        size = productSplit[1],
                        price = productSplit[2]
                    )
                )
            } catch (e: Exception) {

            }

            tableNumber++
            if (tableNumber > 34) {
                tableNumber = 5
                pageIndex++
                driver.get("https://www.box4u.co.kr/shop/goods/goods_list.php?category=001&sort=size1&page_num=30&ea[0]=1&page=$pageIndex")
            }
            if (pageIndex == 9) {
                salesList.forEach {
                    println("Box4u품번 : " + it.number)
                    println("사이즈 : " + it.size)
                    println("가격 : " + it.price)
                    println()
                }
                saveToExcel() // 엑셀 파일 저장
                return
            }
        }
    }

    fun saveToExcel() {
        try {
            // 엑셀 워크북 및 시트 생성
            val workbook: Workbook = XSSFWorkbook()
            val sheet: XSSFSheet = workbook.createSheet("SalesData") as XSSFSheet

            // 엑셀에 데이터 작성
            val headerRow: XSSFRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Box4u품번")
            headerRow.createCell(1).setCellValue("사이즈")
            headerRow.createCell(2).setCellValue("가격")

            for ((index, sales) in salesList.withIndex()) {
                val row: XSSFRow = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(sales.number)
                row.createCell(1).setCellValue(sales.size)
                row.createCell(2).setCellValue(sales.price)
            }

            // 엑셀 파일로 저장
            val excelFile = FileOutputStream("$FILE_PATH\\Box4u_suchang.xlsx") // 경로 수정
            workbook.write(excelFile)
            excelFile.close()

            println("엑셀 파일이 성공적으로 저장되었습니다.")
        } catch (e: Exception) {
            println("엑셀 파일 저장 중 오류 발생: ${e.message}")
        }
    }

    fun quit() = driver.quit()
}