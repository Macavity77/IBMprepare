import java.io.BufferedInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    static class dvalue {
        int sum = 0;
        double ave = 0;
        Set<String> store = new HashSet<>();
        dvalue (String s, int x) {
            sum = x;
            ave = (double) x;
            store.add(s);
        }
    }
    /**Storing item info*/
    public static void code4() throws ParseException {
        Map<Date, dvalue> map = new TreeMap<>();
        /**pre-processing*/
        Scanner stdin = new Scanner(new BufferedInputStream(System.in));
        while (stdin.hasNext()) {
            String cur = stdin.nextLine();
            String[] info = cur.split(",");
            /**processing the date*/
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(info[0]);
            int num = Integer.valueOf(info[1]);
            String name = String.valueOf(info[2]);
            if (!map.containsKey(date)) {
                map.put(date, new dvalue(name, num));
                continue;
            }
            /**adding record to this date*/
            dvalue temp = map.get(date);
            temp.sum += num;
            /**if not contains, just add into that*/
            if (!temp.store.contains(name)) {
                temp.store.add(name);
            }
            temp.ave = (double) temp.sum / temp.store.size();
            /**put back*/
            map.put(date, temp);
        }
        /**TreeMap is going to do sorting automatically*/
        for (Date d : map.keySet()) {
            dvalue temp = map.get(d);
            System.out.println(d + "," + temp.sum + "," + temp.ave + "," + temp.store.size());
        }
    }

    static final String NEW_FORMAT = "yyyy-MM-dd";
    static final SimpleDateFormat formatter = new SimpleDateFormat(NEW_FORMAT);
    public static void code5(String[] input) throws ParseException {
        Map<Date, dvalue> map = new TreeMap<>();
        /**pre-processing*/
        for (String a : input) {
            String[] info = a.split(",");
            /**processing the date*/
            Date date = formatter.parse(info[0]);
            int num = Integer.valueOf(info[1]);
            String name = String.valueOf(info[2]);
            if (!map.containsKey(date)) {
                map.put(date, new dvalue(name, num));
                continue;
            }
            /**adding record to this date*/
            dvalue temp = map.get(date);
            temp.sum += num;
            /**if not contains, just add into that*/
            if (!temp.store.contains(name)) {
                temp.store.add(name);
            }
            temp.ave = (double) temp.sum / temp.store.size();
            /**put back*/
            map.put(date, temp);
        }
        /**TreeMap is going to do sorting automatically*/

        for (Date d : map.keySet()) {
            dvalue temp = map.get(d);
            System.out.println(formatter.format(d) + "," + temp.sum + "," + String.format("%.2f", temp.ave) + "," + temp.store.size());
        }
    }

    /**
     * Pharmacy Store, Trie Tree in Java
     * */
    private static class TrieNode{
        char val;
        boolean isLeaf;
        TreeMap<Character, TrieNode> children = new TreeMap<>();
        public TrieNode(char c) {
            val = c;
            isLeaf = false;
        }
    }

    static TrieNode root = new TrieNode(' ');
    public static void insert(String word) {
        TreeMap<Character, TrieNode> children = root.children;
        char[] bank = word.toLowerCase().toCharArray();
        for (int i = 0; i < bank.length; i++) {
            TrieNode temp;
            if (children.containsKey(bank[i])) {
                temp = children.get(bank[i]);
            } else {
                temp = new TrieNode(bank[i]);
                children.put(bank[i], temp);
            }
            children = temp.children;
            if (i == word.length() - 1) {
                temp.isLeaf = true;
            }
        }
    }

    public static List<String> getWordsForPrefix(String prefix) {
        TrieNode cur = root;
        List<String> result = new ArrayList<>();
        char[] bank = prefix.toLowerCase().toCharArray();
        for (int i = 0; i < bank.length; i++) {
            if (!cur.children.containsKey(bank[i])) {
                return result;
            }
            cur = cur.children.get(bank[i]);
        }
        helper(cur, prefix, result, 3);
        return result;
    }

    private static void helper(TrieNode cur, String s, List<String> result, int limit) {
        if (result.size() > limit - 1) {
            return;
        }
        if (cur != null) {
            if (cur.isLeaf == true) {
                result.add(s);
            }
        }
        for (char c : cur.children.keySet()) {
            String temp = s + c;
            helper(cur.children.get(c),temp, result, limit);
        }
        return;
    }


    /************************************************************************/

    public static void main(String[] args) throws ParseException {
//        System.out.println(code1("20 3 4"));
//        System.out.println(code2("IBM cognitive computing|IBM \"cognitive\" computing is a revolution|  ibm cognitive  computing|'IBM Cognitive Computing' is a revolution?"));
//        System.out.println(code3("Frank->Mary,Mary->Sam,Mary->Bob,Sam->Katie,Sam->Pete,Bob->John,Bob,Katie"));
//        String[] input = new String[]{"2018-06-24,1,people","2016-04-01,4,pies", "2016-04-01,6,pies", "2016-04-01,2,cakes", "2016-04-01,12,cookies","2016-04-02,7,pumpkins","2016-04-02,2,peaches", "2016-04-03,6,apples", "2016-04-03,23,pies","2016-04-03,4,cookies","2016-04-03,9,peaches"};
//        code5(input);
        String[] pharm = new String[]{"asperin", "aspeor", "aap", "bsje", "bsfjke", "bsdjfe", "cvfr", "cvfro", "cvfropd", "cdase", "asperins"};
        for (String s : pharm) {
            insert(s);
        }
        List<String> result = getWordsForPrefix("a");
        System.out.println(result.toString());
    }
}