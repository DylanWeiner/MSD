package assignment07;

public class MediocreHashFunctor implements HashFunctor {
    @Override
    public int hash(String item) {
        int hash = 0;
        for(char c : item.toCharArray()) {
            hash += c;
        }
        return hash;
    }
}