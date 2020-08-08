package bigbee.rabbit.spider.test1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListToMap_Demo {

    public static void main(String[] args) {
        List<StandardSportTourenament> list = new ArrayList<>();
        StandardSportTourenament s1 = new StandardSportTourenament();
        s1.setId(1L);
        s1.setName("杭锅联赛");
        s1.setName_code("9627");

        StandardSportTourenament s2 = new StandardSportTourenament();
        s2.setId(2L);
        s2.setName("冠军联赛");
        s2.setName_code("6382746827");

        StandardSportTourenament s3 = new StandardSportTourenament();
        s3.setId(3L);
        s3.setName("中华铁骑");
        s3.setName_code("xjbgoooooooooooooo");


        list.add(s1);
        list.add(s2);
        list.add(s3);

        Map<String,String> maps = list.stream().collect(Collectors.toMap(sst -> sst.getName(), sst -> sst.getName_code()));
        System.out.println(maps);
    }
}
