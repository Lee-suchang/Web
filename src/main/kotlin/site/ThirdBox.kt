package site

import data.ThirdBoxData
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
import kotlin.math.ceil

class ThirdBox(private val driver: WebDriver) {
    private val salesListbk = mutableListOf<ThirdBoxData>()

    fun start() {
        driver.get("https://www.3sk.co.kr/goods/goods_list.php?cateCd=001003")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        saveToExcel()


    }

    fun getList() {
        var productIndex = 1
        while (true) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[1]/div[2]/div[6]/div/div/form/table/tbody/tr[$productIndex]")).text
                val splitTest = product.split("\n")
                val splitTest2 = splitTest[1].split(" ")
                val name = splitTest[0].substring(0, splitTest[0].indexOf("["))
                val size = splitTest2[0]

                val paperNumberIndex = if (splitTest2[4] == "톰슨") 5 else 4
                val paperNumberWithJang = splitTest2[paperNumberIndex]
                val paperNumber = paperNumberWithJang.replace("장", "").trim()

                val priceIndex = if (splitTest2[4] == "톰슨") 6 else 5
                val priceWithWon = splitTest2[priceIndex]
                val price = priceWithWon.replace("원", "").replace(",", "").trim()

                val pricePerPaperNumber = ceil(price.toDouble() / paperNumber.toDouble()).toInt()

                salesListbk.add(
                    ThirdBoxData(
                        name = name,
                        size = size,
                        paperNumber = paperNumber,
                        price = price,
                        pricePerPaperNumber = pricePerPaperNumber
                    )
                )

                println("이름 : $name")
                println("사이즈 : $size")
                println("포장수 : $paperNumber")
                println("가격 : $price")
                println("가격/포장수 : $pricePerPaperNumber")
            } catch (e: Exception) {
                break
            }
            productIndex++
        }
    }

    fun saveToExcel() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("3sk박스")
        val headers = listOf("이름", "사이즈", "포장수량", "전체가격", "장당가격")

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
            dataRow.createCell(2).setCellValue(salesItem.paperNumber)
            dataRow.createCell(3).setCellValue(salesItem.price)
            dataRow.createCell(4).setCellValue(salesItem.pricePerPaperNumber.toString())

        }

        val fileOut = FileOutputStream("$FILE_PATH\\3skBox_suchang.xlsx")
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }


    fun quit() {
        driver.quit()
    }
}