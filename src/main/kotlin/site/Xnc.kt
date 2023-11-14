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

        // 허용된 텍스처 값의 목록 정의
        val validTextures = listOf("HDPE", "OPP", "PP", "크라프트", "LDPE", "LDPE (투명)", "LDPE(반투명)")
// 제거하고 싶은 문자열 패턴
        val patternsToRemove = listOf("A4", "A5", "PS3", "A5", "A6", "B6")
        while (productIndex <= 496) {
            try {
                val product = driver.findElement(By.xpath("/html/body/div/div/div[7]/div/div[1]/table/tbody/tr[$productIndex]")).text

                val splitTest = product.split("\n")
                val splitTest2 = product.split(" ")

                val name = splitTest2[0]
                var size = splitTest2.slice(1..5).joinToString(" ")

                for (pattern in patternsToRemove) {
                    size = size.replace(pattern, "")
                }

                size = size.replace(Regex("[^\\dx+]"), "")

                val texture = splitTest[2] + splitTest[3]+splitTest[4]

                // texture 값을 정리하고 허용된 값만 유지
                val cleanedTexture = texture.split(" ")
                    .filter { it in validTextures }
                    .joinToString(" ")

                val price = splitTest[5]

                println("-------------")
                println(product)
                println("=============")
                println("이름: $name")
                println("규격: $size")
                println("재질: $cleanedTexture")
                println("가격: $price\n")
                println("=============")

                salesListXncData.add(XncData(name, size, cleanedTexture, price))
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
