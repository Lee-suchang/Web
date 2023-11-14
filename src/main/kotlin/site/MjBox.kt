package site

import data.MjBoxData
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

class MjBox(private val driver: WebDriver) {
    private val mjBoxList = mutableListOf<MjBoxData>()

    fun getList2() = mjBoxList
    fun start() {
        println("---MJBOX START---")
        driver.get("https://www.mjbox.co.kr/goods/goods_list.php?cateCd=001")
        Thread.sleep(1500)
        Actions(driver).sendKeys(Keys.ESCAPE).perform()
        getList()
        saveToExcel()
        println("---MJBOX END---")
    }

    fun quit() {
        driver.quit()
    }

    fun getList() {
        var productIndex = 1
        while (true) {
            try {
                val product =
                    driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[2]/div[5]/div/div[2]/div/table/tbody/tr[$productIndex]")).text

                val splitTest = product.split("\n")
                var nameSize = splitTest[0]
                var price = splitTest[1]

                // "추가"가 포함된 경우 해당 정보 제거
                if ("추가" in nameSize) {
                    nameSize = nameSize.split("추가")[0]
                    price = ""
                }

                val nameSizeParts = nameSize.split(" ")
                val name = nameSizeParts[0]

                // 현재 size 값
                var size = nameSizeParts.subList(1, nameSizeParts.size - 4).joinToString(" ")

                // 공백을 제거하고 "X"를 소문자로 변경
                size = size.replace(" ", "").replace("X", "x").trim()

                val specialStrings = listOf(
                    "[회원한정할인]", "(더강함TK박스)",
                    "(우체국1호)", "(우체국2호)", "(우체국3호)",
                    "(우체국4호)", "(우체국5호)", "(우체국6호)",
                    "1호", "2호"
                )

                for (specialString in specialStrings) {
                    if (size.startsWith(specialString)) {
                        size = size.removePrefix(specialString).trim()
                    }
                }

                var paperNumber = nameSizeParts[nameSizeParts.size - 3]
                paperNumber = paperNumber.replace("매", "").trim()

                // "원"을 제거하고 쉼표(,)를 제거하여 "00,000" 형식에서 "00000" 형식으로 변환
                val priceValue = price.replace("원", "").replace(",", "").trim()

                // 가격과 포장수 정보를 사용하여 추가적인 연산 수행
                val priceInt = priceValue.toInt()
                val paperCountInt = paperNumber.toInt()
                val totalPrice = (priceInt / paperCountInt).toString()

                val totalPaperCount = paperNumber // 기존 포장수 정보 유지
                println("------------")
                println("$productIndex 번째 상품")
                println("이름 : $name")
                println("사이즈 : $size")
                println("포장수 : $paperNumber")
                println("가격 : $priceValue")
                println("장당 가격 : $totalPrice")
                println("------------")

                mjBoxList.add(MjBoxData(name, size, paperNumber, priceValue, totalPrice, totalPaperCount))

            } catch (e: Exception) {
                println("오류 한번발생")
                if (productIndex > 230)
                    break
            }
            productIndex++
        }
    }

    fun saveToExcel() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("명진포장")
        val headers = listOf("이름", "사이즈", "포장수", "가격", "장당 가격")

        val headerRow: Row = sheet.createRow(0)
        for (i in headers.indices) {
            val cell: Cell = headerRow.createCell(i)
            cell.setCellValue(headers[i])
        }

        for (i in mjBoxList.indices) {
            val salesItem = mjBoxList[i]
            val dataRow: Row = sheet.createRow(i + 1)
            dataRow.createCell(0).setCellValue(salesItem.name)
            dataRow.createCell(1).setCellValue(salesItem.size)
            dataRow.createCell(2).setCellValue(salesItem.totalPaperCount) // 포장수 변경
            dataRow.createCell(3).setCellValue(salesItem.price)
            dataRow.createCell(4).setCellValue(salesItem.totalPrice) // 장당 가격 추가
        }

        val fileOut = FileOutputStream("$FILE_PATH\\mjBox_suchang.xlsx")
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }
}
