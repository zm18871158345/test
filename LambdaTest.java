package www.bigbee.com.Lamdba;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import www.bigbee.com.Lamdba.action.Book;

public class LambdaTest {
	
	/*
	 * 需求将一个get请求的参数出入到Map中
	 */	
	@Test
	public void test1(){
		
		String getParams = "page=1&token=aidsiurffsie392838suduf2h9&username=zdm&nick=djb&type=alipay";
		Map<String,String> map =Stream.of(getParams.split("&")).map(str -> str.split("=")).collect(Collectors.toMap(x -> x[0], x -> x[1]));
		
		System.out.println(map);
		List<String> xjbg = Stream.of(getParams.split("&")).map(str -> str.split("=")[0]).collect(Collectors.toList());
		System.out.println("xjbg="+xjbg);
	}
	
	@Test
	public void test2(){
		//1:将全部的id取出来放在一个集合中
		Set<Integer> ss = books().stream().map(book -> book.getId() ).collect(Collectors.toSet());
		System.out.println("+++++++++"+ss.toString());
		//一个表达式只能有一个终止操作
		//books().stream().map(x -> x.getId()).forEach(System.out::println);
		List<Integer> idList = books().stream().map(x -> x.getId()).collect(Collectors.toList());
		System.out.println(idList);
		
		//2:把所有的id通过";"连接起来
		String str = books().stream().map(x -> x.getId()+"").collect(Collectors.joining(","));
		System.out.println("str="+str);
		
		String str2 = books().stream().map(x -> x.getId()+"").collect(Collectors.joining(",","(",")"));
		System.out.println("str2="+str2);
		
		String str3 = books().stream().map(x -> "'"+x.getId()+"'").collect(Collectors.joining(",","(",")"));
		System.out.println("str3="+str3);	
		
	}
	
