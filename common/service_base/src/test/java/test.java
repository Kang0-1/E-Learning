import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        int i=FirstNotRepeatingChar("abcdb");
        System.out.println(i);
    }

    public static int FirstNotRepeatingChar(String str) {
        if(str.length()==0){
            return -1;
        }
        HashMap<Character,Integer> map = new HashMap<>();
        for(int i=0;i<str.length();i++){
            if(map.containsKey(str.charAt(i))){
                return map.get(str.charAt(i))+1;
            }
            char c=str.charAt(i);
            map.put(c,i);
        }
        return -1;
    }
}
