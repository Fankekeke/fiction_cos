package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.cos.entity.RelateDTO;
import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 核心算法
 */
public class CoreMath {


    /**
     * 计算相关系数并排序
     *
     * @param key
     * @param map
     * @return Map<Integer, Double>
     */
    public static Map<Integer, Double> computeNeighbor(Integer key, Map<Integer, List<RelateDTO>> map, int type) {
        Map<Integer, Double> distMap = new TreeMap<>();
        List<RelateDTO> userItems = map.get(key);
        map.forEach((k, v) -> {
            //排除此用户
            if (!k.equals(key)) {
                //关系系数
                double coefficient = relateDist(v, userItems, type);
                //关系距离
                //   double distance=Math.abs(coefficient);
                distMap.put(k, coefficient);
            }
        });
        return distMap;
    }


    /**
     * 计算两个序列间的相关系数
     *
     * @param xList
     * @param yList
     * @param type  类型0基于用户推荐 1基于物品推荐
     * @return double
     */
    private static double relateDist(List<RelateDTO> xList, List<RelateDTO> yList, int type) {
        List<Double> xs = CollectionUtil.newArrayList();
        List<Double> ys = CollectionUtil.newArrayList();
        xList.forEach(x -> {
            yList.forEach(y -> {
                if (type == 0) {
                    if (x.getItemId().equals(y.getItemId())) {
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                } else {
                    if (x.getUseId().equals(y.getUseId())) {
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                }
            });
        });
        return getRelate(xs, ys);
    }

    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     *
     * @param xs x集合
     * @param ys y集合
     */
    public static double getRelate(List<Double> xs, List<Double> ys) {
        int n = xs.size();
        //至少有两个元素
        if (n < 2) {
            return 0D;
        }
        double Ex = xs.stream().mapToDouble(x -> x).sum();
        double Ey = ys.stream().mapToDouble(y -> y).sum();
        double Ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double Ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        double Exy = IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();
        double numerator = Exy - Ex * Ey / n;
        double denominator = Math.sqrt((Ex2 - Math.pow(Ex, 2) / n) * (Ey2 - Math.pow(Ey, 2) / n));
        if (denominator == 0) {
            return 0D;
        }
        return numerator / denominator;
    }

}
