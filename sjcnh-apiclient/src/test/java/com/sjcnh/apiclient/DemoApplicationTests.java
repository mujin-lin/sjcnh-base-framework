package com.sjcnh.apiclient;

import com.sjcnh.apiclient.feign.FeignClient;
import com.sjcnh.apiclient.test.ResponseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private FeignClient feignClient;

    private static final List<String> list = new ArrayList<>();


    @Test
    void contextLoads() {
    }

    @Test
    void invokeFeignClient() {
        ResponseTest response = feignClient.requestBaidu();

        System.out.println(response);
//        System.out.println(feignClient.getClass().getSimpleName());
    }

    public static void main(String[] args) throws Exception {
//        HttpGet httpGet = new HttpGet("http://localhost:8001/global/encrypt");
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpResponse execute = client.execute(httpGet);
//        HttpEntity entity = execute.getEntity();
//        ResponseTest response = response(entity.getContent());
//
//        String transToken = response.getResData().getTransToken();
//        String transEncryptKey = response.getResData().getTransEncryptKey();
//        String encrypt = EncryptUtils.aesEncrypt("123456", transEncryptKey);
//
//        Map<String,String> map = new HashMap<>();
//        map.put("userName","admin");
//        map.put("password",encrypt);
//        map.put("transToken",transToken);
//        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] bytes = objectMapper.writeValueAsBytes(map);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//
//
//        HttpEntity postEntity = new InputStreamEntity(inputStream);
//        HttpPost httpPost = new HttpPost("http://localhost:8001/sysUser/login");
//        httpPost.addHeader(HttpHeaders.CONTENT_TYPE,"application/json");
//        httpPost.addHeader("RequestSource","backStage");
//        httpPost.setEntity(postEntity);
//        HttpResponse post = client.execute(httpPost);
//        ThreadPoolExecutor


    }

    private static ResponseTest response(InputStream inputStream) throws IOException {
        int i ;
        byte[] bytes = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (( i = inputStream.read(bytes)) != -1){
            sb.append(new String(bytes,0,i, StandardCharsets.UTF_8));
        }
        inputStream.close();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(sb.toString(),ResponseTest.class);
    }



    public static int addDigits(int num) {
        return (num-1)%9 +1;
    }


    private static int fib(int n) {
        if (n < 2) return n;
        int prev = 0, curr = 1;
        for (int i = 0; i < n - 1; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }


    private static void selectionSort() {
        int[] arr = {2, 4, 5, 7, 545, 2, 54, 6, 4, 1, 4, 58, 9, 7, 86, 1, 4, 3, 5};
        int[] arrCopy = {2, 4, 5, 7, 545, 2, 54, 6, 4, 1, 4, 58, 9, 7, 86, 1, 4, 3, 5};

        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[minIndex] > arr[j]){
                    minIndex = j;
                }
            }
            if (minIndex != i){
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }

        System.out.println(Arrays.toString(arr));
        System.out.println("-------Arrays.sort------");
        Arrays.sort(arrCopy);
        System.out.println(Arrays.toString(arrCopy));
    }

    /**
     * 插入排序
     *
     * @return void
     * @throws
     * @author chenglin.wu
     * @date: 2021/8/31
     */
    private static void insertSort() {
        int[] arr = {2, 4, 5, 7, 545, 2, 54, 6, 4, 1, 4, 58, 9, 7, 86, 1, 4, 3, 5};
        int[] arrCopy = {2, 4, 5, 7, 545, 2, 54, 6, 4, 1, 4, 58, 9, 7, 86, 1, 4, 3, 5};

        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j - 1];

                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("-------Arrays.sort------");
        Arrays.sort(arrCopy);
        System.out.println(Arrays.toString(arrCopy));
    }

    private static void calcMonthly() {
        LocalDateTime of = LocalDateTime.of(2021, 12, 1, 0, 0);
        Date from = Date.from(of.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        // 得到当前日期的Integer值
        Integer monthly = Integer.parseInt(simpleDateFormat.format(from));
        // 获得年份
        Integer year = monthly / 100;
        //  获取当前月份
        Integer currentMonth = monthly % 100;
        // 获取计算的下个月的年份和下个月的月份
        Integer nextMonthYear = year;
        Integer nextMonth = currentMonth + 1;
        // 如果当前月是12月，那么计算的下个月应该是明年的1月
        if (currentMonth == 12) {
            nextMonthYear += 1;
            nextMonth = 1;
        }
        // 下个月的monthly
        int nextMonthly = nextMonthYear * 100 + nextMonth;

        System.out.println(nextMonthly);
        System.out.println(monthly);
    }

    static class TestUser {
        private String name;

        private Integer age;

        private BigDecimal sexNum;

        public TestUser(String name, Integer age, BigDecimal sexNum) {
            this.name = name;
            this.age = age;
            this.sexNum = sexNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public BigDecimal getSexNum() {
            return sexNum;
        }

        public void setSexNum(BigDecimal sexNum) {
            this.sexNum = sexNum;
        }

        @Override
        public String toString() {
            return "TestUser{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sexNum=" + sexNum +
                    '}';
        }
    }

}
