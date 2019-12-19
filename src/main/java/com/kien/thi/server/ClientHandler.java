package com.kien.thi.server;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class ClientHandler extends Thread {
    public final int HEADER_ROW= 0;
    public final int EXAMINER_STT_COLUMN = 0;
    public final int EXAMINER_ID_COLUMN = 1;
    public final int EXAMINER_NAME_COLUMN = 2;
    public final int EXAMINER_BIRTHDAY_COLUMN = 3;
    public final int EXAMINER_UNIT_COLUMN = 4;
    public final int EXAMINER_NOTE_COLUMN = 5;

    public final int ROOM_STT_COLUMN = 0;
    public final int ROOM_NAME_COLUMN = 1;
    DataInputStream dis;
    DataOutputStream dos;

    List<Examiner> examiners;
    List<Room> rooms;
    LinkedList<Examiner> examinerOnes;
    LinkedList<Examiner> examinerTwos;
    List<Examiner> outsides;
    int totalRoom;
    int[] randomIndexes;
    Socket socket;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int fileLength = dis.readInt();
            int read = 0;
            int totalRead = 0;
            int remaining = fileLength;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                bos.write(buffer, 0, read);
            }

            getData(bos.toByteArray());
            generateRandomIndexes();
            sortRoom();
            byte[] response = generateExcel();

            dos.writeInt(response.length);
            dos.write(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sortRoom() {
        System.out.println("Starting sorting");
        for (Examiner examiner : examiners) {
            int randomToTwo = random(2);
            switch (randomToTwo) {
                case 1:
                    if(isExaminerOne(examiner)) break;
                case 2:
                    if(isExaminerTwo(examiner) || isExaminerOne(examiner)) break;
                default:
                    isOutsideExaminer(examiner);
            }
        }

        for (int i = 0; i < totalRoom ; i++) {
            Room room = getRoom(i);
            Examiner examinerOne = examinerOnes.pollFirst();
            examinerOnes.addLast(examinerOne);
            if (examinerOne != null) {
                examinerOne.setRoom(room);
            }
            Examiner examinerTwo = examinerTwos.pollFirst();
            examinerTwos.addLast(examinerTwo);
            if (examinerTwo != null) {
                examinerTwo.setRoom(room);
            }
        }
        System.out.println("Done sorting room.");
    }

    private byte[] generateExcel() {
        int stt = 1;
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row firstRow = sheet.createRow(0);

        Cell firstCell = firstRow.createCell(3);
        firstCell.setCellValue("  Cộng hòa xã hội chủ nghĩa Việt Nam " + "\n" +
                "    Độc Lập - Tự Do - Hạnh Phúc    " + "\n" +
                "DANH SÁCH PHÂN CÔNG COI THI"
        );

        sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 5));


        int rowNumber = 5;
        Row header = sheet.createRow(rowNumber++);
        int headerColumnNumber = 0;
        for (String str : Arrays.asList("STT", "Số thẻ", "Họ và tên", "Ngày sinh", "Đơn vị công tác", "Phòng thi", "Chức vụ", "Ghi chú")) {
            Cell cell = header.createCell(headerColumnNumber++);
            cell.setCellValue(str);
        }


        for (Examiner examiner : examinerOnes) {
            Row row = sheet.createRow(rowNumber++);
            int columnNumber = 0;

            Cell sttCell = row.createCell(columnNumber++);
            sttCell.setCellValue(stt++);

            Cell idCell = row.createCell(columnNumber++);
            idCell.setCellValue(examiner.getId());

            Cell nameCell = row.createCell(columnNumber++);
            nameCell.setCellValue(examiner.getName());

            Cell birthDayCell = row.createCell(columnNumber++);
            birthDayCell.setCellValue(examiner.getBirthDate());

            Cell unitCell = row.createCell(columnNumber++);
            unitCell.setCellValue(examiner.getUnit());

            Cell roomCell = row.createCell(columnNumber++);
            roomCell.setCellValue(examiner.getRoom().getName());

            Cell roleCell = row.createCell(columnNumber++);
            roleCell.setCellValue("Giám thị 1");

            Cell noteCell = row.createCell(columnNumber++);
            noteCell.setCellValue(examiner.getNote());
        }

        for (Examiner examiner : examinerTwos) {
            Row row = sheet.createRow(rowNumber++);
            int columnNumber = 0;

            Cell sttCell = row.createCell(columnNumber++);
            sttCell.setCellValue(stt++);

            Cell idCell = row.createCell(columnNumber++);
            idCell.setCellValue(examiner.getId());

            Cell nameCell = row.createCell(columnNumber++);
            nameCell.setCellValue(examiner.getName());

            Cell birthDayCell = row.createCell(columnNumber++);
            birthDayCell.setCellValue(examiner.getBirthDate());

            Cell unitCell = row.createCell(columnNumber++);
            unitCell.setCellValue(examiner.getUnit());

            Cell roomCell = row.createCell(columnNumber++);
            roomCell.setCellValue(examiner.getRoom().getName());


            Cell roleCell = row.createCell(columnNumber++);
            roleCell.setCellValue("Giám thị 2");

            Cell noteCell = row.createCell(columnNumber++);
            noteCell.setCellValue(examiner.getNote());
        }

        for (Examiner examiner: outsides) {
            Row row = sheet.createRow(rowNumber++);
            int columnNumber = 0;

            Cell sttCell = row.createCell(columnNumber++);
            sttCell.setCellValue(stt++);

            Cell idCell = row.createCell(columnNumber++);
            idCell.setCellValue(examiner.getId());

            Cell nameCell = row.createCell(columnNumber++);
            nameCell.setCellValue(examiner.getName());

            Cell birthDayCell = row.createCell(columnNumber++);
            birthDayCell.setCellValue(examiner.getBirthDate());

            Cell unitCell = row.createCell(columnNumber++);
            unitCell.setCellValue(examiner.getUnit());

            Cell roomCell = row.createCell(columnNumber++);
            roomCell.setCellValue("");

            Cell roleCell = row.createCell(columnNumber++);
            roleCell.setCellValue("Giám thị hành lang");

            Cell noteCell = row.createCell(columnNumber++);
            noteCell.setCellValue(examiner.getNote());
        }

        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done.");
        return byteArrayInputStream.toByteArray();
    }

    private void generateRandomIndexes() {
        System.out.println("Generating random indexes");
        randomIndexes = IntStream.iterate(0, i -> i+1)
                .limit(totalRoom)
                .toArray();

        for (int i = 0; i < 1000000 ;i++) {
            int randomOne = random(totalRoom) - 1;
            int randomeTwo = random(totalRoom) - 1;
            int tmp = randomIndexes[randomOne];
            randomIndexes[randomOne] = randomIndexes[randomeTwo];
            randomIndexes[randomeTwo] = tmp;
        }
        System.out.println("Done generating random indexes.");
    }

    private void getData(byte[] data) {
        System.out.println("Starting getting data");
        examiners = new LinkedList<>();
        examinerOnes = new LinkedList<>();
        examinerTwos = new LinkedList<>();
        outsides = new LinkedList<>();

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(200000)
                .bufferSize(1024*5)
                .open(bis);

        Sheet sheetOne = workbook.getSheetAt(0);
        for (Row row : sheetOne) {
            if (row.getRowNum() == HEADER_ROW) continue;
            double stt = row.getCell(EXAMINER_STT_COLUMN).getNumericCellValue();
            if (stt == 0) break;
            String id = row.getCell(EXAMINER_ID_COLUMN).getStringCellValue();
            String name = row.getCell(EXAMINER_NAME_COLUMN).getStringCellValue();
            String birthDay = row.getCell(EXAMINER_BIRTHDAY_COLUMN).getStringCellValue();
            String unit = row.getCell(EXAMINER_UNIT_COLUMN).getStringCellValue();
            String note = "";
            if (row.getCell(EXAMINER_NOTE_COLUMN) != null) {
                note = row.getCell(EXAMINER_NOTE_COLUMN).getStringCellValue();
            }
            examiners.add(new Examiner(id,birthDay,unit,name,note));
        }

        Sheet sheetTwo = workbook.getSheetAt(1);

        rooms = new ArrayList<>(examiners.size()/2);

        for (Row row : sheetTwo) {
            if (row.getRowNum() == HEADER_ROW) continue;
            double stt = row.getCell(ROOM_STT_COLUMN).getNumericCellValue();
            if (stt == 0) break;
            String name = row.getCell(ROOM_NAME_COLUMN).getStringCellValue();
            rooms.add(new Room(name));
        }
        this.totalRoom = rooms.size();
        System.out.println("Done getting data.");
    }

    private int random(int number) {
        return (int) (Math.random()*number) + 1;
    }

    private Room getRoom(int i) {
        Room room = rooms.get(randomIndexes[i]);
        return room;
    }

    private boolean isExaminerOne(Examiner examiner) {
        return examinerOnes.size() < totalRoom ? examinerOnes.add(examiner) : false;
    }


    private boolean isExaminerTwo(Examiner examiner) {
        return examinerTwos.size() < totalRoom ? examinerTwos.add(examiner) : false;
    }

    private boolean isOutsideExaminer(Examiner examiner) {
        return outsides.add(examiner);
    }


}
