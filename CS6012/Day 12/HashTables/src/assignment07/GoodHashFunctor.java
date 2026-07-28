package assignment07;

public class GoodHashFunctor implements HashFunctor {
    @Override
    public int hash(String item) {
        int hash = 7;
        for(char c : item.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return (int) (hash % Integer.MAX_VALUE);
    }
}
