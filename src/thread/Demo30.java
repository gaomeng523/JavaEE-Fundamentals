package thread;


//class Point {
//    public Point(double x , double y){
//        //.......
//    }
//
//    public Point(double r , double a){
//        //.......
//    }
//}

// 工厂设计模式
class Point {
    public Point() {

    }
    // 提供一系列的Set 方法，来针对类进行设置
    void setXXX() {

    }
}
class PointFactory {
    public static Point buildPointByXY(double x , double y) {
        Point p = new Point();
        p.setXXX();
        return p;
    }
    public static Point buildPointByRA(double r , double a) {
        Point p = new Point();
        p.setXXX();
        return p;
    }
}

public class Demo30 {
}
