package frontend.client;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String[] args) throws IOException {

        Map<Integer,String> map = new HashMap();
        map.put(1,"a");
        map.put(2,"b");
        map.put(3,"a");
        map.put(4,"d");
        map.put(5,"a");

        System.out.println(map);



    }
}