	@Test
	public void test3(){
		//找出所有书的类型
		List<String> typeList = books().stream().map(book -> book.getType()).collect(Collectors.toList());
		//System.out.println(typeList);
		
		//找出去重后的所有书的类型
		List<String> typeList2 = books().stream().map(book -> book.getType()).distinct().collect(Collectors.toList());
		//System.out.println(typeList2);
		
		//也可以通过set的方式去重
		Set<String> typeList3 =  books().stream().map(book -> book.getType()).collect(Collectors.toSet());
		//System.out.println(typeList3);
		
		//根据价格排序 低-->高
		//books().stream().sorted( (book1,book2) -> Double.compare(book1.getPrice(), book2.getPrice()) ).forEach(System.out::println);
		
		//根据价格排序 高-->低
		Comparator<Book> comp = (book1,book2) -> Double.compare(book1.getPrice(), book2.getPrice());
		//books().stream().sorted( comp.reversed()).forEach(System.out::println);
		
		//价格相同按照出版时间的顺序排序
		//Comparator<Book> conparate = (book1,book2) -> Double.compare(book1.getPrice(), book2.getPrice());
		//books().stream().sorted( conparate.thenComparing( (book1,book2) -> book1.getPublishDate().isAfter(book2.getPublishDate()) ? 1 : -1 )).forEach(System.out::println);
		
		//排序更简单的
		System.out.println("=====================================================");
		//books().stream().sorted(Comparator.comparing(Book::getPrice)).forEach(System.out::println);
		//books().stream().sorted(Comparator.comparing(Book::getPrice).reversed()).forEach(System.out::println);
		books().stream().sorted(Comparator.comparing(Book::getPrice).reversed().thenComparing(Comparator.comparing(Book::getPublishDate).reversed())).forEach(System.out::println);
	}
	
	
	@Test
	public void test5(){
		Map<Integer, Book> getBookInfo = books().stream().collect(Collectors.toMap(book -> book.getId(), book -> book));
		//System.out.println(getBookInfo);
		
		//统计书的平均价格
		Double agvPrice = books().stream().collect(Collectors.averagingDouble(Book::getPrice));
		System.out.println("所有书的平均价格:"+agvPrice);
		
		//找出价格最大与最小的书
		 Optional<Book> maxBook = books().stream().collect(Collectors.maxBy(Comparator.comparing(Book::getPrice)));
		if(maxBook.isPresent()){
			System.out.println("最贵的书"+maxBook.get());
		}
		
		Optional<Book> minBook = books().stream().collect(Collectors.minBy(Comparator.comparing(Book::getPrice)));
		if(minBook.isPresent()){
			System.out.println("最便宜的书"+minBook.get());
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test6(){
		//最贵且最新出的那本书
		Comparator maxPri = Comparator.comparing(Book::getPrice);
		Optional<Book> getBook = books().stream().collect(Collectors.maxBy(maxPri.thenComparing(Comparator.comparing(Book::getPublishDate))));
		if(getBook.isPresent()){
			System.out.println("最贵且最新出的那本书="+getBook.get());
		}
	}
	
	@Test
	public void test7(){
		//将所有的书进行分类
		 Map<String, List<Book>>  bclass = books().stream().collect(Collectors.groupingBy(Book::getType));
		 //System.out.println(bclass);
		 bclass.keySet().forEach( key -> {
			 System.out.println(key);
			 System.out.println(bclass.get(key));
			 System.out.println("---------------------------");
		 });
		 
		 //每个类型有几本书
		 Map<String, Long>  bookNum = books().stream().collect(Collectors.groupingBy(Book::getType , Collectors.counting()));
		 bookNum.keySet().forEach( key -> {
			 System.out.println( key +":"+ bookNum.get(key));
		 });
		 
		 //每个类型的总价
		 Map<String, Double>  booksum = books().stream().collect(Collectors.groupingBy(Book::getType , Collectors.summingDouble(Book::getPrice)));
		 booksum.keySet().forEach( key -> {
			 System.out.println( key +"="+ booksum.get(key));
		 });
		 
		 //每个类型的平均价格
		 Map<String, Double>  bookavg = books().stream().collect(Collectors.groupingBy(Book::getType , Collectors.averagingDouble(Book::getPrice)));
		 bookavg.keySet().forEach( key -> {
			 System.out.println( key +"=>"+ bookavg.get(key));
		 });
		 
		 //每一种类型最贵的
		 Map<String, Optional<Book>>  typeBook = books().stream().collect(Collectors.groupingBy(Book::getType,Collectors.maxBy(Comparator.comparing(Book::getPrice))));
		 typeBook.keySet().forEach( key -> {
			 System.out.println( key +"====>"+ typeBook.get(key));
		 });
		 
		 //每一种类型书出版是最晚的
		 Map<String, Optional<Book>>  lastBook = books().stream().collect(Collectors.groupingBy(Book::getType,Collectors.maxBy(Comparator.comparing(Book::getPublishDate))));
		 lastBook.keySet().forEach( key -> {
			 System.out.println( key +"*******>"+ lastBook.get(key));
		 });
		 
	}
	
	@Test
	public void test8(){
		//filter测试
		//选择金额在68以上的书，并且按出版时间的先后顺序排列
		books().stream().filter( book -> book.getPrice() >=68).sorted(Comparator.comparing(Book::getPublishDate)).forEach(System.out::println);
	}
	
	//Book(int id, double price, String type, String name, LocalDate publishDate) 
	private List<Book> books(){
		List<Book> books = new ArrayList<Book>();
		books.add(new Book(1, 60, "编程语言", "java", LocalDate.parse("2015-10-06")));
		books.add(new Book(2, 30.00, "编程语言", "python", LocalDate.parse("2017-03-08")));
		books.add(new Book(3, 50, "编程语言", "c++", LocalDate.parse("2021-05-01")));
		books.add(new Book(4, 68, "编程语言", "js", LocalDate.parse("2016-06-11")));
		books.add(new Book(5, 42, "编程语言", "swift", LocalDate.parse("2022-01-01")));
		books.add(new Book(6, 126, "编程语言", "php", LocalDate.parse("2012-03-24")));
		books.add(new Book(7, 60, "编程语言", "c", LocalDate.parse("2009-03-24")));
		books.add(new Book(8, 60, "农业机械", "bigbee", LocalDate.parse("2020-03-24")));
		books.add(new Book(9, 60, "房车", "balong", LocalDate.parse("2025-03-24")));
		books.add(new Book(10, 561, "房车", "bolong", LocalDate.parse("2025-03-24")));
		books.add(new Book(11, 561, "梦想", "liaofang8", LocalDate.parse("2020-03-24")));
		books.add(new Book(12, 88, "梦想", "game", LocalDate.parse("2020-03-24")));
		books.add(new Book(13, 60, "梦想", "footbool", LocalDate.parse("2021-03-24")));
		books.add(new Book(14, 60, "梦想", "keyhero", LocalDate.parse("2021-03-24")));
		books.add(new Book(15, 60, "梦想", "dataglod", LocalDate.parse("2023-03-24")));
		return books;
		
	}
	

}
