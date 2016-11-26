package tao.study.disk;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by qinluo.ct on 16/11/14.
 */
public class App {

    public static void main(String[] args) {

        int fileSize = 1024 * 1024 * 1024;

        String fileName = "/Users/tao/data/temp/testmap";

        File file = new File(fileName);


        FileChannel fileChannel = null;

        long start = System.currentTimeMillis();
        MappedByteBuffer mappedByteBuffer = null;
        try {

            if (!file.exists()) {
                file.createNewFile();
            }


            fileChannel = new RandomAccessFile(file, "rw").getChannel();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);


            long t1 = System.currentTimeMillis();
            long t2 = 0L;
            for (int i = 0; i < 1024; i++) {

                write(mappedByteBuffer, 1024 * 1024);

                t1 = System.currentTimeMillis();
//                MappedByteBuffer byteBuffer = (MappedByteBuffer)mappedByteBuffer.slice();
//                System.out.println(byteBuffer.position());
//                byteBuffer.force();
                mappedByteBuffer.force();
                t2 = System.currentTimeMillis();
                System.out.println("dur=" + (t2 - t1) + ",size=" + i);
            }
//
//            mappedByteBuffer.flip();
//
//            mappedByteBuffer.get();
//            mappedByteBuffer.compact();
//
//            mappedByteBuffer.force();
//
//
//            mappedByteBuffer.put((byte) 97);
            t1 = System.currentTimeMillis();
            mappedByteBuffer.force();
            t2 = System.currentTimeMillis();
            System.out.println("dur=" + (t2 - t1));
            t1 = t2;
            mappedByteBuffer.force();
            t2 = System.currentTimeMillis();
            System.out.println("dur=" + (t2 - t1));


            long end = System.currentTimeMillis();
            System.out.println("write file end,t=" + end + ",dur=" + (end - start));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    private static void write(MappedByteBuffer mappedByteBuffer, long size) {
        for (int i = 0; i < size; i++) {
            mappedByteBuffer.put((byte) 97);
        }

    }
}
