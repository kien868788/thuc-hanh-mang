package com.kien.thi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class TestDataGenerator {
    private static final int ONE_MILLION = 1000000;
    public static void main(String[] args) throws Exception {
        int rowNumber = 0;
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheetOne = workbook.createSheet("Danh sách giáo viên");

        //header
        Row headerRowOne = sheetOne.createRow(rowNumber++);
        Cell sttHeaderCell = headerRowOne.createCell(0);
        sttHeaderCell.setCellValue("TT");

        Cell idHeaderCell = headerRowOne.createCell(1);
        idHeaderCell.setCellValue("Số thẻ");

        Cell nameHeaderCell = headerRowOne.createCell(2);
        nameHeaderCell.setCellValue("Họ và tên");

        Cell birthDate = headerRowOne.createCell(3);
        birthDate.setCellValue("Ngày sinh");

        Cell workingUnitHeaderCell = headerRowOne.createCell(4);
        workingUnitHeaderCell.setCellValue("Đơn vị công tác");

        Cell noteHeaderCell = headerRowOne.createCell(5);
        noteHeaderCell.setCellValue("Note");

        for (int i = 1; i <= ONE_MILLION + 48575; i++) {
            Row row = sheetOne.createRow(rowNumber++);
            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(i);

            Cell idCell = row.createCell(1);
            idCell.setCellValue(100+i);

            Cell nameCell = row.createCell(2);
            nameCell.setCellValue("Trần văn " + i);

            Cell bithDateCell = row.createCell(3);
            bithDateCell.setCellValue(LocalDate.now().toString());

            Cell workingUnitCell = row.createCell(4);
            workingUnitCell.setCellValue("Khoa CNTT" + i);

            Cell noteCell = row.createCell(5);
            noteCell.setCellValue("note");
        }

        Sheet sheetTwo = workbook.createSheet("Danh sách phòng thi");
        rowNumber = 0;
        Row headerRowTwo = sheetTwo.createRow(rowNumber++);
        Cell sttHeaderCellTwo = headerRowTwo.createCell(0);
        sttHeaderCellTwo.setCellValue("STT");

        Cell nameHeaderCellTwo = headerRowTwo.createCell(1);
        nameHeaderCellTwo.setCellValue("Tên phòng thi");

        Cell locationHeaderCellTwo = headerRowTwo.createCell(3);
        locationHeaderCellTwo.setCellValue("Địa điểm");

        Cell noteHeaderCellTwo = headerRowTwo.createCell(4);
        noteHeaderCellTwo.setCellValue("Note");

        for (int i = 1; i <= ONE_MILLION/2; i++) {
            Row row = sheetTwo.createRow(rowNumber++);
            Cell sttCellTwo = row.createCell(0);
            sttCellTwo.setCellValue(i);

            Cell nameCellTwo = row.createCell(1);
            nameCellTwo.setCellValue("A" + i);

            Cell locationCellTwo = row.createCell(3);
            locationCellTwo.setCellValue("H" + i);

            Cell notCellTwo = row.createCell(4);
            notCellTwo.setCellValue("note");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        FileOutputStream fileInputStream = new FileOutputStream(new File("/home/kien/data-test-thi-thuc-hanh.xlsx"));
        fileInputStream.write(data);
    }
}
