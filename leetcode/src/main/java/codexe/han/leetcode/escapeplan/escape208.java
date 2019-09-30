package codexe.han.leetcode.escapeplan;

public class escape208 {

}
class TrieNode{
    TrieNode[] nextNodes;
    boolean isWord;
}
class Trie {

    private TrieNode root;
    /** Initialize your data structure here. */
    public Trie() {
        this.root = new TrieNode();
        this.root.nextNodes = new TrieNode[26];
        for(int i=0;i<26;i++){
            this.root.nextNodes[i] = null;
        }
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode node = root;
        for(int i=0;i<word.length();i++){
            int n = word.charAt(i) - 'a';
            if(node.nextNodes[n]==null){
                node.nextNodes[n] = new TrieNode();
                if(!node.nextNodes[n].isWord) node.nextNodes[n].isWord = i==word.length()-1;//这里要注意，一定不要覆盖之前已经插入的isWord
                node.nextNodes[n].nextNodes = new TrieNode[26];
                for(int j=0;j<26;j++){
                    node.nextNodes[n].nextNodes[j] = null;
                }
            }
            else{
                if(!node.nextNodes[n].isWord) node.nextNodes[n].isWord = i==word.length()-1;//这里要注意，一定不要覆盖之前已经插入的isWord
            }
            node = node.nextNodes[n];

        }
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode node = root;
        for(int i=0;i<word.length();i++){
            int n = word.charAt(i) - 'a';
            if(node.nextNodes[n]!=null){
                if(i==word.length()-1){
                        return node.nextNodes[n].isWord;
                }
                else{
                    node = node.nextNodes[n];
                }
            }
            else{
                return false;
            }
        }
        return false;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for(int i=0;i<prefix.length();i++){
            int n = prefix.charAt(i) - 'a';
            if(node.nextNodes[n]!=null){
                if(i==prefix.length()-1){
                    return true;
                }
                else{
                    node = node.nextNodes[n];
                }
            }
            else{
                return false;
            }
        }
        return false;
    }
}
