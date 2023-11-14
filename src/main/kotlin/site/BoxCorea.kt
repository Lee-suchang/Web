package site

import data.BoxCoreaData
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import ui.FILE_PATH
import java.io.FileOutputStream

class BoxCorea(private val driver: WebDriver) {
    private val boxCoreaList = mutableListOf<BoxCoreaData>()
    fun getList2() = boxCoreaList
    fun start() {
        println("---BOXCOREA START---")
        driver.get("https://boxcorea.co.kr/product/list.html?cate_no=96")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        saveToExcel()

        println("---BOXCOREA END---")
    }

    fun getList() {
        var productIndex = 1
        while (true) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[2]/div[2]/div[2]/table/tbody/tr[$productIndex]/td[2]/a/ul")).text
                println(product)
                println("프로덕트")

                val sizeRegex = Regex("\\b\\d+\\s*X\\s*\\d+\\s*X\\s*\\d+\\b")
                val sizeMatch = sizeRegex.find(product)
                val sizeWithSpaces = sizeMatch?.value ?: "사이즈 정보 없음"
                val size = sizeWithSpaces.replace(" ", "").replace("X", "x", ignoreCase = true)

                val splitTest = product.split(" ")
                val name = splitTest[0]
                val priceParts = product.split("원")
                val price = priceParts[1]

                println("------------")
                println("$productIndex 번째 상품")
                println("이름 : $name")
                println("사이즈 : $size")
                println("가격 : $price")
                println("------------")

                boxCoreaList.add(BoxCoreaData(name, size, price))
            } catch (e: org.openqa.selenium.NoSuchElementException) {
                // WebElement를 찾을 수 없을 때
                println("요소를 찾을 수 없음")
            } catch (e: Exception) {
                // 다른 예외 처리
                println("예외 발생: ${e.message}")
            }

            if (productIndex > 333) {
                break
            }
            productIndex++
        }
    }

    fun saveToExcel() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("박스코리아")
        val headers = listOf("이름", "사이즈", "가격")

        val headerRow: Row = sheet.createRow(0)
        for (i in headers.indices) {
            val cell: Cell = headerRow.createCell(i)
            cell.setCellValue(headers[i])
        }

        for (i in boxCoreaList.indices) {
            val salesItem = boxCoreaList[i]
            val dataRow: Row = sheet.createRow(i + 1)
            dataRow.createCell(0).setCellValue(salesItem.name)
            dataRow.createCell(1).setCellValue(salesItem.size)
            dataRow.createCell(2).setCellValue(salesItem.price)
        }

        val fileOut = FileOutputStream("$FILE_PATH\\boxcorea_suchang.xlsx")
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }

    fun quit() {
        driver.quit()
    }
}