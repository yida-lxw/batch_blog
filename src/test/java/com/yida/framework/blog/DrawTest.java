package com.yida.framework.blog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 10:02
 * @Description 这里是类的描述信息
 */
public class DrawTest {
    public static void main(String[] args) {
        Map<Integer, String> initMap = new HashMap<Integer, String>();
        initMap.put(398, "小明童鞋");
        initMap.put(3, "白小伟童鞋");
        initMap.put(1008, "李凯峰童鞋");
        initMap.put(117, "白孟寰童鞋");
        initMap.put(17, "南京-安逸童鞋");
        initMap.put(5221, "条子哥童鞋");
        initMap.put(6666, "杭州-小卡童鞋");
        initMap.put(12, "黎柱权童鞋");
        initMap.put(9, "李小雷童鞋");
        initMap.put(8647, "拯救狗不理童鞋");
        initMap.put(24, "王江波童鞋");
        initMap.put(888, "朱选林童鞋");
        initMap.put(7, "YOUNGFREE童鞋");
        initMap.put(999, "蜗牛童鞋");
        initMap.put(29, "策恒童鞋");
        initMap.put(13, "夕寻童鞋");
        initMap.put(666, "FYL童鞋");
        initMap.put(10000, "孙永强童鞋");
        initMap.put(1007, "陈传朋童鞋");
        initMap.put(975, "Xtra童鞋");
        initMap.put(777, "Terry童鞋");
        initMap.put(555, "杨帆童鞋");
        initMap.put(9870, "张晓亮童鞋");
        initMap.put(9527, "张健童鞋");
        initMap.put(6868, "晓赞童鞋");
        initMap.put(1992, "蔚蓝天空童鞋");
        initMap.put(3991, "一库大神童鞋");
        initMap.put(6668, "年华似水童鞋");
        initMap.put(8989, "李国涛童鞋");
        initMap.put(4, "朱军童鞋");
        initMap.put(120, "王硕童鞋");
        initMap.put(8888, "FC童鞋");
        initMap.put(1010, "李佳乐童鞋");
        initMap.put(816, "冯志勇童鞋");
        initMap.put(5566, "左殇童鞋");
        initMap.put(1027, "崔朋童鞋");
        initMap.put(1024, "LOMO童鞋");
        initMap.put(72, "湛童鞋");
        initMap.put(2048, "赵远童鞋");
        initMap.put(3333, "徐凌云童鞋");
        initMap.put(6890, "努力没用童鞋");
        initMap.put(8080, "陈东良童鞋");
        initMap.put(5555, "Chenglong童鞋");
        initMap.put(110, "章钢彪童鞋");
        initMap.put(7927, "林小江童鞋");
        initMap.put(502, "海外直邮童鞋");
        initMap.put(8707, "张延平童鞋");


        //用于存放随机到的次数统计结果
        Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
        countMap.put(398, 0);
        countMap.put(3, 0);
        countMap.put(1008, 0);
        countMap.put(117, 0);
        countMap.put(17, 0);
        countMap.put(5221, 0);
        countMap.put(6666, 0);
        countMap.put(12, 0);
        countMap.put(9, 0);
        countMap.put(8647, 0);
        countMap.put(24, 0);
        countMap.put(888, 0);
        countMap.put(7, 0);
        countMap.put(999, 0);
        countMap.put(29, 0);
        countMap.put(13, 0);
        countMap.put(666, 0);
        countMap.put(10000, 0);
        countMap.put(1007, 0);
        countMap.put(975, 0);
        countMap.put(777, 0);
        countMap.put(555, 0);
        countMap.put(9870, 0);
        countMap.put(9527, 0);
        countMap.put(6868, 0);
        countMap.put(1992, 0);
        countMap.put(3991, 0);
        countMap.put(6668, 0);
        countMap.put(8989, 0);
        countMap.put(4, 0);
        countMap.put(120, 0);
        countMap.put(8888, 0);
        countMap.put(1010, 0);
        countMap.put(816, 0);
        countMap.put(5566, 0);
        countMap.put(1027, 0);
        countMap.put(1024, 0);
        countMap.put(72, 0);
        countMap.put(2048, 0);
        countMap.put(3333, 0);
        countMap.put(6890, 0);
        countMap.put(8080, 0);
        countMap.put(5555, 0);
        countMap.put(110, 0);
        countMap.put(7927, 0);
        countMap.put(502, 0);
        countMap.put(8707, 0);

        System.out.println(initMap.size() + "-->" + +countMap.size());
        //随机总次数
        int times = 1;
        int min = 1;
        int max = 10000;

        int index = 0;
        while (index < times) {
            int num = randomNum(min, max);
            //如果没有一个命中,跳过继续生成下一个随机数
            if (!initMap.containsKey(num)) {
                continue;
            }
            //票数加1
            countMap.put(num, countMap.get(num) + 1);
            index++;
        }

        //打印结果
        int maxNum = 0;
        int maxKey = 0;
        String maxName = "";
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int key = entry.getKey();
            int count = entry.getValue();
            String name = initMap.get(key);
            System.out.println(name + "[" + key + "]:" + count);
            if (count > maxNum) {
                maxNum = count;
                maxKey = key;
                maxName = name;
            }
        }

        System.out.println("抽中者为:" + maxName + "[" + maxKey + "]:" + maxNum);

    }

    public static int randomNum(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        System.out.println("随机生成的随机数为：" + s);
        return s;
    }


}
