package site


import data.XncData
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

class Xnc(private val driver: WebDriver) {
    //private val salesListXncData = mutableListOf<XncData>()
    private val salesListXncData = mutableListOf<XncData>()
    fun start() {
        println("---Xnc START---")
        driver.get("https://www.xncmall.co.kr/shop/search_result.php?list_mode=3&cate=1002&ctype=big&sort=&search_str=&size1=&size2=&erange=3&un_tx=")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        saveToExcel()
        println("---Xnc END---")
    }

    fun getList() {
        var productIndex = 2
        while (productIndex <= 496) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/div/div/div[7]/div/div[1]/table/tbody/tr[$productIndex]")).text
                val splitTest = product.split(" ")
                val name = splitTest[0]
                val size = splitTest[1]
                val texture = splitTest[2]
                val price = splitTest[3]
                println("이름$name")
                println("규격$size")
                println("재질$texture")
                println("가격$price")
                salesListXncData.add(XncData(name, size, texture, price))
            } catch (e: Exception) {
                println("오류 발생($productIndex)")

            }
            productIndex += 2
        }
    }

    fun saveToExcel() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("SalesList")
        val headers = listOf("이름", "사이즈", "재질", "가격")

        val headerRow: Row = sheet.createRow(0)
        for (i in headers.indices) {
            val cell: Cell = headerRow.createCell(i)
            cell.setCellValue(headers[i])
        }

        for (i in salesListXncData.indices) {
            val salesItem = salesListXncData[i]
            val dataRow: Row = sheet.createRow(i + 1)
            dataRow.createCell(0).setCellValue(salesItem.name)
            dataRow.createCell(1).setCellValue(salesItem.size)
            dataRow.createCell(2).setCellValue(salesItem.texture)
            dataRow.createCell(3).setCellValue(salesItem.price)
        }

        val fileOut = FileOutputStream("$FILE_PATH\\Xncmall_suchang.xlsx")
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }

    fun quit() {
        driver.quit()
    }
}