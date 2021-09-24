package dev.felnull.felnulllib.util;

/**
 * 色関係
 *
 * @author MORIMORI0317
 * @since 1.0
 */
public class ColorUtil {

    /**
     * 16進数カラーコードから赤色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 赤色
     */
    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * 16進数カラーコードから緑色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 緑色
     */
    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * 16進数カラーコードから青色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 青色
     */
    public static int getBlue(int color) {
        return color & 0xFF;
    }

    /**
     * 16進数ARGBカラーコードから透過率を取得
     * 255(FF)が最大
     * 0に近いほど透明
     *
     * @param color カラーコード
     * @return 透過率
     */
    public static int getAlpha(int color) {
        return color >>> 24;
    }

    /**
     * RGBから16進数カラーコードへ変換
     *
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @return カラーコード
     */
    public static int getHexColor(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    /**
     * RGBAから16進数ARGBカラーコードへ変換
     *
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a 透過度 ０に近いほど透明
     * @return カラーコード
     */
    public static int getARGBHexColor(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    /**
     * 数字カラーコードを文字列の16新数のカラーコードに変換
     *
     * @param colorCode カラーコード
     * @return カラーコード文字列
     */
    public static String getStringHexColor(int colorCode) {
        String hex = Integer.toHexString(colorCode);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, 6 - hex.length()); i++) {
            sb.append("0");
        }
        sb.append(hex);
        return sb.toString();
    }

    /**
     * 色ごとの距離を取得
     * 16新数のカラーコード
     *
     * @param color1 色1
     * @param color2 色2
     * @return 距離
     */
    public static double getColorDistance(int color1, int color2) {
        return Math.abs(Math.sqrt(Math.pow(getRed(color1) - getRed(color2), 2) + Math.pow(getGreen(color1) - getGreen(color2), 2) + Math.pow(getBlue(color1) - getBlue(color2), 2)));
    }

    /**
     * 近似色を取得する
     * 16新数のカラーコード
     *
     * @param target 対象色
     * @param colors 比較色
     * @return 比較色の中で最も近い色
     */
    public static int getApproximateColor(int target, int... colors) {
        int most = -1;
        double mostDis = Double.MAX_VALUE;
        for (int color : colors) {
            if (target == color)
                return color;
            double dis = getColorDistance(target, color);
            if (dis < mostDis) {
                mostDis = dis;
                most = color;
            }
        }
        return most;
    }

}
