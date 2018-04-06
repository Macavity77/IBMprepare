import java.util.HashMap;
import java.util.Map;

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

    public static String code2(String input) {
        if (input.length() == 0) {
            return input;
        }
        String[] words = input.split("\\|");
        /**pre-process using the map-reduce function*/
        String[] processed = new String[words.length];
        int index = 0;
        for (String origin : words) {
            processed[index++] = mapreduce(origin);
            /**compare using the map reduced version, also make sure the earier version is contained*/
            for (int i = 0; i < index - 1; i++) {
                if (processed[i] == null) {
                    continue;
                }
                /**if they are equal*/
                if (processed[index - 1].equals(processed[i])) {
                    if (words[index - 1].length() < words[i].length()) {
                        words[i] = null;
                        processed[i] = null;
                    } else {
                        words[index - 1] = null;
                        processed[index - 1] = null;
                    }
                    continue;
                }
                /**if contained, then remove contained one according to rules*/
                if (processed[i].contains(processed[index - 1])) {
                    words[index - 1] = null;
                    processed[index - 1] = null;
                } else if (processed[index - 1].contains(processed[i])) {
                    words[i] = null;
                    processed[i] = null;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : words) {
            if (s != null) {
                sb.append(s + "|");
            }
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    private static String mapreduce(String input) {
        /**reduce leading, tailing and extra white space*/
        input = input.trim().replaceAll("\\s+", " ");
        String[] array = input.split(" ");
        StringBuilder sb = new StringBuilder();
        /**remove nonletter and nondigit*/
        for (int i = 0; i < array.length; i++) {
            char[] temp = array[i].toCharArray();
            String s = "";
            for (char c : temp) {
                if (Character.isLetterOrDigit(c)) {
                    s += Character.toLowerCase(c);
                }
            }
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    /*****************************************************/
    static class Node {
        String name;
        Node parent;
        Node (String s) {
            name = s;
            parent = null;
        }
    }
    private static Map<String, Node> maps;

    public static String code3(String input) {
        /**preprocessing, loaded into nodes*/
        String[] array = input.split(",");
        String p = array[array.length - 1];
        String q = array[array.length - 2];
        maps = new HashMap<>();
        for (int i = 0; i < array.length - 2; i++) {
            String[] temp = array[i].split("->");
            if (!maps.containsKey(temp[0])) {
                maps.put(temp[0], new Node(temp[0]));
            }
            if (!maps.containsKey(temp[1])) {
                maps.put(temp[1], new Node(temp[1]));
            }
            /**adding parent relationship*/
            Node cur = maps.get(temp[1]);
            cur.parent = maps.get(temp[0]);
            maps.put(temp[1], cur);
        }
        return LCA(maps.get(p), maps.get(q));
    }

    private static String LCA(Node p, Node q) {
        /**see their difference in depth*/
        int depth1 = depth(p);
        int depth2 = depth(q);
        int difference = depth1 - depth2;
        if (difference < 0) {
            Node temp = p;
            p = q;
            q = temp;
            difference = - difference;
        }
        /**make them the same level*/
        while (difference-- != 0) {
            p = p.parent;
        }
        /**then, if equal*/
        if (p == q) {
            return q.parent.name;
        }
        /**if not equal*/
        while (p != null && q != null) {
            if (p == q) {
                return p.name;
            }
            p = p.parent;
            q = q.parent;
        }
        return null;
    }

    private static int depth(Node x) {
        int dep = 0;
        while (x != null) {
            dep++;
            x = x.parent;
        }
        return dep;
    }

    public static void main(String[] args) {
//        System.out.println(code1("20 3 4"));
//        System.out.println(code2("IBM cognitive computing|IBM \"cognitive\" computing is a revolution|  ibm cognitive  computing|'IBM Cognitive Computing' is a revolution?"));
        System.out.println(code3("Frank->Mary,Mary->Sam,Mary->Bob,Sam->Katie,Sam->Pete,Bob->John,Bob,Katie"));
    }
}