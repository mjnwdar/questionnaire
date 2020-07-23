package com.zzq.system.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.feature.StandardScaler;
import org.apache.spark.mllib.feature.StandardScalerModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.rdd.RDD;
/**
 * @author Sherlock
 * @copyright freeman
 * @since 2020/7/16 22:10
 */
public class LogisticRegression {
    public static void train(String filePath) {
        // 实例化sparkConf对象
        SparkConf conf = new SparkConf().setAppName("l").setMaster("local[3]");
        // 生成上下文对象
        JavaSparkContext context = new JavaSparkContext(conf);
        // 读取文本为rdd
        JavaRDD<String> lines = context.textFile(filePath);
        // map分割数据，生成标签向量rdd
        JavaRDD<LabeledPoint> labeledPoint = lines.map((Function<String, LabeledPoint>) line -> {
            // 分割lable和feature
            String[] lf = line.split(",");
            // 分割feature字符串转double
            String[] SFeatures = lf[1].split(" ");
            double[] features = new double[SFeatures.length];
            int index = 0;
            for(String s:SFeatures){
                features[index] = Double.parseDouble(s);
            }
            return new LabeledPoint(Double.parseDouble(lf[0]), Vectors.dense(features));
        });

        // 通过训练数据创建model
        LogisticRegressionModel model = LogisticRegressionWithSGD.train(labeledPoint.rdd(),30);
        System.out.println("未特征变量标准化之前的权重值为：\r\n" + model.weights().toString());
        // 准备测试数据测试模型
        Vector testData1 = Vectors.dense(1.0,1.0,1.0,1.0);
        Vector testData2 = Vectors.dense(0.0,0.0,0.0,1.0);
        // 预测操作
        double res1 = model.predict(testData1);
        double res2 = model.predict(testData2);
        // 打印测试结果
        System.out.println("测试数据" + testData1.toString() + "预测结果为："+ res1);
        System.out.println("测试数据" + testData2.toString() + "预测结果为："+ res2);

        // 获取特征数据
        JavaRDD<Vector> featuresJRDD = labeledPoint.map((Function<LabeledPoint, Vector>) LabeledPoint::features);
        RDD<Vector> features = featuresJRDD.rdd();
        // 标准化特征变量操作
        StandardScalerModel standardScalerModel = new StandardScaler(true,true).fit(features);
        JavaRDD<LabeledPoint> newJRDD = labeledPoint.map((Function<LabeledPoint, LabeledPoint>) lab -> new LabeledPoint(lab.label(),standardScalerModel.transform(lab.features())));
        // 将javaRDD转化成RDD
        RDD<LabeledPoint> newRDD = newJRDD.rdd();
        // 模型训练
        LogisticRegressionModel sdModel= LogisticRegressionWithSGD.train(newRDD,30);
        System.out.println("特征变量标准化之后的权重值为：\r\n" + sdModel.weights().toString());
        // 数据测试
        Vector testData3 = Vectors.dense(1.0,1.0,1.0,1.0);
        Vector testData4 = Vectors.dense(0.0,0.0,0.0,1.0);
        Vector testData5 = Vectors.dense(1.0,0.0,1.0,0.0);
        double res3 = sdModel.predict(testData3);
        double res4 = sdModel.predict(testData4);
        double res5 = sdModel.predict(testData5);
        System.out.println("标准化特征变量后，测试数据" + testData3.toString() + "预测结果为："+ res3);
        System.out.println("标准化特征变量后，测试数据" + testData4.toString() + "预测结果为："+ res4);
        System.out.println("标准化特征变量后，测试数据" + testData5.toString() + "预测结果为："+ res5);
    }
}
