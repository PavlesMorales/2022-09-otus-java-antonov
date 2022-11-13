public class Demo {

    public static void main(String[] args) {

        TestLoggingInterface testLoggingInterface = Ioc.createProxy();
        testLoggingInterface.calculation(1);
        testLoggingInterface.calculation(2, 3);
        testLoggingInterface.calculation(4, 5, "String");
    }
}
