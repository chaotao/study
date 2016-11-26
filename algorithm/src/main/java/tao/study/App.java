package tao.study;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {


//        MergeSort.sort(new Object[]{
//
//                9,8,7,6,5,4,3,2,1
//        });
        System.out.println( "begin" );


        try {

            int i = 0;
            while (true) {


                List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
                for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
                    ((com.sun.management.GarbageCollectorMXBean) gcMxBean).getLastGcInfo();
                }

                i++;

                if (i > 100000) {
                    break;
                }

                if(i%100==0) {
                    Thread.sleep(100);
                }

            }


            System.out.println("System.gc---------------");
            System.gc();

            System.out.println("end");

        }catch (Exception e){
            e.printStackTrace();
        }



    }


    private void testGC(){
        List<byte[]> list =new ArrayList<byte[]>();

        for (int i=0;i<100;i++){
//            list.add(new byte[1024*1024*3]);
            byte[] temp= new byte[1024*1024*50];
        }

        list=null;

    }
}
