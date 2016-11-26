package tao.study;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Logger logger =new Logger();



        System.out.println( "Hello World!" +logger.getClass());
    }


    public static class Logger{

        public void info(String msg){
            System.out.println("[JavaHost]"+msg);
        }

    }

}

