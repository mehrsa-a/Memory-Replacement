import java.util.*;

public class Project {
    static int page;
    static int frame;
    static List<Integer> fifo_pages = new ArrayList<>();
    static List<Integer> lru_pages = new ArrayList<>();
    static Map<Integer, Integer> sch_pages = new LinkedHashMap<>();
    static int fifo_fault = 0;
    static int lru_fault = 0;
    static int sch_fault = 0;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("enter the frame number:");
        frame = scanner.nextInt();

        System.out.println("enter the new page number:");
        page = scanner.nextInt();

        while(page!=-1){
            FIFO();
            LRU();
            second_chance();
            System.out.println("enter the new page number:");
            page = scanner.nextInt();
        }

        System.out.println("FIFO page fault: " + fifo_fault);
        System.out.println("LRU page fault: " + lru_fault);
        System.out.println("second chance page fault: " + sch_fault);
    }

    public static void FIFO(){
        if(fifo_pages.contains(page)){
            System.out.print("FIFO: ");
            print(fifo_pages);
            return;
        }
        fifo_fault++;
        if(fifo_pages.size()<frame){
            fifo_pages.add(page);
            System.out.print("FIFO: ");
            print(fifo_pages);
            return;
        }
        fifo_pages.remove(0);
        fifo_pages.add(page);
        System.out.print("FIFO: ");
        print(fifo_pages);
    }

    public static void LRU(){
        if(lru_pages.contains(page)){
            lru_pages.removeIf(p -> p == page);
            lru_pages.add(page);
            System.out.print("LRU: ");
            print(lru_pages);
            return;
        }
        lru_fault++;
        if(lru_pages.size()<frame){
            lru_pages.add(page);
            System.out.print("LRU: ");
            print(lru_pages);
            return;
        }
        lru_pages.remove(0);
        lru_pages.add(page);
        System.out.print("LRU: ");
        print(lru_pages);
    }

    public static void second_chance(){
        if(sch_pages.containsKey(page)){
            sch_pages.replace(page, 1);
            System.out.print("S_Ch: ");
            print(sch_pages.keySet().stream().toList());
            return;
        }
        sch_fault++;
        if(sch_pages.size()<frame){
            sch_pages.put(page, 1);
            System.out.print("S_Ch: ");
            print(sch_pages.keySet().stream().toList());
            return;
        }
        int key = 0;
        Optional<Integer> firstKey = sch_pages.keySet().stream().findFirst();
        if (firstKey.isPresent()) {
            key = firstKey.get();
        }
        int bit = sch_pages.get(key);
        sch_pages.remove(key);
        while(bit == 1){
            sch_pages.put(key, 0);
            firstKey = sch_pages.keySet().stream().findFirst();
            if (firstKey.isPresent()) {
                key = firstKey.get();
            }
            bit = sch_pages.get(key);
            sch_pages.remove(key);
        }
        sch_pages.put(page, 1);
        System.out.print("S_Ch: ");
        print(sch_pages.keySet().stream().toList());
    }

    public static void print(List<Integer> pages){
        for(Integer page: pages){
            System.out.print(page+" ");
        }
        System.out.println();
    }
}
