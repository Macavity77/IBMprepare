public class solution {

    public static String code1(String source) {
        String[] info = source.split(" ");
        int N = Integer.valueOf(info[0]);
        int p = Integer.valueOf(info[1]);
        int q = Integer.valueOf(info[2]);
        /**check false input*/
        if (N < 1 || p < 1 || q < 1 || q == p) {
            throw new IllegalArgumentException("please have the right input");
        }
        StringBuilder sb = new StringBuilder();
        String pval = String.valueOf(p);
        String qval = String.valueOf(q);
        for (int i = 1; i <= N; i++) {
            String temp = "";
            /**requirement one*/
            if (i % q == 0 || i % p == 0) {
                temp = temp + "OUT";
            }
            String num = String.valueOf(i);
            if (num.contains(pval) || num.contains(qval)) {
                temp = temp + "THINK";
            }
            if (temp.length() == 0) {
                sb.append(i);
            } else {
                sb.append(temp);
            }
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static void main(String[] args) {
        System.out.println(code1("20 3 4"));
    }
}