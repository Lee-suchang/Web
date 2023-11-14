package site

import data.YbData
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

class YbMall(private val driver: WebDriver) {
    private val salesListbk = mutableListOf<YbData>()

    fun start() {
        println("---YB START---")
        driver.get("https://ydboxmall.com/goods/catalog?page=1&searchMode=catalog&undefined=307&category=c0001&per=50&sorting=ranking&filter_display=lattice")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        saveToExcel()
        println("---YB END---")
    }

    fun getList() {
        var productIndex = 1
        while (true) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[$productIndex]")).text
                val splitTest = product.split(" ")
                val name = splitTest[0]
                val size = splitTest[1].replace("*", "x")
                val price = splitTest[5]
                println("이름$name")
                println("규격$size")
                println("가격$price")
                salesListbk.add(YbData(name, size, price))
            } catch (e: Exception) {

                println("오류 한번발생")
                if (productIndex > 308) {
                    break
                }
            }
            productIndex++
        }
    }

    fun saveToExcel() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Yd박스몰")
        val headers = listOf("이름", "사이즈", "가격")

        val headerRow: Row = sheet.createRow(0)
        for (i in headers.indices) {
            val cell: Cell = headerRow.createCell(i)
            cell.setCellValue(headers[i])
        }

        for (i in salesListbk.indices) {
            val salesItem = salesListbk[i]
            val dataRow: Row = sheet.createRow(i + 1)
            dataRow.createCell(0).setCellValue(salesItem.name)
            dataRow.createCell(1).setCellValue(salesItem.size)
            dataRow.createCell(2).setCellValue(salesItem.price)
        }

        val fileOut = FileOutputStream("$FILE_PATH\\ydboxmall_suchang.xlsx")
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }

    fun quit() {
        driver.quit()
    }
}